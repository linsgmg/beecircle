package com.lin.baselib.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lin.baselib.Base.BaseApplication;
import com.lin.baselib.R;
import com.lin.baselib.net.API;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhaoss.weixinrecorded.activity.RecordedActivity;

import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class PicUtils {

    public static void uploadPic(Context context, String url, ImageView view) {
        //TODO 暂时取消
        if (!url.startsWith("http")){
            url = API.URL_HOST_USER_IMAGE+url;
        }
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.touxiang_wode)
                .into(view);
    }

    public static void uploadVideo(Context context, String url, ImageView view) {
        //TODO 暂时取消
//        if (!url.startsWith("http")){
//            url = API.URL_HOST_USER_IMAGE+url;
//        }
        Glide.with(context).load(url).into(view);
    }

    public static void uploadGif(Context context, int url, ImageView view) {
        Glide.with(context).asGif().load(url).into(view);
    }

    public static void addPicAndShow(Context context, List<String> list, int position) {
        List<LocalMedia> selectList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            LocalMedia media = new LocalMedia(list.get(i), 0, PictureMimeType.ofImage(), "image/jpeg");
            selectList.add(media);
        }

        PictureSelector.create((Activity) context)
                .themeStyle(R.style.picture_default_style)
                .isNotPreviewDownload(true)
                .imageEngine(GlideEngine.createGlideEngine()) // 请参考Demo GlideEngine.java
                .openExternalPreview(position, selectList);
    }


    public static void addVideoAndShow(Activity context, String url) {
        PictureSelector.create(context).externalPictureVideo(url);
    }


    private static AlertDialog DIALOG;

    //选择图片
    public static void beginCameraDialog(Context context, int code, int maxCount) {
        DIALOG = new AlertDialog.Builder(context).create();
        DIALOG.show();
        final Window window = DIALOG.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_photo_picker);
            window.setGravity(Gravity.BOTTOM);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置属性
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            //弹出一个窗口，让背后的窗口变暗一点
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            //dialog背景层
            params.dimAmount = 0.5f;
            window.setAttributes(params);

            Button appCompatButton = window.findViewById(R.id.photodialog_btn_cancel);
            Button appCompatButton1 = window.findViewById(R.id.photodialog_btn_take);
            Button appCompatButton2 = window.findViewById(R.id.photodialog_btn_native);

            appCompatButton.setOnClickListener(v -> {
                DIALOG.cancel();
            });
            appCompatButton1.setOnClickListener(v -> {
                enterPicture(context, false, code, maxCount);
                DIALOG.cancel();
            });
            appCompatButton2.setOnClickListener(v -> {
                enterPicture(context, true, code, maxCount);
                DIALOG.cancel();
            });
        }
    }

    //选择图片
    public static void beginCameraDialog(Context context, int code, int maxCount, boolean isJustCamera) {
        DIALOG = new AlertDialog.Builder(context).create();
        DIALOG.show();
        final Window window = DIALOG.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_photo_picker);
            window.setGravity(Gravity.BOTTOM);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置属性
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            //弹出一个窗口，让背后的窗口变暗一点
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            //dialog背景层
            params.dimAmount = 0.5f;
            window.setAttributes(params);

            Button appCompatButton = window.findViewById(R.id.photodialog_btn_cancel);
            Button appCompatButton1 = window.findViewById(R.id.photodialog_btn_take);
            Button appCompatButton2 = window.findViewById(R.id.photodialog_btn_native);

            if (isJustCamera) {
                appCompatButton2.setVisibility(View.GONE);
            }

            appCompatButton.setOnClickListener(v -> {
                DIALOG.cancel();
            });
            appCompatButton1.setOnClickListener(v -> {
                enterPicture(context, false, code, maxCount);
                DIALOG.cancel();
            });
            appCompatButton2.setOnClickListener(v -> {
                enterPicture(context, true, code, maxCount);
                DIALOG.cancel();
            });
        }
    }

    private static void enterPicture(Context context, boolean openPic, int code, int maxCount) {
        if (openPic) {
            PictureSelector.create((Activity) context)
                    .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                    .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
                    .maxSelectNum(maxCount)// 最大图片选择数量 int
                    .minSelectNum(1)// 最小选择数量 int
                    .imageSpanCount(4)// 每行显示个数 int
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                    .isPreviewImage(true)// 是否可预览图片 true or false
                    .isPreviewVideo(true)// 是否可预览视频 true or false
                    .isCamera(false)// 是否显示拍照按钮 true or false
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
//                    .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                    .isEnableCrop(false)// 是否裁剪 true or false
                    .isCompress(true)// 是否压缩 true or false
                    .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                    .isGif(false)// 是否显示gif图片 true or false
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                    .circleDimmedLayer(false)// 是否圆形裁剪 true or false
//                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
//                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
//                .isOpenClickSound(true)// 是否开启点击声音 true or false
                    .isPreviewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                    .cutOutQuality(100)// 裁剪压缩质量 默认90 int
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
//                .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                    .forResult(code);//结果回调onActivityResult code
        } else {
            PictureSelector.create((Activity) context)
                    .openCamera(PictureMimeType.ofAll())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                    .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
                    .maxSelectNum(maxCount)// 最大图片选择数量 int
                    .minSelectNum(1)// 最小选择数量 int
                    .imageSpanCount(4)// 每行显示个数 int
                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                    .isPreviewImage(true)// 是否可预览图片 true or false
                    .isPreviewVideo(true)// 是否可预览视频 true or false
                    .isCamera(false)// 是否显示拍照按钮 true or false
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
//                    .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                    .isEnableCrop(false)// 是否裁剪 true or false
                    .isCompress(true)// 是否压缩 true or false
                    .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                    .isGif(false)// 是否显示gif图片 true or false
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                    .circleDimmedLayer(false)// 是否圆形裁剪 true or false
//                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
//                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
//                .isOpenClickSound(true)// 是否开启点击声音 true or false
                    .isPreviewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                    .cutOutQuality(100)// 裁剪压缩质量 默认90 int
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
//                .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                    .forResult(code);//结果回调onActivityResult code
        }
    }

    //选择图片
    public static void beginVideoDialog(Context context, int code, int maxCount) {
        DIALOG = new AlertDialog.Builder(context).create();
        DIALOG.show();
        final Window window = DIALOG.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_photo_picker);
            window.setGravity(Gravity.BOTTOM);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置属性
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            //弹出一个窗口，让背后的窗口变暗一点
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            //dialog背景层
            params.dimAmount = 0.5f;
            window.setAttributes(params);

            Button appCompatButton = window.findViewById(R.id.photodialog_btn_cancel);
            Button appCompatButton1 = window.findViewById(R.id.photodialog_btn_take);
            Button appCompatButton2 = window.findViewById(R.id.photodialog_btn_native);

            appCompatButton1.setText("录像");
            appCompatButton.setOnClickListener(v -> {
                DIALOG.cancel();
            });
            appCompatButton1.setOnClickListener(v -> {
                DIALOG.cancel();
                judgePermisson(context, 1, code);
            });
            appCompatButton2.setOnClickListener(v -> {
                DIALOG.cancel();
                judgePermisson(context, 2, code);
            });
        }
    }

    private static void judgePermisson(Context context, int type, int code) {
        if (XXPermissions.hasPermission(context, Manifest.permission.CAMERA, Manifest.permission.MANAGE_EXTERNAL_STORAGE)) {
            if (type == 1) {
                Intent intent = new Intent(context, RecordedActivity.class);
                ((Activity) context).startActivityForResult(intent, code);
            } else {
                enterVideo(context, code, 1);
            }
        } else {
            XXPermissions.with(context)
//                    .permission(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .permission(Manifest.permission.CAMERA)
                    // 适配 Android 11 需要这样写，这里无需再写 Permission.Group.STORAGE
                    .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                    .request(new OnPermission() {

                        @Override
                        public void hasPermission(List<String> granted, boolean all) {
                            if (all) {
                                ToastUtils.showShort("获取权限成功");
                                if (type == 1) {
                                    Intent intent = new Intent(context, RecordedActivity.class);
                                    ((Activity) context).startActivityForResult(intent, code);
                                } else {
                                    enterVideo(context, code, 1);
                                }
                            }
                        }

                        @Override
                        public void noPermission(List<String> denied, boolean never) {
                            if (never) {
                                ToastUtils.showShort("被永久拒绝授权，请手动授予存储权限");
                                // 如果是被永久拒绝就跳转到应用权限系统设置页面
                                XXPermissions.startPermissionActivity(context, denied);
                            } else {
                                ToastUtils.showShort("获取权限失败");
                            }
                        }
                    });
        }
    }


    private static void enterVideo(Context context, int code, int maxCount) {
        PictureSelector.create((Activity) context)
                .openGallery(PictureMimeType.ofVideo())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
                .maxSelectNum(maxCount)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .isPreviewImage(true)// 是否可预览图片 true or false
                .isPreviewVideo(true)// 是否可预览视频 true or false
                .isCamera(false)// 是否显示拍照按钮 true or false
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
//                    .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .isEnableCrop(false)// 是否裁剪 true or false
                .isCompress(true)// 是否压缩 true or false
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
//                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
//                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
//                .isOpenClickSound(true)// 是否开启点击声音 true or false
                .isPreviewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cutOutQuality(100)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
//                .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                .forResult(code);//结果回调onActivityResult code
    }
}
