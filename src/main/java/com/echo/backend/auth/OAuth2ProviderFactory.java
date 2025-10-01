package com.echo.backend.auth;

import com.echo.backend.auth.providers.OAuth2Provider;
import com.echo.backend.auth.enums.ServiceProvider;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// dynamically get all implementing providers with constructors injection at initialization, getProvider
// then lets us get the providers.

/* What problem it solves?

->  Without the factory:

        if (provider.equals("GOOGLE")) {
          googleProvider.doSomething();
        } else if (provider.equals("GITHUB")) {
          githubProvider.doSomething();
        } ...

    and the list goes on once we add more service providers

->  With the factory:

        OAuth2Provider provider = oauth2ProviderFactory.getProvider(ServiceProvider.GOOGLE);
        provider.buildAuthRequestUrl(params);

    and this dynamically gets all the implemented OAuth2Providers and finds the right one,
* */
@Component
public class OAuth2ProviderFactory {
    private final Map<ServiceProvider, OAuth2Provider> providers;

    public OAuth2ProviderFactory(List<OAuth2Provider> auth2Providers) {
        Map<ServiceProvider, OAuth2Provider> map = new HashMap<>();
        for (OAuth2Provider provider : auth2Providers) {
            map.put(provider.getServiceProvider(), provider);
        }
        this.providers = Collections.unmodifiableMap(map);
    }

    public OAuth2Provider getProvider(ServiceProvider providerName) {
        OAuth2Provider provider = providers.get(providerName);
        if (provider == null) {
            throw new UnsupportedOperationException("Unsupported OAuth2 provider: " + providerName);
        }
        return provider;
    }
}
