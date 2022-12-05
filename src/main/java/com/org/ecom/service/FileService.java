package com.org.ecom.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public interface FileService {
    String uploadImage(String userName, String path, MultipartFile file) throws IOException;

    InputStream getUserImage(String path, String fileName) throws FileNotFoundException;

    public Set<String> listFilesUsingDirectoryStream();
}
