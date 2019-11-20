package com.imooc.exception;

import com.imooc.utils.JSONResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * @author mcg
 * @create 2019-11-20-10:42
 */
@RestControllerAdvice
public class CustomExceptionHandler {


    // 上传文件超过 500k 捕获异常 MaxUploadSizeExceededException
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public JSONResult handlerMaxUploadFile(MaxUploadSizeExceededException ex) {
        return JSONResult.errorMsg("文件大小上传不能超过 500k,请压缩图片或者降低图片质量再上传");
    }
}
