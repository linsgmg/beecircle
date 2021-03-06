package com.lin.baselib.widgt.klinechart.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.lin.baselib.R;
import com.lin.baselib.widgt.klinechart.BaseKLineChartView;
import com.lin.baselib.widgt.klinechart.base.IChartDraw;
import com.lin.baselib.widgt.klinechart.base.IValueFormatter;
import com.lin.baselib.widgt.klinechart.entity.IVolume;
import com.lin.baselib.widgt.klinechart.formatter.BigValueFormatter;
import com.lin.baselib.widgt.klinechart.utils.ViewUtil;

import java.math.BigDecimal;



/**
 * Created by hjm on 2017/11/14 17:49.
 */
public class VolumeDraw implements IChartDraw<IVolume> {

    private Paint mRedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mGreenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma5Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ma10Paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int pillarWidth = 0;

    public VolumeDraw(BaseKLineChartView view) {
        Context context = view.getContext();
        mRedPaint.setColor(ContextCompat.getColor(context, R.color.chart_red));
        mGreenPaint.setColor(ContextCompat.getColor(context, R.color.chart_green));
        pillarWidth = ViewUtil.Dp2Px(context, 4);
    }

    @Override
    public void drawTranslated(
            @Nullable IVolume lastPoint, @NonNull IVolume curPoint, float lastX, float curX,
            @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position) {

        drawHistogram(canvas, curPoint, lastPoint, curX, view, position);
        if (lastPoint.getMA5Volume() != 0f) {
            view.drawVolLine(canvas, ma5Paint, lastX, lastPoint.getMA5Volume(), curX, curPoint.getMA5Volume());
        }
        if (lastPoint.getMA10Volume() != 0f) {
            view.drawVolLine(canvas, ma10Paint, lastX, lastPoint.getMA10Volume(), curX, curPoint.getMA10Volume());
        }
    }

    private void drawHistogram(
            Canvas canvas, IVolume curPoint, IVolume lastPoint, float curX,
            BaseKLineChartView view, int position) {

        float r = pillarWidth / 2;
        float top = view.getVolY(curPoint.getVolume());
        int bottom = view.getVolRect().bottom;
        if (curPoint.getClosePrice() >= curPoint.getOpenPrice()) {//???
            canvas.drawRect(curX - r, top, curX + r, bottom, mRedPaint);
        } else {
            canvas.drawRect(curX - r, top, curX + r, bottom, mGreenPaint);
        }

    }

    @Override
    public void drawText(
            @NonNull Canvas canvas, @NonNull BaseKLineChartView view, int position, float x, float y) {
        IVolume point = (IVolume) view.getItem(position);
//        String text = "MA(7,30):" + getValueFormatter().format(point.getVolume()) + "  ";

        String text = "VOL:" +new BigDecimal(point.getVolume()).setScale(2,4).toPlainString()+" ";
        canvas.drawText(text, x, y+15, view.getTextPaint());
        x += view.getTextPaint().measureText(text);
        text = " ??MA5:" + new BigDecimal(point.getMA5Volume()).setScale(2,4).toPlainString()+" ";
        canvas.drawText(text, x, y+15, ma5Paint);
        x += ma5Paint.measureText(text);
        text = " ??MA10:" + new BigDecimal(point.getMA10Volume()).setScale(2,4).toPlainString();
        canvas.drawText(text, x, y+15, ma10Paint);
    }

    @Override
    public float getMaxValue(IVolume point) {
        return Math.max(point.getVolume(), Math.max(point.getMA5Volume(), point.getMA10Volume()));
    }

    @Override
    public float getMinValue(IVolume point) {
        return Math.min(point.getVolume(), Math.min(point.getMA5Volume(), point.getMA10Volume()));
    }

    @Override
    public IValueFormatter getValueFormatter() {
        return new BigValueFormatter();
    }

    /**
     * ?????? MA5 ????????????
     *
     * @param color
     */
    public void setMa5Color(int color) {
        this.ma5Paint.setColor(color);
    }

    /**
     * ?????? MA10 ????????????
     *
     * @param color
     */
    public void setMa10Color(int color) {
        this.ma10Paint.setColor(color);
    }

    public void setLineWidth(float width) {
        this.ma5Paint.setStrokeWidth(width);
        this.ma10Paint.setStrokeWidth(width);
    }

    /**
     * ??????????????????
     *
     * @param textSize
     */
    public void setTextSize(float textSize) {
        this.ma5Paint.setTextSize(textSize);
        this.ma10Paint.setTextSize(textSize);
    }


}
