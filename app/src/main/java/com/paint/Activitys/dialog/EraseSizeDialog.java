package com.paint.Activitys.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jacky.commondraw.utils.LoadUtils;
import com.paint.Activitys.R;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * 类描述：设置橡皮擦大小 弹窗
 * 创建人：张飞祥
 * 创建时间：2019/2/19 10:43
 * 修改人：张飞祥
 * 修改时间：2019/2/19 10:43
 * 修改备注：
 */
public abstract class EraseSizeDialog extends Dialog implements View.OnClickListener {

    //进度条
    private SeekBar tvSeekbar;
    //图片
    private ImageView ivErase;
    //橡皮擦大小
    private TextView tvSize;
    //取消
    private TextView tvCancel;
    //确认
    private TextView tvConfirm;
    //上下文
    private Activity mActivity = null;
    //当前字体大小
    private float eraseSize;
    //当前橡皮擦 图片资源
    public Bitmap eraseBitmap = null;

    /**
     * 构造方法
     *
     * @param activity
     */
    public EraseSizeDialog(@NonNull Activity activity, float size) {
        super(activity, R.style.dialog_default_theme);
        this.mActivity = activity;
        this.eraseSize = size;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_erase_size);
        //初始化UI
        initUI();
        //TODO 取消
        tvCancel.setOnClickListener(this);
        //TODO 确认
        tvConfirm.setOnClickListener(this);

        //初始化 橡皮擦
        initEraseSize();
        tvSeekbar.setProgress((int) eraseSize);
        //滑动事件
        tvSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int size, boolean b) {

                if (size<=1)
                    eraseSize = 1;
                else
                    eraseSize = size;

                initEraseSize();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }


    /**
     * 设置橡皮擦 大小
     */
    private void initEraseSize(){
        //设置文字
        tvSize.setText(eraseSize+"");
        //初始化你需要显示的光标样式
        eraseBitmap = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.img_round);
        eraseBitmap = LoadUtils.getNewBitmap(eraseBitmap,eraseSize,eraseSize);
        ivErase.setImageBitmap(eraseBitmap);
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
                setEraseSize(eraseSize);
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
        //橡皮擦
        ivErase = findViewById(R.id.iv_erase);
        //橡皮擦大小
        tvSize = findViewById(R.id.tv_size);
        //取消
        tvCancel = findViewById(R.id.tv_cancel);
        //确认
        tvConfirm = findViewById(R.id.tv_confirm);
    }

    //设置橡皮擦大小
    public abstract void setEraseSize (float size);
}
