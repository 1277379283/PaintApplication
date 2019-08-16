package com.paint.Activitys.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.jacky.commondraw.model.stroke.InsertableObjectStroke;
import com.jacky.commondraw.views.doodleview.DoodleEnum;
import com.jacky.commondraw.views.doodleview.DoodleView;
import com.jrummyapps.android.colorpicker.ColorPanelView;
import com.jrummyapps.android.colorpicker.ColorPickerDialog;
import com.jrummyapps.android.colorpicker.ColorPickerDialogListener;
import com.paint.Activitys.R;
import com.paint.Activitys.activity.base.BaseActivity;
import com.paint.Activitys.dialog.EraseSizeDialog;
import com.paint.Activitys.dialog.TextSizeDialog;
import com.paint.Activitys.utils.ImageUtils;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    //颜色板
    @Bind(R.id.view_color_title) ColorPanelView viewColorTitle;
    //当前画笔
    @Bind(R.id.tv_paint_title) TextView tvPaintTitle;
    //笔触粗细
    @Bind(R.id.tv_thickness_title) TextView tvThicknessTitle;
    //书写板
    @Bind(R.id.surface_view) DoodleView surfaceView;


    //文件权限 请求
    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    //笔型
    private int penStyle = InsertableObjectStroke.STROKE_TYPE_PEN;
    private String penStyleStr = "钢笔";
    //颜色
    private int penColor = Color.BLACK;
    //不透明度
    private int penAlpha = 255;
    //文字大小
    private float penThickness = 15.0f;
    //橡皮擦大小
    private float eraseSize = 100.0f;
    //大小 flag
    private boolean isSizeFlag = true;
    //颜色dialog id
    public static final int DIALOG_ID = 0;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initData() {
        //设置背景
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.text_bg, null);
        surfaceView.setBackgroundBitmap(bitmap);
    }


    /**
     * 按钮点击事件
     * @return
     */
    @OnClick({R.id.btn_paint_style,R.id.btn_paint_rubber,R.id.btn_paint_color,
            R.id.btn_brush_strokes,R.id.btn_up,R.id.btn_next,R.id.btn_clean_up,R.id.btn_save})
    public void onClick(View view){
        switch (view.getId()){

            case R.id.btn_paint_style:           //笔型
                paintItemDialog();
                break;

            case R.id.btn_paint_rubber:          //橡皮
                setPenRubber();
                break;

            case R.id.btn_paint_color:           //颜色
                showColorSelect();
                break;

            case R.id.btn_brush_strokes:         //笔触
                setShowSizeDialog();
                break;

            case R.id.btn_up:                    //撤销
                surfaceView.undo();
                break;

            case R.id.btn_next:                  //恢复
                surfaceView.redo();
                break;

            case R.id.btn_clean_up:              //清除
                surfaceView.clearStrokes();
                break;

            case R.id.btn_save:                  //保存
                saveImage();
                break;
        }
    }

    /**
     * 笔型弹窗
     */
    private void paintItemDialog(){
        //item 弹窗显示的文字
        final String[] items = {"圆珠笔","钢笔","毛笔","记号笔","铅笔","喷枪"};

        new AlertDialog.Builder(MainActivity.this)
                .setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0)
                    penStyle = InsertableObjectStroke.STROKE_TYPE_NORMAL;
                else if (which == 1)
                    penStyle = InsertableObjectStroke.STROKE_TYPE_PEN;
                else if (which == 2)
                    penStyle = InsertableObjectStroke.STROKE_TYPE_BRUSH;
                else if (which == 3)
                    penStyle = InsertableObjectStroke.STROKE_TYPE_MARKER;
                else if(which == 4)
                    penStyle = InsertableObjectStroke.STROKE_TYPE_PENCIL;
                else
                    penStyle = InsertableObjectStroke.STROKE_TYPE_AIRBRUSH;

                //设置标题
                tvPaintTitle.setText(items[which]);
                penStyleStr = items[which];
                //重置画笔
                setPenStyle();
                //销毁弹窗
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * 公用设置笔型方法
     */
    private void setPenStyle(){
        //设置弹窗flag
        isSizeFlag = true;
        //绘画图纸
        surfaceView.setInputMode(DoodleEnum.InputMode.DRAW);
        //设置笔型加入画板
        surfaceView.setStrokeType(penStyle);
        //设置笔型
        surfaceView.setStrokeAttrs(penStyle,penColor,penThickness,penAlpha);
        //设置笔触粗细
        tvThicknessTitle.setText(penThickness+"");
    }

    /**
     * 公用设置橡皮方法
     */
    private void setPenRubber(){
        //设置弹窗flag
        isSizeFlag = false;
        //擦除图纸
        surfaceView.setInputMode(DoodleEnum.InputMode.ERASE);
        //设置 橡皮擦 宽度 & 图片
        surfaceView.setEraseAttribute(eraseSize,R.drawable.img_round);
        //设置标题
        tvPaintTitle.setText("橡皮");
        //设置橡皮擦大小
        tvThicknessTitle.setText(eraseSize+"");
    }

    /**
     * 颜色设置
     */
    private void showColorSelect() {
        int color = viewColorTitle.getColor();
        //传入的默认color
        final ColorPickerDialog colorPickerDialog = ColorPickerDialog.newBuilder().setColor(color)
                .setDialogTitle(R.string.cpv_default_title)     //设置dialog标题
                .setDialogType(ColorPickerDialog.TYPE_PRESETS)  //设置为自定义模式
                .setDialogId(DIALOG_ID)                         //设置Id,回调时传回用于判断
                .setShowAlphaSlider(true)                       //设置默认没有透明度
                .setAllowPresets(true)                          //显示预知模式
                .create();
        colorPickerDialog.setColorPickerDialogListener(new ColorPickerDialogListener() {
            @Override
            public void onColorSelected(int dialogId, int color) {
                //设置显示颜色
                viewColorTitle.setColor(color);
                //设置画笔颜色
                penColor = color;
                //设置不透明度
                penAlpha =  colorPickerDialog.getAlpha();
                //设置标题
                tvPaintTitle.setText(penStyleStr);
                //重置画笔
                setPenStyle();
            }
            @Override
            public void onDialogDismissed(int dialogId) {}
        });
        //设置回调，用于获取选择的颜色
        colorPickerDialog.show(this.getFragmentManager(), "color-picker-dialog");
    }

    /**
     * 动态设置 橡皮擦 & 字体大小弹窗
     */
    private void setShowSizeDialog(){
        if (isSizeFlag){
            new TextSizeDialog(this,penThickness) {
                @Override
                public void setTextSize(float size) {
                    //设置大小
                    penThickness = size;
                    //重置画笔
                    setPenStyle();
                }
            }.show();
        }else{
            new EraseSizeDialog(this, eraseSize) {
                @Override
                public void setEraseSize(float size) {
                    //设置大小
                    eraseSize = size;
                    setPenRubber();
                }
            }.show();
        }
    }

    /**
     * 图片保存
     *
     * 1.获取文件读写权限
     * 2.如有全选则保存图片
     *
     */
    private void saveImage(){
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }else{
            //调用图片保存功能
            ImageUtils.saveImageToGallery(getApplicationContext(), surfaceView.newWholeBitmap(true));
        }
    }
}
