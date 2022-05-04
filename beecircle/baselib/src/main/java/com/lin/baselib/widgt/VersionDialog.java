package com.lin.baselib.widgt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lin.baselib.R;

public class VersionDialog {
    public static Dialog showLogoutDialog(final Context context, String content, OnConfirmClickListener myListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        /*true 代表点击空白可消失   false代表点击空白哦不可消失 */
        builder.setCancelable(false);
        View view = View.inflate(context, R.layout.dialog_version, null);

        TextView tv_title = view.findViewById(R.id.tv_subContent);
        TextView tvOk = view.findViewById(R.id.tv_positive);
        TextView tvCancel = view.findViewById(R.id.tv_negative);

        if (!TextUtils.isEmpty(content)) {
            tv_title.setText(content);
        }


        builder.setView(view);
        final AlertDialog dialog = builder.create();

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myListener != null) {
                    myListener.onPositiveClick(dialog);
                    dialog.dismiss();
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListener.onNegativeClick(dialog);
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public interface OnConfirmClickListener {
        void onNegativeClick(Dialog dialog);
        void onPositiveClick(Dialog dialog);
    }
}
