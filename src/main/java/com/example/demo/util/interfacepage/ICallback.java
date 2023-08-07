package com.example.demo.util.interfacepage;

import okhttp3.Call;

public interface ICallback {
    void onSuccess(Call call, String data);
    void onFail(Call call, String errorMsg);
}

