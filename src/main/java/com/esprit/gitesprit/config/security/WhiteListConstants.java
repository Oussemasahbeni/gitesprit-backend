package com.esprit.gitesprit.config.security;

public class WhiteListConstants {

  public static final String[] AUTH_WHITE_LIST = {
    "/api-docs/**",
    "/swagger-resources/**",
    "/configuration/ui",
    "/configuration/security",
    "/docs/**",
    "/swagger-ui.html",
    "/swagger-ui/**",
    "/webjars/**",
    "/docs.html",
    "/swagger-ui/index.html",
    "/favicon.ico",
    "/ws/**",
    "/actuator/**",
    "/api/v1/auth/**",
    "/api/v1/notifications/**",
    "/api/v1/codes/verify/**",
    "/api/v1/kc/sync"
  };


}
