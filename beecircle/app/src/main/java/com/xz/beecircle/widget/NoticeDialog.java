package com.xz.beecircle.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.lin.baselib.net.API;
import com.lin.baselib.utils.Arith;
import com.lin.baselib.utils.PicUtils;
import com.lin.baselib.utils.WebViewInitUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.xz.beecircle.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * description:自定义dialog
 */
public class NoticeDialog extends Dialog {

    public NoticeDialog(Context context) {
        super(context, R.style.CustomDialog);
        setCancelable(false);
    }

    /**
     * 内容文本
     */
//    private WebView webView;
//    private TextView tv_title;
//    private TextView tv_confirm;

    private ImageView ivAd;
    private ImageView ivClose;
    private WebView webView;
    private VideoView vvVideo;
    private TextView tv_time;

    /**
     * 内容数据
     */
    private String htmlBody;
    private String title;
    private String url;
    private String type;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_notice);
        //按空白处不能取消动画
//        setCanceledOnTouchOutside(true);
        //去除状态栏变黑问题
        Window window = getWindow();
        window.setDimAmount(0.3f);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.height = QMUIDisplayHelper.getScreenHeight(getContext())- QMUIDisplayHelper.getStatusBarHeight(getContext());

        //初始化界面控件
        initView();
        //初始化界面数据
        refreshView();
        //初始化界面控件的事件
        initEvent();
    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        if (onClickBottomListener == null) {
            return;
        }
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBottomListener.onNegativeClick(v);
            }
        });
//
//        tv_confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClickBottomListener.onPositiveClick(v);
//            }
//        });

    }

    private CountDownTimer timer;

    private void setTime(Long time) {
        /**
         * CountDownTimer timer = new CountDownTimer(3000, 1000)中，
         * 第一个参数表示总时间，第二个参数表示间隔时间。
         * 意思就是每隔一秒会回调一次方法onTick，然后1秒之后会回调onFinish方法。
         */
        timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
              tv_time.setText((millisUntilFinished / 1000) + "秒");
            }

            @Override
            public void onFinish() {
                tv_time.setText("");
                ivClose.setVisibility(View.VISIBLE);
            }
        };
        //调用 CountDownTimer 对象的 start() 方法开始倒计时，也不涉及到线程处理
        timer.start();
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * 初始化webView
     *
     * @param htmlbody
     */
    private void initWebView(String htmlbody) {
        String htmlData = WebViewInitUtils.getHtmlData(htmlbody);
        webView.loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null);
    }

    /**
     * 初始化webView
     */
    private void initWebView() {
        WebViewInitUtils.init(webView);
        //设置客户端-不跳转到默认浏览器中
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url == null) return false;
                if (url.startsWith("http:") || url.startsWith("https:") ){
                    view.loadUrl(url);
                    return false;
                }else{
                    try{
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        getContext().startActivity(intent);
                    }catch (Exception e){
                        //ToastUtils.showShort("暂无应用打开此链接");
                    }
                    return true;
                }
            }
        });
        //监听手机返回按键：
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                        //表示按返回键时的操作
                        webView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
        //访问网页
        webView.loadUrl(url);
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void refreshView() {

        if (!TextUtils.isEmpty(type)) {
            setTime(Long.parseLong(Arith.mul(type, "1000")));
        }

        if (!TextUtils.isEmpty(url)) {
            initWebView();
        }


//        if (!url.startsWith("http")) {
//            url = API.URL_HOST_USER_IMAGE + url;
//        }
//        if (type.equals("png")) {
//            Glide.with(getContext()).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                    int width = resource.getWidth();
//                    int height = resource.getHeight();
//                    Log.d("TAG", "width " + width); //200px
//                    Log.d("TAG", "height " + height); //200px
//                    int screenWidth = QMUIDisplayHelper.getScreenWidth(getContext()) - QMUIDisplayHelper.dp2px(getContext(), 40);
//                    int screenHeight = height * screenWidth / width;
//                    ViewGroup.LayoutParams layoutParams = ivAd.getLayoutParams();
//                    layoutParams.height = screenHeight;
//                    ivAd.setLayoutParams(layoutParams);
//                    vvVideo.setVisibility(View.GONE);
//                    if (!TextUtils.isEmpty(url)) {
//                        PicUtils.uploadPic(getContext(), url, ivAd);
//                    }
//                }
//            });
//        } else {
//            Bitmap bitmap = this.bitmap;
//            int width = bitmap.getWidth();
//            int height = bitmap.getHeight();
//            int screenWidth = QMUIDisplayHelper.getScreenWidth(getContext()) - QMUIDisplayHelper.dp2px(getContext(), 40);
//            int screenHeight = height * screenWidth / width;
//            ViewGroup.LayoutParams layoutParams = ivAd.getLayoutParams();
//            layoutParams.height = screenHeight;
//            ivAd.setLayoutParams(layoutParams);
//            vvVideo.setVideoPath(url);
//            //开始播放
//            vvVideo.start();
//        }
    }

//    public static Bitmap getVideoThumb(String path) {
//        Bitmap bitmap = null;
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        try {
//            //根据url获取缩略图
//            retriever.setDataSource(path, new HashMap());
//            //获得第一帧图片
//            bitmap = retriever.getFrameAtTime();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } finally {
//            retriever.release();
//        }
//        return bitmap;
//    }

    @Override
    public void show() {
        super.show();
        refreshView();
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        ivAd = (ImageView) findViewById(R.id.iv_ad);
        ivClose = (ImageView) findViewById(R.id.iv_close);
        vvVideo = (VideoView) findViewById(R.id.vv_ad);
        webView = (WebView) findViewById(R.id.webView);
        tv_time = (TextView) findViewById(R.id.tv_time);
//        webView = (WebView) findViewById(R.id.webView);
//        webView.setHorizontalScrollBarEnabled(false);//水平不显示
//        webView.setVerticalScrollBarEnabled(false); //垂直不显示
//        webView.setBackgroundColor(0); // 设置背景色

    }


    /**
     * 设置确定取消按钮的回调
     */
    public OnClickBottomListener onClickBottomListener;

    public NoticeDialog setOnClickBottomListener(OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }

    public interface OnClickBottomListener {
        /**
         * 点击negative按钮事件
         */
        public void onNegativeClick(View view);

        /**
         * 点击positive按钮事件
         */
        public void onPositiveClick(View view);

    }

    public NoticeDialog setUrl(String url) {
        this.url = url;
        return this;
    }

    public NoticeDialog setType(String type) {
        this.type = type;
        return this;
    }

    public NoticeDialog setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        return this;
    }

    @SuppressLint("NewApi")
    public static String getPathFromUri(final Context context, final Uri uri) {
        if (uri == null) {
            return null;
        }
        // 判斷是否為Android 4.4之後的版本
        final boolean after44 = Build.VERSION.SDK_INT >= 19;
        if (after44 && DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是Android 4.4之後的版本，而且屬於文件URI
            final String authority = uri.getAuthority();
            // 判斷Authority是否為本地端檔案所使用的
            if ("com.android.externalstorage.documents".equals(authority)) {
                // 外部儲存空間
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] divide = docId.split(":");
                final String type = divide[0];
                if ("primary".equals(type)) {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/").concat(divide[1]);
                    return path;
                } else {
                    String path = "/storage/".concat(type).concat("/").concat(divide[1]);
                    return path;
                }
            } else if ("com.android.providers.downloads.documents".equals(authority)) {
                // 下載目錄
                final String docId = DocumentsContract.getDocumentId(uri);
                if (docId.startsWith("raw:")) {
                    final String path = docId.replaceFirst("raw:", "");
                    return path;
                }
                final Uri downloadUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(docId));
                String path = queryAbsolutePath(context, downloadUri);
                return path;
            } else if ("com.android.providers.media.documents".equals(authority)) {
                // 圖片、影音檔案
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] divide = docId.split(":");
                final String type = divide[0];
                Uri mediaUri = null;
                if ("image".equals(type)) {
                    mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    mediaUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else {
                    return null;
                }
                mediaUri = ContentUris.withAppendedId(mediaUri, Long.parseLong(divide[1]));
                String path = queryAbsolutePath(context, mediaUri);
                return path;
            }
        } else {
            // 如果是一般的URI
            final String scheme = uri.getScheme();
            String path = null;
            if ("content".equals(scheme)) {
                // 內容URI
                path = queryAbsolutePath(context, uri);
            } else if ("file".equals(scheme)) {
                // 檔案URI
                path = uri.getPath();
            }
            return path;
        }
        return null;
    }

    public static String queryAbsolutePath(final Context context, final Uri uri) {
        final String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                return cursor.getString(index);
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }


}

