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
public class LinkedInOAuth2IdentityProvider implements OAuth2Provider<String, String>{
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;


    private static final String AUTH_URL = "https://www.linkedin.com/oauth/v2/authorization";
    private static final String TOKEN_URL = "https://www.linkedin.com/oauth/v2/accessToken";
    private static final String PROFILE_URL = "https://api.linkedin.com/v2/userinfo";
    private static final String EMAIL_URL = "https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))";


    public LinkedInOAuth2IdentityProvider(
            @Value("${linkedin.client.id}") String clientId,
            @Value("${linkedin.client.secret}") String clientSecret,
            @Value("${linkedin.redirect.url}") String redirectUri,
            UserRepository userRepository, RestTemplate restTemplate) {
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
        return ServiceProvider.LINKEDIN;
    }

    @Override
    public String buildAuthorizationUrl(Map<String, Object> params) {
        String state = "secureRandomState"; // For CSRF protection if you want
        String scope = "openid%20email%20profile";

        return AUTH_URL + "?" +
                "response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&scope=" + scope +
                "&state=" + state;
    }

    @Override
    public Users registerAccount(OAuth2RequestDto payload) throws Exception {
        String code = payload.getCode();

        String accessToken = fetchAccessToken(code);

        LinkedInProfile profile = fetchLinkedInProfile(accessToken);

        Users user = userRepository.findUsersByLinkedInId(profile.sub());
        if (user == null) {
            user = new Users();
            user.setLinkedInId(profile.sub());
            user.setUsername(profile.name());
            user.setEmail(profile.email());
            userRepository.save(user);
        } else {
            boolean updated = false;
            String newName = profile.name();
            if (!newName.equals(user.getUsername())) {
                user.setUsername(newName);
                updated = true;
            }
            if (!profile.email.equals(user.getEmail())) {
                user.setEmail(profile.email);
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
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("code", code);
        form.add("redirect_uri", redirectUri);
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        ResponseEntity<AccessTokenResponse> response = restTemplate.exchange(
                TOKEN_URL, HttpMethod.POST, request, AccessTokenResponse.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Failed to fetch LinkedIn access token");
        }

        return response.getBody().access_token();
    }

    private LinkedInProfile fetchLinkedInProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<LinkedInProfile> response = restTemplate.exchange(
                PROFILE_URL, HttpMethod.GET, entity, LinkedInProfile.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Failed to fetch LinkedIn profile");
        }

        return response.getBody();
    }



    public record AccessTokenResponse(String access_token, String expires_in) {}

    public record LinkedInProfile(
            String given_name, String family_name, String email, String picture,
            String sub, String email_verified, LinkedInLocalResponse locale, String name
    ) {}

    public record LinkedInLocalResponse(String country, String language) {}
}
