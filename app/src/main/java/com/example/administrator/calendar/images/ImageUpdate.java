package com.example.administrator.calendar.images;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 3/24/2017.
 */

public class ImageUpdate implements Serializable {
    private String url;
    private Date date;

    public ImageUpdate(String url, Date date){
        this.url = url;
        this.date = date;
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
}
