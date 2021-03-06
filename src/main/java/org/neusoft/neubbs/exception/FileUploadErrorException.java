package org.neusoft.neubbs.exception;

import org.neusoft.neubbs.controller.annotation.ApiException;

/**
 * 文件上传错误异常
 *
 * @author Suvan
 */
@ApiException
public class FileUploadErrorException extends RuntimeException implements IPrintLog {

    private String logMessage;

    /**
     * Constructor
     */
    public FileUploadErrorException(String message) {
        super(message);
    }

    @Override
    public FileUploadErrorException log(String logMessage) {
        this.logMessage = logMessage;
        return this;
    }

    @Override
    public String getLogMessage() {
        return logMessage;
    }
}
