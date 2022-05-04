package com.lin.baselib.widgt.klinechart.formatter;


import com.lin.baselib.widgt.klinechart.base.IDateTimeFormatter;
import com.lin.baselib.widgt.klinechart.utils.DateUtil;

import java.util.Date;


/**
 * 时间格式化器
 * Created by tifezh on 2016/6/21.
 */

public class DateFormatter implements IDateTimeFormatter {
    @Override
    public String format(Date date) {
        if (date != null) {
            return DateUtil.DateFormat.format(date);
        } else {
            return "";
        }
    }
}
