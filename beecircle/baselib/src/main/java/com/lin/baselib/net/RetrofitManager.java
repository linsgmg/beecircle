package com.lin.baselib.net;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.lin.baselib.BuildConfig;
import com.lin.baselib.utils.SSLContextUtil;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Description: <RetrofitManager><br>
 * Author:      mxdl<br>
 * Date:        2019/6/22<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
public class RetrofitManager {
    public static RetrofitManager retrofitManager;
    public static Context mContext;
    public Retrofit mRetrofit;
    private OkHttpClient.Builder okHttpBuilder;
    private String mToken;

    private final Gson mGson = new GsonBuilder()
            .setLenient()  // 设置GSON的非严格模式setLenient()
            .create();

    private RetrofitManager() {

        okHttpBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//这里可以选择拦截级别

            //设置 Debug Log 模式
            okHttpBuilder.addInterceptor(loggingInterceptor);
        }
        okHttpBuilder.connectTimeout(20 * 1000, TimeUnit.MILLISECONDS).
                readTimeout(20 * 1000, TimeUnit.MILLISECONDS).
                writeTimeout(20 * 1000, TimeUnit.MILLISECONDS);

//        okHttpBuilder.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//
//                if (!TextUtils.isEmpty(mToken)) {
////                    request = request.newBuilder()
////                            .header("Authorization", "Bearer " + mToken)
////                            .build();
//
//                    request = request.newBuilder().url(
//                            request.url().newBuilder()
//                                    .addQueryParameter("access_token", mToken)
//                                    .build()
//                    ).build();
//                }
//                return chain.proceed(request);
//            }
//        });

        //给client的builder添加了一个socketFactory
        SSLContext sslContext = SSLContextUtil.getDefaultSLLContext();
        if (sslContext != null) {
            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
//            Android 10调用不了这个方法
//            okHttpBuilder.sslSocketFactory(socketFactory);
            try {
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init((KeyStore) null);
                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                    throw new IllegalStateException("Unexpected default trust managers:"
                            + Arrays.toString(trustManagers));
                }
                X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
                okHttpBuilder.sslSocketFactory(socketFactory, trustManager);
            } catch (NoSuchAlgorithmException | KeyStoreException e) {
                e.printStackTrace();
            }
        }
        okHttpBuilder.hostnameVerifier(SSLContextUtil.HOSTNAME_VERIFIER);

        //创建client
        OkHttpClient okHttpClient = okHttpBuilder
                .addInterceptor(new TokenInterceptor())
                .addInterceptor(new LoggingInterceptor.Builder()
                        .setLevel(Level.BODY)
                        .log(Platform.INFO)
                        .request("request")
                        .response("response")
                        .build())
                .build();
        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(API.URL_HOST_USER)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonDConverterFactory.create())
//                .addConverterFactory(FastJsonConverterFactory.create())
                .build();

    }

    public static void init(Application application) {
        mContext = application;
    }

    public static RetrofitManager getInstance() {
        if (retrofitManager == null) {
            synchronized (RetrofitManager.class) {
                if (retrofitManager == null) {
                    retrofitManager = new RetrofitManager();
                }
            }
        }
        return retrofitManager;
    }


    public void addToken(String token) {
        mToken = token;
    }
}