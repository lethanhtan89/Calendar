package com.example.administrator.calendar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 3/9/2017.
 */

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG = CalendarActivity.class.getSimpleName();
    private static final int IMAGE_GALLERY = 10000;
    private static final int IMAGE_CAMERA = 10001;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri uriCameraImage;
    private MaterialCalendarView calendarView;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    TextView txtPicture, txtUpload, txtCompare;
    ImageView imgLoad;
    private  Drawable highlightDrawable;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        setupToolbar();
        init();
        setupCalendar();
    }

    private void init(){
        txtPicture = (TextView) findViewById(R.id.txtPicture);
        txtUpload = (TextView) findViewById(R.id.txtUpload);
        //txtCompare = (TextView) findViewById(R.id.txtCompare);
        imgLoad = (ImageView) findViewById(R.id.txtCompare);
        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        calendarView.setSelectedDate(Calendar.getInstance());
        calendarView.state().edit().setFirstDayOfWeek(Calendar.MONDAY).commit();
        txtPicture.setOnClickListener(this);
        txtUpload.setOnClickListener(this);
        imgLoad.setOnClickListener(this);
        //txtCompare.setOnClickListener(this);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Toast.makeText(getApplicationContext(), date + "", Toast.LENGTH_SHORT).show();
                //upload();

            }
        });
//        HashSet<Date> dateHashSet = new HashSet<>();
//        dateHashSet.add(new Date());
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.calendar_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        TextView txtCalendar = (TextView) findViewById(R.id.txtToolBarCalendar);
        txtCalendar.setText("PXP CALENDAR");

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
                captureImage();
                break;
            case R.id.txtUpload:
                upload();
                break;
            case R.id.txtCompare:
                upload();
                break;
            case R.id.calendarView:
                Toast.makeText(getApplicationContext(), "Calendar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CAMERA) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            }
            else if(requestCode == IMAGE_GALLERY && resultCode == RESULT_OK){
                previewCapturedImage();
            }
            else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        uriCameraImage = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriCameraImage);
        // start the image capture Intent
        startActivityForResult(intent, IMAGE_CAMERA);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        }
        else {
            return null;
        }
        return mediaFile;
    }

    private void previewCapturedImage() {
        try {
//            HashMap<Integer, ArrayList<Image>> hashMap = new HashMap<>();
//            ArrayList<Image> imageArrayList = new ArrayList<>();
//            imageArrayList.add(new Image());
//            hashMap.put(15, imageArrayList);
            // hide video preview
            imgLoad.setVisibility(View.VISIBLE);
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();
            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;
            final Bitmap bitmap = BitmapFactory.decodeFile(uriCameraImage.getPath(), options);
            final Calendar calendar = Calendar.getInstance();
            calendarView.addDecorator(new DayViewDecorator() {
                @Override
                public boolean shouldDecorate(CalendarDay day) {
                    return true;
                }

                @Override
                public void decorate(DayViewFacade view) {
                    //view.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_noti));
                    //view.addSpan(new ForegroundColorSpan(Color.RED));

                }
            });



            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageBitmap(bitmap);
            calendarView.addView(imageView, calendarView.getSelectionColor());
            imgLoad.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void upload(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, IMAGE_GALLERY);
    }

    private void setupCalendar(){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = year + "/" + month + "/" + day;
    }
}
