package com.example.administrator.calendar.images;

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
    private String time;

    public Image(String url, Date date){
        this.url = url;
        this.date = date;
    }

    public Image(String url, String time){
        this.url = url;
        this.time = time;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
