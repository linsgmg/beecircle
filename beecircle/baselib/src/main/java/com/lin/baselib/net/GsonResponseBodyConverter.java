package com.lin.baselib.net;

import android.util.Log;

import com.google.gson.Gson;
import com.lin.baselib.Base.BaseResponse;
import com.lin.baselib.exception.NetErrorException;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody,
        T> {
    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    /**
     * 针对数据返回成功、错误不同类型字段处理
     */
    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            // 这里的type实际类型是 LoginUserEntity<User>  User就是user字段的对象。
            BaseResponse result = gson.fromJson(response, BaseResponse.class);
            if (result.getCode() == 200) {
                return gson.fromJson(response, type);
            } else if (result.getCode() == 210 || result.getCode() == 212 ||result.getCode() == 214||result.getCode() == 215) {
                Log.d("HttpManager", "err==：" + response);
                BaseResponse errResponse = gson.fromJson(response, BaseResponse.class);
                throw new NetErrorException(errResponse.getMsg(), result.getCode());
            }  else {
                Log.d("HttpManager", "err==：" + response);
                BaseResponse errResponse = gson.fromJson(response, BaseResponse.class);
                throw new NetErrorException(errResponse.getMsg(), NetErrorException.OTHER);
            }
        } finally {
            value.close();
        }
    }
}
