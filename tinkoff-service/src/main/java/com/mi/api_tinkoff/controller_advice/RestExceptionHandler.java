package com.mi.api_tinkoff.controller_advice;


import com.mi.api_tinkoff.controller_advice.exception.ExceptionDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.NoSuchElementException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = NoSuchElementException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionDetail> handleNoSuchElementException(WebRequest request, NoSuchElementException exception) {
        ExceptionDetail exceptionDetail = new ExceptionDetail();
        exceptionDetail.setDate(LocalDateTime.now(ZoneId.of("Europe/Moscow")));
        exceptionDetail.setTitle("Элемент не найден");
        exceptionDetail.setStatus(HttpStatus.NOT_FOUND.value());
        exceptionDetail.setDetail(exception.getMessage());
//        "javax.servlet.error.request_uri"
        exceptionDetail.setPath(request.getDescription(false));
        exceptionDetail.setDeveloperMessage(exception.getClass().getName());

        return new ResponseEntity<>(exceptionDetail, null, HttpStatus.NOT_FOUND);
    }

}
