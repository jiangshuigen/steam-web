package com.example.demo.util;

import java.util.Random;
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

    public static String getInviteCode() {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWYZ0123456789";
        Random sc = new Random();
        String code = "M8";
        for (int i = 0; i < 5; i++) {
            int ax = sc.nextInt(str.length());//随机一个索引
            char c = str.charAt(ax);//获取字符索引位置的值
            code += c;//累加
        }
        return code;
    }
}
