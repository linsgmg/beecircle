package com.lin.baselib.utils;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by zixing
 * Date 2018/7/4.
 * desc ：
 */

public class WebViewInitUtils {
    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    public static void init(WebView webview) {
        webview.setHorizontalScrollBarEnabled(false);//水平不显示
        webview.setVerticalScrollBarEnabled(false); //垂直不显示
        webview.setBackgroundColor(0); // 设置背景色(透明）


        //如果除了加载html的haunted，只需要用webviewclient即可，但是在进行互联网兼容附加js的页面的时候和调用js对话框的时候
        // ，或者功能较为复杂的内嵌操作的时候，建议使用webchromeclient
        webview.setWebViewClient(new MyClient());
        webview.setWebChromeClient(new WebChromeClient());

        // 默认缓存模式
        WebSettings settings = webview.getSettings();

        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        settings.setJavaScriptEnabled(true); // 设置支持javaScript
        settings.setSaveFormData(true);
        settings.setSavePassword(false); // 不保存密码

        settings.setLoadsImagesAutomatically(true);
        settings.setSupportMultipleWindows(true);
        settings.setLightTouchEnabled(true);

//        settings.setAllowFileAccess(true);
//        settings.setDomStorageEnabled(true);
//        settings.setBlockNetworkLoads(false);

        settings.setBuiltInZoomControls(false); // 不支持页面放大

        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); // 滚动条
        settings.setDefaultTextEncodingName("utf-8"); // 非常关键，否则设置了WebChromeClient后会出现乱码

        settings.setUseWideViewPort(false);
        settings.setLoadWithOverviewMode(true);

        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        settings.setJavaScriptCanOpenWindowsAutomatically(true);

    }

    /**
     * 初始化webView
     *
     * @param htmlbody
     */
    public static void initWebView(WebView webView,String htmlbody) {
        String htmlData = WebViewInitUtils.getHtmlData(htmlbody);
       webView.loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null);
    }

//    p {color:#fff;} a {color:#fff;} *{color:#fff;}  设置字体颜色
    public static String getHtmlData (String bodyHTML){
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=2.0, user-scalable=yes\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}  </style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    /**
     * Android使用Webview显示前端页面正常，跳到微信支付时会白屏。部分手机能正常支付，部分会白屏
     *
     * 原因是WebViewClent的onReceivedSslError()方法只能读Android认证过的https合法证书，因此不能继承父类的onReceivedSslError()方法，需要重写或调用sslErrorHandler.proceed();
     */
    public static class MyClient extends WebViewClient{
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }
    }

}
