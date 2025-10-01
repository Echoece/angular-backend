package com.echo.backend.auth.providers;

import com.echo.backend.auth.dto.OAuth2RequestDto;
import com.echo.backend.auth.enums.ServiceProvider;
import com.echo.backend.auth.enums.ThirdPartyServiceType;
import com.echo.backend.entity.auth.Users;
import com.echo.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GitHubOAuth2IdentityProvider implements OAuth2Provider<String,String> {
    private static final String AUTH_URL = "https://github.com/login/oauth/authorize";
    private static final String TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String USER_URL = "https://api.github.com/user";
    private static final String EMAILS_URL = "https://api.github.com/user/emails";

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public GitHubOAuth2IdentityProvider(
            @Value("${github.client.id}") String clientId,
            @Value("${github.client.secret}") String clientSecret,
            @Value("${github.redirect.url}") String redirectUri,
            UserRepository userRepository,
            RestTemplate restTemplate
    ) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }


    @Override
    public ThirdPartyServiceType getServiceType() {
        return ThirdPartyServiceType.IDENTITY_PROVIDER;
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return ServiceProvider.GITHUB;
    }

    @Override
    public String buildAuthorizationUrl(Map<String, Object> params) {
        String state = "secureRandomState";
        String scope = "read:user user:email";

        return AUTH_URL + "?" +
                "client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&scope=" + scope +
                "&state=" + state;
    }

    @Override
    public Users registerAccount(OAuth2RequestDto payload) throws Exception {
        String code = payload.getCode();
        String accessToken = fetchAccessToken(code);
        GitHubUser githubUser = fetchGitHubUser(accessToken);
        String email = fetchGitHubEmail(accessToken);

        // 🔒 Upsert user
        Users user = userRepository.findUsersByGithubId(githubUser.id());
        if (user == null) {
            user = new Users();
            user.setGithubId(githubUser.id());
            user.setUsername(githubUser.login());
            user.setEmail(email);
            user.setAvatarUrl(githubUser.avatar_url());
            userRepository.save(user);
        } else {
            boolean updated = false;
            if (!user.getUsername().equals(githubUser.login())) {
                user.setUsername(githubUser.login());
                updated = true;
            }
            if (!user.getEmail().equals(email)) {
                user.setEmail(email);
                updated = true;
            }
            if (!user.getAvatarUrl().equals(githubUser.avatar_url())) {
                user.setAvatarUrl(githubUser.avatar_url());
                updated = true;
            }
            if (updated) {
                userRepository.save(user);
            }
        }
        return user;
    }

    @Override
    public Boolean isAuthenticated(String uniqueId) throws Exception {
        return null;
    }

    @Override
    public String renewAccessToken(String refreshToken) {
        return "";
    }

    @Override
    public String loadCredential(String uniqueId) throws Exception {
        return "";
    }

    private String fetchAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("code", code);
        body.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<GitHubAccessTokenResponse> response = restTemplate.postForEntity(
                TOKEN_URL, request, GitHubAccessTokenResponse.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Failed to exchange GitHub code for access token");
        }

        return response.getBody().access_token();
    }

    private GitHubUser fetchGitHubUser(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GitHubUser> response = restTemplate.exchange(
                USER_URL, HttpMethod.GET, entity, GitHubUser.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Failed to fetch GitHub user profile");
        }

        return response.getBody();
    }

    private String fetchGitHubEmail(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GitHubEmail[]> response = restTemplate.exchange(
                EMAILS_URL, HttpMethod.GET, entity, GitHubEmail[].class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null || response.getBody().length == 0) {
            throw new RuntimeException("Failed to fetch GitHub emails");
        }

        for (GitHubEmail email : response.getBody()) {
            if (email.primary() && email.verified()) {
                return email.email();
            }
        }

        // fallback: first email
        return response.getBody()[0].email();
    }

    public record GitHubAccessTokenResponse(String access_token, String scope, String token_type) {}

    public record GitHubUser(String id, String login, String avatar_url) {}

    public record GitHubEmail(String email, boolean primary, boolean verified) {}

}
