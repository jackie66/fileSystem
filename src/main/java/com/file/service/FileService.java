package com.file.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wenteng on
 * 2017/9/10.
 */
public interface FileService {

    String uploadPDF(MultipartFile file, HttpServletRequest request);

    String uploadPicture(MultipartFile file, HttpServletRequest request);

    String uploadVideo(MultipartFile file, HttpServletRequest request);

    String uploadEpub(MultipartFile file, HttpServletRequest request);
}
