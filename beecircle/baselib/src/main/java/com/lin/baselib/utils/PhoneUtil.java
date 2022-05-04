package com.lin.baselib.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class PhoneUtil {
    public static void callPhone(Context context,String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }
}
