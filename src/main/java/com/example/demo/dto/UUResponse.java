package com.example.demo.dto;

import lombok.Data;

@Data
public class UUResponse {
    private int code;
    private String msg;
    private long timestamp;
    private Object data;
}
