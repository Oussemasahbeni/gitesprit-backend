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

  public static final String[] ADMIN_WHITE_LIST = {
    "/thematics/**",
    "/parent-thematic/**",
    "/axes/**",
    "/objectives/**",
    "/library/**",
    "/resources/**",
    "/actors/**",
    "/admin/users/**",
    "/newsletter/schedule",
    "/newsletter/draft",
    "/newsletter/cancel-schedule/**",
    "/blogs/toggle-publish/{postId}"
  };

  public static final String[] DELETE_WHITE_LIST = {
    "/newsletter/{id}",
    "/resources/{id}",
    "/blogs/toggle-publish/{postId}",
    "/blogs/{id}",
    "/articles/{id}",
    "/actors/{id}",
    "/thematics/{id}",
    "/parent-thematic/{id}",
    "/axes/{id}",
    "/objectives/{id}",
    "/library/{id}",
  };

  public static final String[] PUT_WHITE_LIST = {
    "/newsletter/{id}",
    "/articles/{id}",
    "/actors/{id}",
    "/thematics/{id}",
    "/parent-thematic/{id}",
    "/axes/{id}",
    "/objectives/{id}",
    "/library/{id}",
  };
}
