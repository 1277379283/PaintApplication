package com.jacky.commondraw.visual.brush.operation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * 项目名称：
 * 类描述：橡皮擦 跟随手指移动view
 * 创建人：张飞祥
 * 创建时间：2019/8/12 14:41
 * 修改人：张飞祥
 * 修改时间：2019/8/12 14:41
 * 修改备注：
 */
public class EraseDrawView extends View {
    public float currentX = 50;
    public float currentY = 50;

    public EraseDrawView(Context context) {
        super(context);
    }
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(currentX,currentY,10,paint);
    }
}
