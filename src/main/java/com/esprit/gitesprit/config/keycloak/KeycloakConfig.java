package com.esprit.gitesprit.config.keycloak;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.keycloak.OAuth2Constants.CLIENT_CREDENTIALS;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(KeycloakAdminClientProperties.class)
public class KeycloakConfig {

    private final KeycloakAdminClientProperties properties;


    /**
     * this bean creates a Keycloak object. The Keycloak object is used to interact with Keycloak
     * admin services.
     */
    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .grantType(CLIENT_CREDENTIALS)
                .serverUrl(properties.getServerUrl())
                .realm(properties.getRealm())
                .clientId(properties.getClientId())
                .clientSecret(properties.getClientSecret())
                .build();
    }

}
