package com.example.demo.dto;

import lombok.Data;

@Data
public class Callback {

    private String api_order_id;

    private int mch_id;

    private String order_id;

    private String pay_type;

    private String success_at;

    private int total_amount;

    private String sign;
}
