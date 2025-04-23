package com.esprit.gitesprit.auth.infra.dto.request;

import com.esprit.gitesprit.shared.validators.NotEmptyCollection;

import java.util.List;

public record findAllIdpUserRequest(@NotEmptyCollection List<String> roles, String search, int page, int size) {}
