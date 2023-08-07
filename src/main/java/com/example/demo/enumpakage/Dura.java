package com.example.demo.enumpakage;

public enum Dura {
    DURA_0("0", "无磨损"),
    DURA_1("1", "崭新出厂"),
    DURA_2("2", "略有磨损"),
    DURA_3("3", "久经沙场"),
    DURA_4("4", "战痕累累"),
    DURA_5("5", "破损不堪");

    private Dura(String value, String msg) {
        this.val = value;
        this.msg = msg;
    }

    public String val() {
        return val;
    }

    public String msg() {
        return msg;
    }

    private String val;
    private String msg;
}
