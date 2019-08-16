package com.paint.Activitys.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 项目名称：
 * 类描述：图片处理类
 * 创建人：张飞祥
 * 创建时间：2018/04/18 下午2:17
 * 修改人：张飞祥
 * 修改时间：2018/04/18 下午2:17
 * 修改备注：
 */
public class ImageUtils {

    public static final int GET_IMAGE_BY_CAMERA = 5001;
    public static final int GET_IMAGE_FROM_PHONE = 5002;
    public static final int CROP_IMAGE = 5003;
    public static Uri imageUriFromCamera;
    public static Uri cropImageUri;


    public static void openCameraImage(final Activity activity) {
        ImageUtils.imageUriFromCamera = ImageUtils.createImagePathUri(activity);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUtils.imageUriFromCamera);
        activity.startActivityForResult(intent, ImageUtils.GET_IMAGE_BY_CAMERA);
    }


    public static void openLocalImage(final Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intent, ImageUtils.GET_IMAGE_FROM_PHONE);
    }


    public static Uri createImagePathUri(Context context) {
        Uri imageFilePath = null;
        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
        values.put(MediaStore.Images.Media.TITLE, imageName);
        values.put(MediaStore.Images.Media.DATE_ADDED, time);
        values.put(MediaStore.Images.Media.DATE_TAKEN, time);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        if (Environment.getExternalStorageState()
                       .equals(Environment.MEDIA_MOUNTED)) {
            imageFilePath = context.getContentResolver()
                                   .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            imageFilePath = context.getContentResolver()
                                   .insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
        }
        return imageFilePath;
    }


    public static void crop(Activity activity, Uri uri) {
        ImageUtils.cropImageUri = ImageUtils.createImagePathUri(activity);
        if (uri == null) {
            return;
        }
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", false);// true:不返回uri，false：返回uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUtils.cropImageUri);
        activity.startActivityForResult(intent, CROP_IMAGE);
    }


    public static void deleteImageUri(Context context, Uri uri) {
        context.getContentResolver().delete(uri, null, null);
    }


    /**
     * 获取网落图片资源
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * 加载高清大图的适当缩放功能的方法
     * @param activity
     * @param imagePath
     * @return
     */
    public static Bitmap loadingImageBitmap(Activity activity, String imagePath) {
        /**
         * 获取屏幕的宽与高
         */
        final int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        final int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        /**
         * 通过设置optios来只加载大图的尺寸
         */
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(imagePath, options);
            /**
             * 计算手机宽高与显示大图的宽高，然后确定缩放有比例
             */
            int widthRaio = (int) Math.ceil(options.outWidth/(float)width);
            int heightRaio = (int) Math.ceil(options.outHeight/(float)height);
            if (widthRaio>1&&heightRaio>1){
                if (widthRaio>heightRaio){
                    options.inSampleSize = widthRaio;
                }else {
                    options.inSampleSize = heightRaio;
                }
            }
            /**
             * 设置加载缩放后的图片
             */
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(imagePath, options);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * 获取控件截图
     *
     * Bitmap.Config：
     *      ARGB_8888  透明
     *      RGB_565    黑色背景
     *
     * @param v
     * @return
     */
    public static Bitmap loadBitmapFromView(View v) {
        if (v == null) {
            return null;
        }
        Bitmap screenshot;
        screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(screenshot);
        v.draw(c);
        return screenshot;
    }


    /**
     * bitmap 保存到 图库
     * @param context
     * @param bmp
     */
    public static File saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Paint_images");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".png";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！，记得要传你更新的file哦
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        //弹出提示
        Toast.makeText(context,"图片保存成功",Toast.LENGTH_SHORT).show();
        return file;
    }
}
