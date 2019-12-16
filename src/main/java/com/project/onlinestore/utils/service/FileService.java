package com.project.onlinestore.utils.service;


import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void writeFile(String path,String text);
    String readFile(String path);
    void transferImage(String path, MultipartFile image,String extension);
}
