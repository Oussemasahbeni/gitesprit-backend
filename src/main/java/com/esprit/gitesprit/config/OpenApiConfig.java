package com.esprit.gitesprit.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info =
                @Info(
                        contact = @Contact(name = "Oussema sahbeni", email = "oussemas@inspark.tn"),
                        description = "OpenApi documentation for agriculture project",
                        title = "OpenApi specification",
                        version = "1.0",
                        license = @License(name = "Licence name", url = "https://some-url.com"),
                        termsOfService = "Terms of service",
                        extensions = {
                            @Extension(
                                    name = "x-logo",
                                    properties = {
                                        @ExtensionProperty(
                                                name = "url",
                                                value =
                                                        "https://res.cloudinary.com/dyo9yeeck/image/upload/v1738671010/cabuysinjrz1xl9aqopm.png"),
                                        @ExtensionProperty(name = "altText", value = "Project Logo")
                                    })
                        }),
        servers = {@Server(description = "Env ", url = "${application.backend.url}")},
        security = {@SecurityRequirement(name = "keycloak")})
@SecurityScheme(
        name = "keycloak",
        type = SecuritySchemeType.OAUTH2,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        flows =
                @OAuthFlows(
                        authorizationCode =
                                @OAuthFlow(
                                        authorizationUrl =
                                                "${keycloak.url}/realms/${keycloak.realm}/protocol/openid-connect/auth",
                                        tokenUrl =
                                                "${keycloak.url}/realms/${keycloak.realm}/protocol/openid-connect/token",
                                        scopes = {
                                            @OAuthScope(name = "openid", description = "OpenID Connect scope"),
                                            @OAuthScope(name = "profile", description = "Profile access")
                                        })))
public class OpenApiConfig {}
