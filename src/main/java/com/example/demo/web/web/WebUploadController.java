package com.example.demo.web.web;


import io.swagger.annotations.Api;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/web/upload")
@Api(value = "图片上传", tags = {"图片上传"})
public class WebUploadController {

    @Value("${upload.url}")
    private String url;


    /**
     * 图片上传
     * @param file
     * @return
     */
    @RequestMapping("/uploadImg")
    public String upload2(MultipartFile file) {
        String result = "";
        try {
            OkHttpClient httpClient = new OkHttpClient();
            MultipartBody multipartBody = new MultipartBody.Builder().
                    setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getOriginalFilename(),
                            RequestBody.create(MediaType.parse("multipart/form-data;charset=utf-8"),
                                    file.getBytes()))
                    .addFormDataPart("output", "json")
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(multipartBody)
                    .build();

            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body != null) {
                    result = body.string();
                    System.out.println(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
