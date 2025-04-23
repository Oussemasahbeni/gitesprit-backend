package com.esprit.gitesprit.cloudstorage.infrastructure.controller;

import com.esprit.gitesprit.cloudstorage.domain.model.Blob;
import com.esprit.gitesprit.cloudstorage.domain.service.CloudStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/cloudstorage")
@RequiredArgsConstructor
public class TestController {
    private final CloudStorage cloudStorage;

    @PostMapping(value = "/test", consumes = "multipart/form-data")
    public Blob test(@RequestPart("file") MultipartFile file) {
        return cloudStorage.uploadFile(file);
    }

    @DeleteMapping("/test")
    public void delete() {
        var url = "https://d2qvd60zjmjxsq.cloudfront.net/files/1e1c53ca-d62c-4088-876d-7bac043905a1-maxresdefault.jpg";
        cloudStorage.deleteFile(url);
    }
}
