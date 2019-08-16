package com.paint.Activitys.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.zhy.autolayout.AutoLayoutActivity;
import com.zhy.autolayout.config.AutoLayoutConifg;

import butterknife.ButterKnife;


/**
 * 项目名称：
 * 类描述：父类公用Activity方法
 * 创建人：张飞祥
 * 创建时间：2018/04/18 下午2:17
 * 修改人：张飞祥
 * 修改时间：2018/04/18 下午2:17
 * 修改备注：
 */
public abstract class BaseActivity extends AutoLayoutActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    protected abstract void initUI();

    protected abstract void initData();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * android每次点击桌面图标应用重启的解决办法，
         * 第一个 activity 的启动模式必须改为 android:launchMode="standard"
         */
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        AutoLayoutConifg.getInstance().useDeviceSize();
        initUI();
        initData();
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        this.injectViews();
    }


    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        this.injectViews();
    }


    public void setContentView(View view) {
        super.setContentView(view);
        this.injectViews();
    }


    private void injectViews() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        finish();
        super.onDestroy();
    }

    /**
     * 设置 状态栏位透明
     */
    public void setThemeTransparent(){
        /**
         * 设置透明状态栏
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
