package com.file.controller;

import com.file.service.FileService;
import com.file.utils.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "file")
public class FileController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FileService fileService;

    @ResponseBody
    @RequestMapping(value = "upload")
    public String uploadFile(MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        String fileContentType = file.getContentType();
        String resourcesPath = request.getSession().getServletContext().getRealPath("/resources");

        if (fileContentType.equals("application/pdf")) {
            return fileService.uploadPDF(file, resourcesPath);
        } else if (fileContentType.equals("image")) {
            return fileService.uploadPicture(file, resourcesPath);
        } else if (fileContentType.equals("video/mp4")) {
            return fileService.uploadVideo(file, resourcesPath);
        } else if (fileContentType.equals("application/epub+zip")) {
            return fileService.uploadEpub(file, resourcesPath);
        }
        return JSONUtils.operateError("未支持该格式的文件");
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
}
