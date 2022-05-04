package com.lin.baselib.net;


import android.text.TextUtils;
import android.util.Log;

import com.lin.baselib.utils.AppUtils;
import com.lin.baselib.utils.SharedPreferencesUtils;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request.newBuilder().header("Content-Type", "application/json;charset=UTF-8").build();
        String token = SharedPreferencesUtils.getString(AppUtils.getApp(), "token", "");
        if (!TextUtils.isEmpty(token) && request.method().equals("POST")) {
//            FormBody.Builder bodyBuilder = new FormBody.Builder();
//            FormBody formBody = (FormBody) request.body();
//            //把原来的参数添加到新的构造器，（因为没找到直接添加，所以就new新的）
//            for (int i = 0; i < formBody.size(); i++) {
//                if (!formBody.encodedName(i).equals("uid")) {
//                    bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
//                }
//            }
//            formBody = bodyBuilder
//                    .addEncoded("uid", SharedPreferencesUtils.getString(AppUtils.getApp(), "uid", ""))
//                    .build();
            request = request.newBuilder()
                    .addHeader("token", token)
//                    .post(formBody)
                    .build();
        }
        Log.d("token", "intercept: " + token);
        return chain.proceed(request);
    }
}