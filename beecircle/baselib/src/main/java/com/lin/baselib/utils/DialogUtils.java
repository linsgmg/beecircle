package com.lin.baselib.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.google.gson.Gson;
import com.lin.baselib.R;
import com.lin.baselib.bean.JsonBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.lin.baselib.utils.TimeUtils.calculateTime;

public class DialogUtils {

    public static Dialog getDialog(Context context, TextView v, String[] list) {
        AlertDialog dialog;
        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        final Window window = dialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_template);
            window.setGravity(Gravity.BOTTOM);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//??????????????????
            //????????????
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            //???????????????????????????????????????????????????
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            //dialog?????????
            params.dimAmount = 0.5f;
            window.setAttributes(params);

            LinearLayout ll_template = window.findViewById(R.id.ll_template);
            for (int i = 0; i < list.length; i++) {
                View inflate = LayoutInflater.from(context).inflate(R.layout.item_dialog, null, false);
                TextView textView = inflate.findViewById(R.id.tv_content);
                textView.setText(list[i]);
                ll_template.addView(inflate);
                int finalI = i;
                inflate.setOnClickListener(v1 -> {
                    v.setText(list[finalI]);
                    dialog.dismiss();
                });
            }
        }
        return dialog;
    }


    public static Dialog getDialog2(Context context, TextView v, String[] list, String[] ids, String title) {
        AlertDialog dialog;
        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        final Window window = dialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_wheelview);
            window.setGravity(Gravity.BOTTOM);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//??????????????????
            //????????????
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            //???????????????????????????????????????????????????
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            //dialog?????????
            params.dimAmount = 0.5f;
            window.setAttributes(params);

            WheelView wheelView = window.findViewById(R.id.wheelview);
            wheelView.setCyclic(false);

            final List<String> mOptionsItems = new ArrayList<>();
            for (int i = 0; i < list.length; i++) {
                mOptionsItems.add(list[i]);
            }

            TextView tvSubmit = (TextView) window.findViewById(R.id.tv_finish);
            TextView ivCancel = (TextView) window.findViewById(R.id.iv_cancel);
            TextView tvTitle = (TextView) window.findViewById(R.id.tv_title);
            tvTitle.setText(title);
            tvSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            ivCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            wheelView.setAdapter(new ArrayWheelAdapter(mOptionsItems));
            wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    v.setText(list[index]);
                    v.setTag(ids[index]);
//                    dialog.dismiss();
                }
            });
        }
        return dialog;
    }

    /**
     * ????????????????????????
     *
     * @param currentTime
     */
    public static TimePickerView initPickTime(Context context, String currentTime) {
        String[] split_start = currentTime.split("-");
        String endTime = calculateTime(currentTime, 1);
        String[] split_end = endTime.split("-");
        //??????????????????(?????????????????????????????????????????????1900-2100???????????????????????????)
        //????????????Calendar???????????????0-11???,?????????????????????Calendar???set?????????????????????,???????????????????????????0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(Integer.parseInt(split_start[0]), Integer.parseInt(split_start[1]) - 1, Integer.parseInt(split_start[2]));
        Calendar endDate = Calendar.getInstance();
        startDate.set(1980, 1, 1);
        endDate.set(2030, 11, 31);

        //???????????????
        TimePickerBuilder timePickerBuilder = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//??????????????????
                // ?????????????????????v,??????show()???????????????????????? View ???????????????show??????????????????????????????v??????null
                TextView btn = (TextView) v;
                btn.setText(TimeUtils.getDate(date));
            }
        });
        timePickerBuilder.setType(new boolean[]{true, true, true, false, false, false});
        timePickerBuilder.setLabel("???", "???", "???", "", "", "");
        timePickerBuilder.isCenterLabel(true);
        timePickerBuilder.setDate(selectedDate);
        timePickerBuilder.setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.parseColor("#04C7BE")) //???????????????????????????
                .setContentTextSize(20)
                .setSubmitColor(Color.parseColor("#04C7BE"))
                .setCancelColor(Color.parseColor("#818596"));
//        timePickerBuilder.setTitleBgColor(Color.parseColor("#FFFFFF"));
        timePickerBuilder.setRangDate(startDate, endDate);

        TimePickerView pvTime = timePickerBuilder.build();
        timePickerBuilder.setLayoutRes(R.layout.dialog_time, new CustomListener() {
            @Override
            public void customLayout(View v) {
                TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                TextView ivCancel = (TextView) v.findViewById(R.id.iv_cancel);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvTime.returnData();
                        pvTime.dismiss();
                    }
                });
                ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvTime.dismiss();
                    }
                });
            }
        });//??????????????????
// ?????????????????????v,??????show()???????????????????????? View ???????????????show??????????????????????????????v??????null
//?????????????????? ????????????????????????????????????????????????

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {
            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//??????????????????
                dialogWindow.setGravity(Gravity.BOTTOM);//??????Bottom,????????????
                dialogWindow.setDimAmount(0.5f);
                //????????????
//                dialogWindow.setBackgroundDrawableResource(R.drawable.bg_white_10);
                final WindowManager.LayoutParams params = dialogWindow.getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                dialogWindow.setAttributes(params);
            }
        }

        return pvTime;
    }

    private static String province;
    private static String city;
    private static String county;
    private static String addressDetail;
    private static List<JsonBean> options1Items = new ArrayList<>();
    private static ArrayList<ArrayList<JsonBean>> options2Items = new ArrayList<>();
    private static ArrayList<ArrayList<ArrayList<JsonBean>>> options3Items = new ArrayList<>();
    private static Thread thread;
    private static boolean isLoaded = false;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;

    public static void showPickerView(Context context, TextView textView) {// ???????????????
        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //?????????????????????????????????????????????
                province = options1Items.size() > 0 ?
                        options1Items.get(options1).getPickerViewText() : "";
//                provinceCode = options1Items.size() > 0 ?
//                        options1Items.get(options1).getCode() : "";

                city = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2).getName() : "";
//                cityCode = options2Items.size() > 0
//                        && options2Items.get(options1).size() > 0 ?
//                        options2Items.get(options1).get(options2).getCode() : "";

                county = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3).getName() : "";
//                countryCode = options2Items.size() > 0
//                        && options3Items.get(options1).size() > 0
//                        && options3Items.get(options1).get(options2).size() > 0 ?
//                        options3Items.get(options1).get(options2).get(options3).getCode() : "";

                String tx = province + "-" + city + "-" + county;
                textView.setText(tx);
            }
        })

                .setTitleText("????????????")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.parseColor("#04C7BE")) //???????????????????????????
                .setContentTextSize(20)
                .setSubmitColor(Color.parseColor("#04C7BE"))
                .setCancelColor(Color.parseColor("#818596"))
                .setLayoutRes(R.layout.pickerview_options2, null)
//                .setTitleBgColor(Color.parseColor("#FFFFFF"))
                .build();

        /*pvOptions.setPicker(options1Items);//???????????????
        pvOptions.setPicker(options1Items, options2Items);//???????????????*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//???????????????
        pvOptions.show();
    }

    public static void showPickerView2(Context context, TextView textView) {// ???????????????
        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //?????????????????????????????????????????????
                province = options1Items.size() > 0 ?
                        options1Items.get(options1).getPickerViewText() : "";
//                provinceCode = options1Items.size() > 0 ?
//                        options1Items.get(options1).getCode() : "";

                city = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2).getName() : "";
//                cityCode = options2Items.size() > 0
//                        && options2Items.get(options1).size() > 0 ?
//                        options2Items.get(options1).get(options2).getCode() : "";

                county = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3).getName() : "";
//                countryCode = options2Items.size() > 0
//                        && options3Items.get(options1).size() > 0
//                        && options3Items.get(options1).get(options2).size() > 0 ?
//                        options3Items.get(options1).get(options2).get(options3).getCode() : "";

                String tx = province + "-" + city + "-" + county;
                textView.setText(tx);
            }
        })

                .setTitleText("????????????")
                .setDividerColor(Color.WHITE)
                .setTextColorCenter(Color.parseColor("#04C7BE")) //???????????????????????????
                .setContentTextSize(14)
                .setSubmitColor(Color.WHITE)
                .setCancelColor(Color.WHITE)
                .setSubCalSize(14)
                .setLineSpacingMultiplier(1.8f)
                .setLayoutRes(R.layout.pickerview_options2, null)
//                .setTitleBgColor(Color.parseColor("#FFFFFF"))
                .build();

        /*pvOptions.setPicker(options1Items);//???????????????
        pvOptions.setPicker(options1Items, options2Items);//???????????????*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//???????????????
        pvOptions.show();
    }

    public static void initJsonData() {//????????????

        /**
         * ?????????assets ????????????Json??????????????????????????????????????????????????????
         * ???????????????????????????
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(AppUtils.getApp(), "cityList.json");//??????assets????????????json????????????

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//???Gson ????????????

        /**
         * ??????????????????
         *
         * ???????????????????????????JavaBean????????????????????????????????? IPickerViewData ?????????
         * PickerView?????????getPickerViewText????????????????????????????????????
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//????????????
            ArrayList<JsonBean> cityList = new ArrayList<>();//????????????????????????????????????
            ArrayList<ArrayList<JsonBean>> province_AreaList = new ArrayList<>();//??????????????????????????????????????????

            for (int c = 0; c < jsonBean.get(i).getChildren().size(); c++) {//??????????????????????????????
                cityList.add(jsonBean.get(i).getChildren().get(c));//????????????
                ArrayList<JsonBean> city_AreaList = new ArrayList<>();//??????????????????????????????

                //??????????????????????????????????????????????????????????????????null ?????????????????????????????????????????????
                /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }*/
                city_AreaList.addAll(jsonBean.get(i).getChildren().get(c).getChildren());
                province_AreaList.add(city_AreaList);//??????????????????????????????
            }

            /**
             * ??????????????????
             */
            options2Items.add(cityList);

            /**
             * ??????????????????
             */
            options3Items.add(province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }


    private static ArrayList<JsonBean> parseData(String result) {//Gson ??????
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONObject data = new JSONObject(result);

            JSONArray array = data.getJSONObject("data").getJSONArray("list");
            Gson gson = new Gson();
            for (int i = 0; i < array.length(); i++) {
                JsonBean entity = gson.fromJson(array.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//????????????????????????????????????????????????
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // ?????????????????????????????????
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:

                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:

                    break;


            }
        }
    };
}
