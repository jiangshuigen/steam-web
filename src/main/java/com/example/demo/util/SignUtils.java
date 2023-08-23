package com.example.demo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SignUtils {

    /**
     * 参数拼接 如body=大乐透2.1&buyer_id=2088102116773037&charset=utf-8
     */
    public static String getPreSignContent(Map<String, String> params) {
        if (params == null) {
            return null;
        } else {
            StringBuilder content = new StringBuilder();
            List<String> keys = new ArrayList(params.keySet());
            for (int i = 0; i < keys.size(); ++i) {
                String key = (String) keys.get(i);
                String value = (String) params.get(key);
                content.append(i == 0 ? "" : "&").append(key).append("=").append(value);
            }

            return content.toString();
        }
    }
}
