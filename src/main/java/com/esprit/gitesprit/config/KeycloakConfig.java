package com.esprit.gitesprit.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.keycloak.OAuth2Constants.CLIENT_CREDENTIALS;

@Configuration
public class KeycloakConfig {

    @Value("${keycloak.url}")
    public String serverUrl;

    @Value("${keycloak.realm}")
    public String realm;

    @Value("${keycloak.client}")
    public String clientId;

    @Value("${keycloak.credentials.secret}")
    public String clientSecret;

    /**
     * this bean creates a Keycloak object. The Keycloak object is used to interact with Keycloak
     * admin services.
     */
    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .grantType(CLIENT_CREDENTIALS)
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }

}
