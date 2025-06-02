package com.esprit.gitesprit.git.domain.model;

import com.esprit.gitesprit.git.domain.enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryContent {
    private String name; // The name of the file or directory itself (e.g., "src", "README.md")
    private String path; // The full path relative to the repository root (e.g., "src/main/java/MyClass.java")
    private ContentType type; // FILE or DIRECTORY
    private byte[] content; // For files, the byte content. Null for directories.

    // You might want to add a convenience method to get content as String if it's text
    public String getContentAsString() {
        if (type == ContentType.FILE && content != null) {
            return new String(content); // Default charset, consider specifying UTF-8
        }
        return null;
    }
}
