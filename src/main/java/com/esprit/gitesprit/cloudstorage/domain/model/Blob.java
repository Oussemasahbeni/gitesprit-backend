package com.esprit.gitesprit.cloudstorage.domain.model;

public record Blob(String name, String type, String url) {
    public String getUrl() {
        return url;
    }
}
