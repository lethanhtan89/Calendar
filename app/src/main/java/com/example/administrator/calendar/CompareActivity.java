package com.example.administrator.calendar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.calendar.grid.CaldroidSampleActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 3/20/2017.
 */

public class CompareActivity extends AppCompatActivity {
    private Bitmap bitmap;
    private ArrayList<Bitmap> bitmapArrayList;
    private ImageView imageView;
    private ProgressDialog dialog;
    private Matrix matrix;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        setupToolbar();
        bitmapArrayList = new ArrayList<>();
        imageView = (ImageView) findViewById(R.id.imgCompare);

        new AsyncTask<String, Void, Bitmap>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(CompareActivity.this);
                dialog.setMessage("Loading...");
                dialog.show();
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                String string_intent = (String) getIntent().getExtras().get(AppConstant.CHECKBOX);
                String[] strings = string_intent.split("#");
                for(int i = 0; i < strings.length; i++){
                    try {
                        ExifInterface exif = new ExifInterface(strings[i]);
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                        int angle = 0;

                        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                            angle = 90;
                        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                            angle = 180;
                        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                            angle = 270;
                        }

                        matrix = new Matrix();
                        matrix.postRotate(angle);

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2;
                        bitmap = BitmapFactory.decodeFile(strings[i], options);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        bitmapArrayList.add(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                dialog.dismiss();
                bitmap = mergeMultiple(bitmapArrayList);
                imageView.setImageBitmap(bitmap);
            }
        }.execute();
    }

    private Bitmap mergeMultiple(ArrayList<Bitmap> parts){

        Bitmap result = Bitmap.createBitmap(parts.get(0).getWidth() * 2, parts.get(0).getHeight() * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        for (int i = 0; i < parts.size(); i++) {
            canvas.drawBitmap(parts.get(i), parts.get(i).getWidth() * (i % 2), parts.get(i).getHeight() * ((i / 2)), paint);
        }
        return result;
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.compare_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView txtToolbar = (TextView) findViewById(R.id.txtToolBarCompare);
        txtToolbar.setText("Compare");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_compare, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            intent();
        }

        if(id == R.id.action_save){
            bitmap = mergeMultiple(bitmapArrayList);
            saveBitmap(bitmap);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent();
    }

    private void intent(){
        Intent intent = new Intent(getApplicationContext(), CaldroidSampleActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveBitmap(Bitmap bm){
        File file = Environment.getExternalStorageDirectory();
        File newFile = new File(file, "test.jpg");

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(CompareActivity.this, "Save Bitmap: " + fileOutputStream.toString(),
                    Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(CompareActivity.this, "Something wrong: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(CompareActivity.this, "Something wrong: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }
}
