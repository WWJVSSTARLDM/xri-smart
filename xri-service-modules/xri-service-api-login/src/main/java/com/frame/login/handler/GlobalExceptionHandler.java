package com.frame.login.handler;

import com.frame.common.enums.CodeMsgEnum;
import com.frame.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Component
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Result> globalExceptionHandler(Exception e, HttpServletRequest request) {
        log.error("系统异常:URL路径[{}]", request.getServletPath());
        e.printStackTrace();
        Result<Object> error = Result.error(CodeMsgEnum.SERVER_ERROR);
        return ResponseEntity.status(500).body(error);
    }
}
