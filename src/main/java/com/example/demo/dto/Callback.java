package com.example.demo.dto;

import lombok.Data;

@Data
public class Callback {

    private int pid;

    private String trade_no;

    private String out_trade_no;

    private String type;

    private String name;

    private String money;

    private String trade_status;

    private String sign;

    private String sign_type;
}
