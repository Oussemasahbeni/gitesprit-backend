package com.esprit.gitesprit.cloudstorage.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    private UUID id;
    private String url;
    private String name;
    private String extension;
    private Long size;
}
