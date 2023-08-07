package com.example.demo.util;

import com.alibaba.fastjson.JSON;
import com.example.demo.util.interfacepage.ICallback;
import okhttp3.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {

    private static volatile OkHttpClient okHttpClient = null;
    private static volatile Semaphore semaphore = null;
    private Map<String, String> headerMap;
    private Map<String, Object> paramMap;
    private String url;
    private Request.Builder request;

    private OkHttpUtil() {
        if (Objects.isNull(okHttpClient)) {
            synchronized (OkHttpUtil.class) {
                if (Objects.isNull(okHttpClient)) {
                    TrustManager[] trustManagers = buildTrustManager();
                    okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .sslSocketFactory(createSSLSocketFactory(trustManagers), (X509TrustManager)trustManagers[0])
                            .hostnameVerifier((hostname, session) -> true)
                            .retryOnConnectionFailure(true)
                            .build();
                    addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36");
                }
            }
        }
    }

    public OkHttpUtil initGet() {
        request = new Request.Builder().get();
        StringBuilder builder = new StringBuilder(url);
        if (Objects.nonNull(paramMap)) {
            builder.append("?");
            paramMap.forEach((key, value) -> {
                try {
                    builder.append(URLEncoder.encode(key, "utf-8"))
                            .append("=")
                            .append(URLEncoder.encode(value.toString(), "utf-8"))
                            .append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
            builder.deleteCharAt(builder.length() - 1);
        }
        request.url(builder.toString());
        return this;
    }

    public OkHttpUtil initPost(boolean isJson) {
        RequestBody requestBody = null;
        if (isJson) {
            String json = "";
            if (Objects.nonNull(paramMap)) {
                json = JSON.toJSONString(paramMap);
            }
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        } else {
            FormBody.Builder formBody = new FormBody.Builder();
            if (Objects.nonNull(paramMap)) {
                paramMap.forEach((x, y) -> formBody.add(x, (String) y));
            }
            requestBody = formBody.build();
        }
        request = new Request.Builder().post(requestBody).url(url);
        return this;
    }

    /**
     * @Description:同步请求
     * @Author: zzc
     * @Date: 2022-12-04 18:06

     * @return: java.lang.String
     **/
    public String sync() {
        setHeader(request);
        try {
            Response result = okHttpClient.newCall(request.build()).execute();
            if (Objects.nonNull(result)) {
                return result.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "请求失败";
    }

    /**
     * @Description:异步请求，有返回值
     * @Author: zzc
     * @Date: 2022-12-04 18:05
     * @return: java.lang.String
     **/
    public String async() {
        StringBuffer buffer = new StringBuffer();
        setHeader(request);
        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                buffer.append("请求出错").append(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (Objects.nonNull(response.body())) {
                    buffer.append(response.body().string());
                    getSemaphore().release();
                }
            }
        });
        try {
            getSemaphore().acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * @Description:异步请求，带接口回调
     * @Author: zzc
     * @Date: 2022-12-04 18:13
     * @param callback:
     * @return: void
     **/
    public void async(ICallback callback) {
        setHeader(request);
        okHttpClient.newCall(request.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFail(call, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (Objects.nonNull(response.body())) {
                    callback.onSuccess(call, response.body().string());
                }
            }
        });

    }

    private static Semaphore getSemaphore() {
        synchronized (OkHttpUtil.class) {
            if (Objects.isNull(semaphore)) {
                semaphore = new Semaphore(0);
            }
        }
        return semaphore;
    }

    public static OkHttpUtil builder() {
        return new OkHttpUtil();
    }

    public OkHttpUtil url(String url) {
        this.url = url;
        return this;
    }

    public OkHttpUtil addParam(String key, Object value) {
        if (Objects.isNull(paramMap)) {
            paramMap = new LinkedHashMap<>(16);
        }
        paramMap.put(key, value);
        return this;
    }

    public void setHeader(Request.Builder request) {
        if (Objects.nonNull(headerMap)) {
            headerMap.forEach(request::addHeader);
        }
    }

    public OkHttpUtil addHeader(String key, String value) {
        if (Objects.isNull(headerMap)) {
            headerMap = new LinkedHashMap<>(16);
        }
        headerMap.put(key, value);
        return this;
    }

    /**
     * @Description:生成安全套接字工厂，用于Https请求的证书跳过
     * @Author: zzc
     * @Date: 2022-11-30 16:03
     * @param trustManagers:
     * @return: javax.net.ssl.SSLSocketFactory
     **/
    private static SSLSocketFactory createSSLSocketFactory(TrustManager[] trustManagers) {
        SSLSocketFactory sslSocketFactory = null;
        try {
            SSLContext ssl = SSLContext.getInstance("SSL");
            ssl.init(null, trustManagers, new SecureRandom());
            sslSocketFactory = ssl.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
    }

    private static TrustManager[] buildTrustManager() {
        return new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };
    }

}
