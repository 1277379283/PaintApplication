package com.paint.Activitys.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.paint.Activitys.PaintApp;

/**
 * 项目名称：
 * 类描述：
 * 创建人：张飞祥
 * 创建时间：2019/8/14 16:02
 * 修改人：张飞祥
 * 修改时间：2019/8/14 16:02
 * 修改备注：
 */
public class StrokeTextView extends android.support.v7.widget.AppCompatTextView {

    public StrokeTextView(Context context) {
        super(context);
        setTypeface(PaintApp.getInstace().getTypeface());
    }

    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(PaintApp.getInstace().getTypeface());
    }

    public StrokeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTypeface(PaintApp.getInstace().getTypeface());
    }

    public void initStroke(float width,int color) {

        TextPaint mPaint = this.getPaint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(width/6);// 描边宽度
        mPaint.setStyle(Paint.Style.STROKE);
        setTextColor(color);// 描边颜色

        if (width <= 1)
            setTextColor(Color.BLACK);
        setGravity(getGravity());
        //刷新页面
        invalidate();
    }
}
