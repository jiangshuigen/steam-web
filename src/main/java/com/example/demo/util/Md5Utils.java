package com.example.demo.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5加密
 *
 * @author dongmd
 * @date 2022/6/30
 */
public class Md5Utils {

    /**
     * MD5方法
     *
     * @param text 明文
     * @param key  密钥
     * @return 密文
     * @throws Exception
     */
    public static String md5(String text, String key) throws Exception {
        //加密后的字符串
        String encodeStr = DigestUtils.md5Hex(text + key);
        return encodeStr;
    }

    /**
     * MD5方法，返回值转为小写
     *
     * @param text 明文
     * @return 密文
     * @throws Exception
     */
    public static String md5(String text) throws Exception {
        //加密后的字符串
        String encodeStr = DigestUtils.md5Hex(text);
        return encodeStr.toLowerCase();
    }

    /**
     * MD5验证方法
     *
     * @param text 明文
     * @param key  密钥
     * @param md5  密文
     * @return true/false
     * @throws Exception
     */
    public static boolean verify(String text, String key, String md5) throws Exception {
        //根据传入的密钥进行验证
        String md5Text = md5(text, key);
        if (md5Text.equalsIgnoreCase(md5)) {
            return true;
        }

        return false;
    }

    /**
     * sha1加密
     *
     * @param: str 需要加密的字符串
     * @author dongmd
     * @date 2022/6/10
     */
    public static String SHA1(String str) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1"); //如果是SHA加密只需要将"SHA-1"改成"SHA"即可
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexStr = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexStr.append(0);
                }
                hexStr.append(shaHex);
            }
            return hexStr.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        }
        return null;
    }

    // 测试
    public static void main(String[] args) {

        try {
            String b = SHA1("sdfdsfd");
            System.out.println(b);
            String privateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAgadgsaefGqPs9Hj3kg0gxAT2KbBZlv78Zky1/ZiGXGsBmZXt9RxuZtCvIUYombKQ1oPxKuWCMS3kgACuwoTFrwIDAQABAkBqLlGaLNS4VJeyf38BNY8n+YhADBTS/HBot1o0Hb2r4SwqfYoU6Vjf4Hl4EHIkEwDrfz8VqPIS/Q4I+9rbUXsBAiEAulRAH9RJumnB74N3NMS4BOtiux5YI6mh+ZPLpre4IL8CIQCyIhG/ilAPykNmUymuU0GShuN/cOglzvGaYZI7e5KnEQIgIxY8YQIDxDOoDmcuKmSk1mBQ3jWPmV+XHb1ECyfrZ1cCICecNdAexHDObDBbu7/82W7oubvD8os9ujG3EppRfynRAiEAqanwhITOh/bNuDYXNK9YxLb/IMTrH04JX4mQFQIvYUA=publicKey:MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIGnYLGnnxqj7PR495INIMQE9imwWZb+/GZMtf2YhlxrAZmV7fUcbmbQryFGKJmykNaD8SrlgjEt5IAArsKExa8CAwEAAQ==";
            String testString = "app_id=2021000121616033&buyer_id=2088622987228082&buyer_pay_amount=1.00&gmt_payment=2022-07-18 16:31:33&out_trade_no=T202207181631082086650032&seller_id=2088621987522912&total_amount=1.00&trade_no=2022071822001428080501646082&trade_status=TRADE_SUCCESS";
            String ltl = md5(testString, "2022-07-18 16:31:33");
            System.out.println("ltl : " + ltl);
            System.out.println(verify(privateKey, privateKey, ltl));
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
        }
    }


}