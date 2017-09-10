package com.file.service.impl;

import com.file.service.FileService;
import com.file.utils.DateUtils;
import com.file.utils.JSONUtils;
import com.file.utils.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wenteng on
 * 2017/9/10.
 */
@Service
@Transactional
public class FileServiceImpl implements FileService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 上传pdf文件函数
     *
     * @param file          pdf文件
     * @param resourcesPath respurces的绝对路径
     * @return 上传操作信息
     */
    public String uploadPDF(MultipartFile file, String resourcesPath) {
        String pdfRelativePath = getRelativePath("book/document", "pdf");
        String coverRelativePath = getRelativePath("book/cover", "png");

        File pdfFile = getFile(resourcesPath, pdfRelativePath);
        File coverFile = getFile(resourcesPath, coverRelativePath);

        PDDocument pdfDocument = null;
        try {
            pdfFile.createNewFile();
            PDFRenderer pdfRenderer = new PDFRenderer(PDDocument.load(file.getInputStream()));
            BufferedImage coverImage = pdfRenderer.renderImage(0);

            ImageIO.write(coverImage, "PNG", coverFile);
            file.transferTo(pdfFile);
        } catch (Exception e) {
            e.printStackTrace();
            return JSONUtils.operateError("文件上传失败");
        }

        logger.info("上传的pdf封面图片路径:" + coverRelativePath);
        logger.info("上传的pdf文件路径:" + pdfRelativePath);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        PDDocumentInformation informationDocument = pdfDocument.getDocumentInformation();
        resultMap.put("filePath", pdfRelativePath);
        resultMap.put("coverFilePath", coverRelativePath);
        resultMap.put("creator", informationDocument.getCreator());
        resultMap.put("title", informationDocument.getTitle());
        resultMap.put("author", informationDocument.getAuthor());
        resultMap.put("keywords", informationDocument.getKeywords());
        resultMap.put("producer", informationDocument.getProducer());
        resultMap.put("subject", informationDocument.getSubject());
        resultMap.put("trapped", informationDocument.getTrapped());
        resultMap.put("numberPage", pdfDocument.getNumberOfPages());
        resultMap.put("publishTime", informationDocument.getCreationDate());
        return JSONUtils.operateSuccess(resultMap);
    }


    /**
     * 上传图片函数
     *
     * @param file          图片文件
     * @param resourcesPath respurces的绝对路径
     * @return 上传操作信息
     */
    public String uploadPicture(MultipartFile file, String resourcesPath) {
        String pictureRelativePath = getRelativePath("picture", "png");
        File pictureFile = getFile(resourcesPath, pictureRelativePath);

        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            ImageIO.write(image, "PNG", pictureFile);
        } catch (Exception e) {
            e.printStackTrace();
            return JSONUtils.operateError("文件上传失败");
        }

        logger.info("上传的图片路径:" + pictureRelativePath);
        return JSONUtils.operateSuccess(pictureRelativePath);
    }

    /**
     * 上传视频文件
     *
     * @param file          视频文件
     * @param resourcesPath respurces的绝对路径
     * @return 上传操作信息
     */
    public String uploadVideo(MultipartFile file, String resourcesPath) {
        String videoRelativePath = getRelativePath("video", "mp4");
        File videoFile = getFile(resourcesPath, videoRelativePath);
        try {
            file.transferTo(videoFile);
        } catch (Exception e) {
            e.printStackTrace();
            return JSONUtils.operateError("视频文件上传失败");
        }
        logger.info("上传的视频路径:" + videoRelativePath);
        return JSONUtils.operateSuccess(videoRelativePath);
    }

    /**
     * 上传epub文件
     *
     * @param file          epub文件
     * @param resourcesPath respurces的绝对路径
     * @return 上传操作信息
     */
    public String uploadEpub(MultipartFile file, String resourcesPath) {
        String epubRelativePath = getRelativePath("epub", "epub");
        File epubFile = getFile(resourcesPath, epubRelativePath);
        try {
            file.transferTo(epubFile);
        } catch (Exception e) {
            e.printStackTrace();
            return JSONUtils.operateError("文件上传失败");
        }
        logger.info("上传的epub路径:" + epubRelativePath);
        return JSONUtils.operateSuccess(epubRelativePath);
    }

    /**
     * @param prefix 文件所在的目录
     * @param suffix 文件后缀
     * @return 文件的相对resources目录的地址信息
     */
    private String getRelativePath(String prefix, String suffix) {
        String fileName = StringUtils.generate8Uuid();
        String currentDate = DateUtils.getStringDate(new Date());
        return "/" + prefix + "/" + currentDate + "/" + fileName + "." + suffix;
    }

    /**
     * @param resourcesPath 绝对路径的前缀部分
     * @param relativePath  绝对路径的后缀部分
     * @return 文件File对象
     */
    private File getFile(String resourcesPath, String relativePath) {
        File file = new File(resourcesPath + relativePath);
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        return file;
    }
}
