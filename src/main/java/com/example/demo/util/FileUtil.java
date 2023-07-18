package com.example.demo.util;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUtil {
    public static String uploadFile(MultipartFile imgfile, HttpServletRequest request) {
        //1、上传路径：项目发布tomcat服务器
        //D:\workUtilsInstall\apache-tomcat-8.0.0\webapps\week_employee_hzy\\upload
        String path = request.getServletContext().getRealPath("/") + "/upload";
        File file = new File(path);
        if (!file.exists()) {//不存在
            file.mkdirs();
        }

        //生成新的文件名称，原因：防止文件名称一样后者上传的文件会覆盖前者上传的文件（前提是文件名称必须一样并且在用一个目录下）
        //生成新的文件名称，保证文件名称唯一有两种方法：
        // 	  1.通过UUID实现文件名称唯一 （UUID会生成32位字母+数字唯一的一个字符串）
        //	  2.通过时间戳现文件名称唯一  （时间戳是毫秒级时间 时间会一直往上加，生成13位数字）注意只有java生成13位 其他则是10位比如oracle、mysql、php
        //  获取时间戳
        //long currentTimeMillis = System.currentTimeMillis();
        //System.out.println(currentTimeMillis);
        String uuid = UUID.randomUUID().toString();
        String oldName = imgfile.getOriginalFilename();//1.jpg
        String suffix = oldName.substring(oldName.lastIndexOf("."));
        String newFile = uuid + suffix;
        File file2 = new File(path + "\\" + newFile);
        try {
            imgfile.transferTo(file2);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "upload/" + newFile;
    }

}
