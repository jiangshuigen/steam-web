package com.example.demo.dto;


import lombok.Data;

@Data
public class MessageResponse {
    private String status;
    private String send_id;
    private String fee;
}
