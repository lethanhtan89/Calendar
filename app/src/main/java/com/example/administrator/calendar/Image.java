package com.example.administrator.calendar;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 3/15/2017.
 */

public class Image implements Serializable{
    private Date date;
    private Bitmap bitmap;
    private String url;
    private boolean select = false;

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
}
