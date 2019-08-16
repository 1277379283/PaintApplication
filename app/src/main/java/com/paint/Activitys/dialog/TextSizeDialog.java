package com.paint.Activitys.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.paint.Activitys.R;
import com.paint.Activitys.view.StrokeTextView;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * 类描述：设置文字大小 弹窗
 * 创建人：张飞祥
 * 创建时间：2019/2/19 10:43
 * 修改人：张飞祥
 * 修改时间：2019/2/19 10:43
 * 修改备注：
 */
public abstract class TextSizeDialog extends Dialog implements View.OnClickListener {

    //进度条
    private SeekBar tvSeekbar;
    //文字大小
    private TextView tvSize;
    //取消
    private TextView tvCancel;
    //确认
    private TextView tvConfirm;
    //显示
    private StrokeTextView tvPaint;
    //上下文
    private Activity mActivity = null;
    //当前字体大小
    private float textSize;


    /**
     * 构造方法
     *
     * @param activity
     */
    public TextSizeDialog(@NonNull Activity activity, float size) {
        super(activity, R.style.dialog_default_theme);
        this.mActivity = activity;
        this.textSize = size;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_text_size);
        //初始化UI
        initUI();
        //TODO 取消
        tvCancel.setOnClickListener(this);
        //TODO 确认
        tvConfirm.setOnClickListener(this);


        tvPaint.initStroke((30-textSize), Color.WHITE);
        //设置文字
        tvSize.setText(textSize+"");
        int size = (int) (textSize * 10f);
        tvSeekbar.setProgress(size);
        tvSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int size, boolean b) {
                textSize = size / 10f;
                tvSize.setText(textSize+"");
                tvPaint.initStroke(30 - textSize, Color.WHITE);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }



    /**
     * 点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.tv_cancel:
                cancel();
                break;

            case R.id.tv_confirm:
                setTextSize(textSize);
                cancel();
                break;
        }
    }


    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = AutoLinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = AutoLinearLayout.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }

    /**
     * 控件初始化
     */
    private void initUI() {
        //进度条
        tvSeekbar = findViewById(R.id.tv_seekbar);
        //文字大小
        tvSize = findViewById(R.id.tv_size);
        //取消
        tvCancel = findViewById(R.id.tv_cancel);
        //确认
        tvConfirm = findViewById(R.id.tv_confirm);
        //示例文本
        tvPaint = findViewById(R.id.tv_paint);
    }

    //设置字体大小
    public abstract void setTextSize (float size);
}
