package com.example.administrator.calendar.slider;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.administrator.calendar.R;
import com.roomorama.caldroid.CaldroidFragment;

@SuppressLint("SimpleDateFormat")
public class SliderCusActivity extends AppCompatActivity{
    private CaldroidFragment caldroidFragment;

    private static final int IMAGE_GALLERY = 10000;
    private static final int IMAGE_CAMERA = 10001;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final String BITAMP = "bitmap";
    private Uri uriCameraImage;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    TextView txtPicture, txtUpload, txtCompare;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        setupToolbar();

        caldroidFragment = new SliderFragment();

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.layout_view, caldroidFragment);
        t.commit();


    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.slider_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        TextView txtCalendar = (TextView) findViewById(R.id.txtToolBarImage);
        txtCalendar.setText("imagew");

    }

}
