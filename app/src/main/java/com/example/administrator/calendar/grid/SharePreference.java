package com.example.administrator.calendar.grid;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.administrator.calendar.AppConstant;

import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 3/21/2017.
 */

public class SharePreference {
    private Context context;
    public SharePreference(Context context){
        this.context = context;
    }
    public boolean setImagetoPre(Bitmap bitmap){
        SharedPreferences preferences = context.getSharedPreferences(null, context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AppConstant.PRE, BitMapToString(bitmap));
        editor.commit();
        return true;
    }

    public Bitmap getImagefromPre(Bitmap bitmap){
        SharedPreferences preferences = context.getSharedPreferences(null, context.MODE_WORLD_READABLE);
        String url = preferences.getString(AppConstant.PRE, "can not get");
        return StringToBitMap(url);
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}
