package com.anbang.p2p.util.bean;

import java.io.InputStream;

/**
 * @Description:
 */
public class FileEntity {
    private InputStream inputStream;
    private String fileType;
    private String fileName;
    private String filePath = "/data/files/excel";


    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
