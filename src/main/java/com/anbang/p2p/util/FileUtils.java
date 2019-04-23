package com.anbang.p2p.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PushbackInputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.anbang.p2p.util.bean.FileEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 从request中提取上传的文件列表
     *
     * @param request HttpServletRequest
     */
    public static List<FileEntity> getFilesFromRequest(HttpServletRequest request) {
        List<FileEntity> files = new ArrayList<FileEntity>();

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        try {
            for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
                InputStream inputstream = entity.getValue().getInputStream();
                if (!(inputstream.markSupported())) {
                    inputstream = new PushbackInputStream(inputstream, 8);
                }

                String fileName = entity.getValue().getOriginalFilename();
                String prefix =
                        fileName.lastIndexOf(".") >= 1 ? fileName.substring(fileName.lastIndexOf(".") + 1)
                                : null;
                FileEntity file = new FileEntity();
                file.setInputStream(inputstream);
                file.setFileType(prefix);
                file.setFileName(fileName);
                files.add(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    /**
     * 下载文件
     *
     * @param response
     * @param entity 的filePath 需要绝对全路径
     */
    public static void setDownloadResponse(HttpServletResponse response, FileEntity entity)
            throws IOException {
        if (entity == null || response == null) {
            response.sendError(404, "File not found!");
            return;
        }

        File f = new File(entity.getFilePath());
        if (!f.exists()) {
            response.sendError(404, "File not found!");
            return;
        }

        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len = 0;

        response.reset(); // 非常重要
        /*
         * if (isOnLine) { // 在线打开方式 URL u = new URL("file:///" + filePath);
         * response.setContentType(u.openConnection().getContentType());
         * response.setHeader("Content-Disposition", "inline; filename=" + f.getName()); //
         * 文件名应该编码成UTF-8 } else { // 纯下载方式
         */
        response.setContentType("application/x-msdownload");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename="
                        + URLEncoder.encode(entity.getFileName(), "utf-8").replace("+", "%20"));
        // }
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        br.close();
        out.close();
    }


    /**
     * 保存文件
     *
     * @param path 路径
     * @param content 内容
     */
    public static void write(String path, String content) {
        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(path), "UTF-8");
            // out.write("\n"+content);
            out.write(content);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     *
     * @param path 路径
     */
    public static void delete(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        logger.info("delete file:" + path);
    }

    /**
     * 删除某路径下所有文件
     *
     * @param path 路径
     */
    public static void deleteAllFiles(String path) {
        File dirFile = new File(path);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return;
        }
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                delete(files[i].getAbsolutePath());
            }
        }
    }

    /**
     * 拷贝文件
     */
    public static void copyFile(String fromPath, String destPath) {
        File oldfile = new File(fromPath);
        File newfile = new File(destPath);
        if (!oldfile.exists()) {
            throw new RuntimeException("文件不存在!");
        }
        if (newfile.exists()) {
            newfile.delete();
        } else if (!newfile.getParentFile().exists()) {
            newfile.getParentFile().mkdirs();
        }

        // int bytesum = 0;
        int byteread = 0;
        try { // 文件存在时
            InputStream inStream = new FileInputStream(fromPath); // 读入原文件
            FileOutputStream fs = new FileOutputStream(destPath);
            byte[] buffer = new byte[1024];
            while ((byteread = inStream.read(buffer)) != -1) {
                // bytesum += byteread; // 字节数 文件大小
                fs.write(buffer, 0, byteread);
            }
            fs.close();
            inStream.close();
        } catch (Exception e) {
            throw new RuntimeException("复制单个文件操作出错!");
        }
    }

}