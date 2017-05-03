//package org.noear.sitedemo.utils;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.view.View;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
///**
// * Created by yuety on 16/1/7.
// */
//public final class ImageUtil {
//    public static void saveImageToGallery(Context context, Bitmap img) {
//        MediaStore.Images.Media.insertImage(context.getContentResolver(), img, "多多猫", "多多猫");
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//                Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
//    }
//
//    public Bitmap reduce(Bitmap bitmap, View view) {
//        if (view.getWidth() == 0 || view.getHeight() == 0) {
//            return bitmap;
//        }
//        int bitmapHeight = bitmap.getHeight();
//        if (view.getWidth() > view.getHeight()) {
//            bitmapHeight = bitmap.getWidth() * view.getHeight() / view.getWidth();
//        }
//
//        //the error occurt here
//        Bitmap temp = Bitmap.createBitmap(bitmap,  bitmap.getWidth()/3, bitmap.getHeight() /3 , bitmap.getWidth()/4,
//                bitmapHeight/4);
//        return Bitmap.createScaledBitmap(temp, temp.getWidth() / 2, temp.getHeight() / 2, true);
//    }
//}
