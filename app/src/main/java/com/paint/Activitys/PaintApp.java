package com.paint.Activitys;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;

import com.zhy.autolayout.config.AutoLayoutConifg;



/*
*
*          ┌─┐       ┌─┐
*       ┌──┘ ┴───────┘ ┴──┐
*       │                 │
*       │       ───       │
*       │  ─┬┘       └┬─  │
*       │                 │
*       │       ─┴─       │
*       │                 │
*       └───┐         ┌───┘
*           │         │
*           │         │
*           │         │
*           │         └──────────────┐
*           │                        │
*           │                        ├─┐
*           │                        ┌─┘
*           │                        │
*           └─┐  ┐  ┌───────┬──┐  ┌──┘
*             │ ─┤ ─┤       │ ─┤ ─┤
*             └──┴──┘       └──┴──┘
*                 神兽保佑
*                 代码无BUG!
*/

/**
 * 项目名称：金泽云 手机app（办卡、实名认证）
 * 类描述：APP 运行监视服务
 * 创建人：张飞祥
 * 创建时间：2018/3/1 13:30
 * 修改人：张飞祥
 * 修改时间：2018/3/1 13:30
 * 修改备注：
 */
public class PaintApp extends Application {

    // 上下文菜单
    private static PaintApp instance;
    // 记录是否已经初始化
    private boolean isInit = false;
    private Typeface typeface=null;


    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();
        instance = (PaintApp) getApplicationContext();

        /**
         * 初始化屏幕适配
         */
        AutoLayoutConifg.getInstance().useDeviceSize();

        //设置字体
        typeface = Typeface.createFromAsset(instance.getAssets(), "fonts/Sillii-Willinn-2.ttf");//下载的字体
    }

    public static  PaintApp getInstace() {
        return instance;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    /**
     * 初始化  MultiDex.install(this); （必须 重写此方法 设置 MultiDex）
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
