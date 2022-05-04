package com.lin.baselib.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.lin.baselib.utils.NetworkUtils;

public class NetReceivers extends BroadcastReceiver {

    public NetReceivers() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            boolean isConnected = NetworkUtils.isNetworkConnected(context);
            System.out.println("网络状态：" + isConnected);
            System.out.println("wifi状态：" + NetworkUtils.isWifiConnected());
            System.out.println("移动网络状态：" + NetworkUtils.getDataEnabled());
            System.out.println("网络连接类型：" + NetworkUtils.getNetworkType());

        }
    }

}
