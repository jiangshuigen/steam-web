package com.example.demo.util;

import java.util.UUID;

public class CodeUtils {
    /**
     * 获取口令
     *
     * @return
     */
    public static String getCode() {
        //随机生成一个UUID字符串
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid.toUpperCase();
    }
}
