package com.echo.backend.auth.providers;

import com.echo.backend.auth.dto.OAuth2RequestDto;
import com.echo.backend.auth.enums.ServiceProvider;
import com.echo.backend.auth.enums.ThirdPartyServiceType;
import com.echo.backend.entity.auth.Users;
import com.echo.backend.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class GoogleOAuth2IdentityProvider implements OAuth2Provider<String, String> {
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private final List<String> scopes = Arrays.asList("openid", "profile", "email");

    private final String redirectUri;
    private final String clientId;
    private final UserRepository userRepository;

    private final GoogleAuthorizationCodeFlow flow;

    public GoogleOAuth2IdentityProvider(
            UserRepository userRepository,
            @Value("${google.redirect.url}") String redirectUri,
            @Value("${google.client.id}") String clientId,
            @Value("${google.client.secret}") String clientSecret
    ) {
        this.redirectUri = redirectUri;
        this.clientId = clientId;
        this.userRepository = userRepository;
        this.flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientId, clientSecret, scopes
        ).build();
    }

    @Override
    public ThirdPartyServiceType getServiceType() {
        return ThirdPartyServiceType.IDENTITY_PROVIDER;
    }

    @Override
    public ServiceProvider getServiceProvider() {
        return ServiceProvider.GOOGLE;
    }

    @Override
    public String buildAuthorizationUrl(Map<String, Object> params) {
        GoogleAuthorizationCodeRequestUrl googleAuthorizationCodeRequestUrl = flow.newAuthorizationUrl()
                .setRedirectUri(redirectUri)
                .setAccessType("offline")
                .setApprovalPrompt("force");


        /*if (payload.getParams().containsKey("state")) {
            url.setState((String) params.get("state"));
        }*/

        return googleAuthorizationCodeRequestUrl.build();
    }

    @Override
    public Users registerAccount(OAuth2RequestDto payload) throws Exception {
        String code = payload.getCode();

        GoogleTokenResponse response = flow.newTokenRequest(code)
                .setRedirectUri(redirectUri)
                .execute();

        return buildUserFromTokenResponse(response);
    }

    @Override
    public Boolean isAuthenticated(String uniqueId) throws Exception {
        return null;
    }

    @Override
    public String renewAccessToken(String refreshToken) {
        try {
            GoogleTokenResponse tokenResponse = flow.newTokenRequest(refreshToken)
                    .setGrantType("refresh_token")
                    .execute();
            return tokenResponse.getAccessToken();
        } catch (IOException e) {
            throw new RuntimeException("Failed to renew access token", e);
        }
    }

    @Override
    public String loadCredential(String uniqueId) throws Exception {
        return "";
    }


    private Users buildUserFromTokenResponse(GoogleTokenResponse tokenResponse) throws IOException {
        GoogleIdToken idToken = tokenResponse.parseIdToken();

        if (idToken == null) {
            throw new RuntimeException("Invalid ID token");
        }

        GoogleIdToken.Payload idPayload = idToken.getPayload();

        // Optionally verify audience, issuer, expiry here if you want:
        // e.g. check if idPayload.getAudience().equals(clientId)

        // Verify token audience, issuer, expiry
        if (!idPayload.getAudience().equals(clientId)) {
            throw new RuntimeException("Invalid audience");
        }

        String issuer = idPayload.getIssuer();
        if (!"accounts.google.com".equals(issuer) && !"https://accounts.google.com".equals(issuer)) {
            throw new RuntimeException("Invalid issuer");
        }

        if (idPayload.getExpirationTimeSeconds() * 1000 < System.currentTimeMillis()) {
            throw new RuntimeException("Token expired");
        }

        String googleUserId = idPayload.getSubject();  // Google's unique user ID
        String email = idPayload.getEmail();
        String name = (String) idPayload.get("name");
        String pictureUrl = (String) idPayload.get("picture");

        // 4. Check your DB if user exists by googleUserId or email
        Users user = userRepository.findUsersByGoogleId(googleUserId);
        if (user == null) {
            user = new Users();
            user.setGoogleId(googleUserId);
            user.setEmail(email);
            user.setUsername(name);
            user.setAvatarUrl(pictureUrl);

            userRepository.save(user);
        } else {
            boolean updated = false;
            if (!user.getEmail().equals(email)) {
                user.setEmail(email);
                updated = true;
            }
            if (!user.getUsername().equals(name)) {
                user.setUsername(name);
                updated = true;
            }
            if (!user.getAvatarUrl().equals(pictureUrl)) {
                user.setAvatarUrl(pictureUrl);
                updated = true;
            }

            if (updated) {
                userRepository.save(user);
            }
        }

        return user;
    }
}
