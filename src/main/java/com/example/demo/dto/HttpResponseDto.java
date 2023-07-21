package com.example.demo.dto;

import lombok.Data;

@Data
public class HttpResponseDto {
    private int  code;
    private String msg;
    private String obj;
}
