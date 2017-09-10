package com.file.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by wenteng on
 * 2017/9/10.
 */
public interface FileService {

    String uploadPDF(MultipartFile file, String resourcesPath);

    String uploadPicture(MultipartFile file, String resourcesPath);

    String uploadVideo(MultipartFile file, String resourcesPath);

    String uploadEpub(MultipartFile file, String resourcesPath);
}
