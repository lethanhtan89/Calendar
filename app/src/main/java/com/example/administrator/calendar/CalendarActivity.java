package com.example.administrator.calendar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Administrator on 3/9/2017.
 */

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int IMAGE_GALLERY = 10000;
    private static final int IMAGE_CAMERA = 10001;
    private Uri uriCameraImage;
    TextView txtPicture, txtUpload, txtCompare;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        setupToolbar();
        init();
    }

    private void init(){
        txtPicture = (TextView) findViewById(R.id.txtPicture);
        txtUpload = (TextView) findViewById(R.id.txtUpload);
        txtCompare = (TextView) findViewById(R.id.txtCompare);
        txtPicture.setOnClickListener(this);
        txtUpload.setOnClickListener(this);
        txtCompare.setOnClickListener(this);
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.calendar_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        TextView txtCalendar = (TextView) findViewById(R.id.txtToolBarCalendar);
        txtCalendar.setText("CALENDAR");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.txtPicture:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String url = "tmp_" + String.valueOf(System.currentTimeMillis());
                uriCameraImage = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriCameraImage);
                startActivityForResult(intent, IMAGE_CAMERA);
                break;
            case R.id.txtUpload:
                intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, IMAGE_GALLERY);
                break;
            case R.id.txtCompare:
                Toast.makeText(getApplicationContext(), "Compare", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
