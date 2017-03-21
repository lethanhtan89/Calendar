package com.example.administrator.calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 3/15/2017.
 */

public class Image implements Serializable{
    private Date date;
    private Bitmap bitmap;
    private String url;
    private boolean select = false;
    Bitmap[] bitmaps;
    ArrayList<Bitmap> bitmapArrayList;

    public Image(ArrayList<Bitmap> bitmapArrayList){
        this.bitmapArrayList = bitmapArrayList;
    }

    public Image(Bitmap[] bitmaps){
        this.bitmaps = bitmaps;
    }

    public Image(String url, boolean select){
        this.url = url;
        this.select = select;
    }

    public Image(Bitmap bitmap, String url){
        this.bitmap = bitmap;
        this.url = url;
    }

    public Image(String url, Date date){
        this.url = url;
        this.date = date;
    }

    public Image(String url){
        this.url = url;
    }

    public Image(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public void saveImagetoPre(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstant.PRE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("url", url);
        editor.commit();
    }

    public Image loadImagePre(Context context){
        Image image = null;
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstant.PRE, Context.MODE_WORLD_READABLE);
        if(image != null){
            image.url = sharedPreferences.getString("url", image.getUrl());
        }
        return image;
    }

    public Bitmap[] getBitmaps() {
        return bitmaps;
    }

    public void setBitmaps(Bitmap[] bitmaps) {
        this.bitmaps = bitmaps;
    }

    public ArrayList<Bitmap> getBitmapArrayList() {
        return bitmapArrayList;
    }

    public void setBitmapArrayList(ArrayList<Bitmap> bitmapArrayList) {
        this.bitmapArrayList = bitmapArrayList;
    }
}
