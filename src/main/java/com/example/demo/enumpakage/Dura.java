package com.example.demo.enumpakage;

public enum Dura {
    DURA_0(0, "无磨损"),
    DURA_1(1, "崭新出厂"),
    DURA_2(2, "略有磨损"),
    DURA_3(3, "久经沙场"),
    DURA_4(4, "战痕累累"),
    DURA_5(5, "破损不堪");

    Dura(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    private int code;
    private String msg;
}
