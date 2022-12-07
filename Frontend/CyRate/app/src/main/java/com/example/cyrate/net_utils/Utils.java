package com.example.cyrate.net_utils;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Utils {
    public static final int IMG_REQUEST = 1;

    public static String imageToString(Bitmap bitmap){

        ByteArrayOutputStream bOutStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bOutStream);
        byte[] imgBytes = bOutStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }


}
