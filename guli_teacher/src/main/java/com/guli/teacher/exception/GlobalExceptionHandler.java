package com.guli.teacher.exception;

import com.guli.common.result.ExceptionUtil;
import com.guli.common.result.Result;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //1.全局异常类型 Exception
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.error().message("出错了");
    }

    //自定义异常
    @ExceptionHandler(EduException.class)
    @ResponseBody
    public Result error(EduException e) {
        e.printStackTrace();
        log.error(ExceptionUtil.getMessage(e));
        return Result.error().code(e.getCode()).message(e.getMessage());
    }
}
