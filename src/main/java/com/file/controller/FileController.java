package com.file.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.file.utils.DateUtils;
import com.file.utils.JSONUtils;
import com.file.utils.StringUtils;

@Controller
@RequestMapping(value = "file")
public class FileController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@ResponseBody
	@RequestMapping(value = "upload")
	public String uploadFile(MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
		String fileContentType = file.getContentType();

		String resourcesPath = request.getSession().getServletContext().getRealPath("/resources");

		if (fileContentType.equals("application/pdf")) {
			return uploadDocument(file, resourcesPath);
		} else if (fileContentType.equals("image")) {
			return uploadPicture(file, resourcesPath);
		} else if (fileContentType.equals("video/mp4")) {
			return uploadVideo(file, resourcesPath);
		} else if (fileContentType.equals("application/epub+zip")) {

		}

		return null;
	}

	/**
	 * 保存上传滴PDF文档和文档的封面，如果保存出错，则需要删除已保存的文件 如果顺利执行，则返回保存的文档路径，封面路径。
	 * 文档的title，author，keywords，numberPage，createTime
	 * 
	 * @throws Exception
	 */
	public String uploadDocument(MultipartFile file, String resourcesPath) {
		String documentRelativeFilePath = getRelativeFilePath("book/document", "pdf");
		String pictureRelativeFilePath = getRelativeFilePath("book/cover", "png");

		File documentFile = new File(resourcesPath + documentRelativeFilePath);
		File pictureFile = new File(resourcesPath + pictureRelativeFilePath);
		if (!documentFile.getParentFile().exists()) {
			documentFile.mkdirs();
		}
		if (!pictureFile.getParentFile().exists()) {
			pictureFile.mkdirs();
		}
		PDDocument pdfDocument = null;
		try {
			documentFile.createNewFile();
			pdfDocument = PDDocument.load(file.getInputStream());
			PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);
			BufferedImage pdfImage = pdfRenderer.renderImage(0);

			ImageIO.write(pdfImage, "PNG", pictureFile);
			file.transferTo(documentFile);
		} catch (IOException e) {
			e.printStackTrace();
			if (documentFile.exists())
				documentFile.delete();
			if (pictureFile.exists())
				pictureFile.delete();
			return JSONUtils.operateError("文件上传失败");
		}

		logger.info("上传cover的路径：" + pictureRelativeFilePath);
		logger.info("上传pdf的路径：" + documentRelativeFilePath);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		PDDocumentInformation informationDocument = pdfDocument.getDocumentInformation();

		resultMap.put("filePath", documentRelativeFilePath);
		resultMap.put("pictureFilePath", pictureRelativeFilePath);
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
	 * 需要删除的文件路径保存filePath参数内
	 * 
	 * @return 返回已删除的文件，删除错误的文件，不存在的文件。
	 */
	@ResponseBody
	@RequestMapping(value = "remove", method = RequestMethod.POST)
	public String deleteDocument(HttpServletRequest request) {
		String resourcesPath = request.getSession().getServletContext().getRealPath("/resources");
		List<String> removed = new LinkedList<String>();
		List<String> notRemoved = new LinkedList<String>();
		List<String> notExisted = new LinkedList<String>();

		String[] filePaths = request.getParameterValues("filePath");
		if (filePaths != null && filePaths.length > 0) {
			for (String filePath : filePaths) {
				File file = new File(resourcesPath + "/" + filePath);
				if (file.exists()) {
					if (file.delete()) {
						removed.add(filePath);
					} else {
						notRemoved.add(filePath);
					}
				} else {
					notExisted.add(filePath);
				}
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("remvoed", removed);
		resultMap.put("notRemoved", notRemoved);
		resultMap.put("notExisted", notExisted);
		return JSONUtils.operateSuccess(resultMap);
	}

	/**
	 * 在某个模块下保存图片
	 */
	public String uploadPicture(MultipartFile file, String resourcesPath) {
		String pictureRelativeFilePath = getRelativeFilePath("picture", "png");

		File pictureFile = new File(resourcesPath + pictureRelativeFilePath);

		try {
			BufferedImage image = ImageIO.read(file.getInputStream());
			if (!pictureFile.getParentFile().exists()) {
				pictureFile.mkdirs();
			}
			ImageIO.write(image, "PNG", pictureFile);
		} catch (Exception e) {
			e.printStackTrace();
			return JSONUtils.operateError("上传失败");
		}

		logger.info("上传picture的路径：" + pictureRelativeFilePath);
		return JSONUtils.operateSuccess(pictureRelativeFilePath);
	}

	public String uploadVideo(MultipartFile file, String resourcesPath) {
		if (!file.getContentType().equals("video/mp4")) {
			return JSONUtils.operateError("文件格式不正确");
		}
		String videoRelativeFilePath = getRelativeFilePath("video", "mp4");

		File videoFile = new File(resourcesPath + videoRelativeFilePath);
		if (!videoFile.getParentFile().exists()) {
			videoFile.mkdirs();
		}
		try {
			file.transferTo(videoFile);
		} catch (Exception e) {
			e.printStackTrace();
			return JSONUtils.operateError("文件上传失败");
		}

		logger.info("上传video的路径：" + videoRelativeFilePath);
		return JSONUtils.operateSuccess(videoRelativeFilePath);
	}

	public String uploadBookEpub(MultipartFile file, String resourcesPath) {
		if (!file.getContentType().equals("application/epub+zip")) {
			return JSONUtils.operateError("文件格式不正确");
		}

		String epubRelativeFilePath = getRelativeFilePath("epub", "epub");

		File bookFile = new File(resourcesPath + epubRelativeFilePath);
		if (!bookFile.getParentFile().exists()) {
			bookFile.mkdirs();
		}
		try {
			file.transferTo(bookFile);
		} catch (Exception e) {
			e.printStackTrace();
			return JSONUtils.operateError("文件上传失败");
		}

		logger.info("上传epub的路径：" + epubRelativeFilePath);
		return JSONUtils.operateSuccess(epubRelativeFilePath);
	}

	/**
	 * 生成文件的目录
	 */
	private String getRelativeFilePath(String prefix, String suffix) {
		String fileName = StringUtils.generate8Uuid();
		String currentDate = DateUtils.getStringDate(new Date());
		StringBuilder relativeFilePath = new StringBuilder();
		relativeFilePath.append("/");
		relativeFilePath.append(prefix);
		relativeFilePath.append("/");
		relativeFilePath.append(currentDate);
		relativeFilePath.append("/");
		relativeFilePath.append(fileName);
		relativeFilePath.append(".");
		relativeFilePath.append(suffix);

		return relativeFilePath.toString();
	}
}
