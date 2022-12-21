package com.mi.api_tinkoff.controller_advice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionDetail {

    private LocalDateTime date;
    private String title;
    private int status;
    private String detail;
    private String path;
    private String developerMessage;


}
