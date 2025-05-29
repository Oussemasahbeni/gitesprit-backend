package com.esprit.gitesprit.cloudstorage.domain.service;

import com.esprit.gitesprit.cloudstorage.domain.model.Blob;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class CloudStorageImp implements CloudStorage {


    @Override
    public Blob uploadFile(MultipartFile file) {
        return null;
    }

    @Override
    public void deleteFile(String url) {

    }

    @Override
    public Blob uploadImage(MultipartFile file) {
        return null;
    }

    @Override
    public void deleteImage(String url) {

    }
}


