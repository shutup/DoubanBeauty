package com.shutup.doubanbeauty;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.PermissionChecker;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by shutup on 2016/12/10.
 */

public class ImageUtils {


    public static void saveImageToGallery(Context context, ImageView imageView) {
        imageView.setDrawingCacheEnabled(true);//开启catch，开启之后才能获取ImageView中的bitmap
        Bitmap bitmap = imageView.getDrawingCache();//获取imageview中的图像
        saveImageToGallery(context,bitmap);
        imageView.setDrawingCacheEnabled(false);
    }

    public static void saveImageToGallery(final Context context, final Bitmap bmp) {
        int result = PermissionChecker.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PermissionChecker.PERMISSION_GRANTED) {
            save(context, bmp);
        }else {
            XPermissionUtils.requestPermissions(context,1,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},new XPermissionUtils.OnPermissionListener(){

                @Override
                public void onPermissionGranted() {
                    save(context, bmp);
                }

                @Override
                public void onPermissionDenied() {

                }
            });
        }
    }

    private static void save(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "DCIM/DoubanBeauty");
        if (!appDir.exists()) {
            if (!appDir.mkdirs()) {
                Toast.makeText(context, "save failed", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + appDir.getAbsolutePath())));
    }
}
