package com.example.administrator.calendar.grid;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.calendar.AppConstant;
import com.example.administrator.calendar.R;
import com.example.administrator.calendar.images.SliderActivity;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

@SuppressLint("SimpleDateFormat")
public class CaldroidSampleActivity extends AppCompatActivity implements View.OnClickListener{
    private CaldroidFragment caldroidFragment;

    private static final int IMAGE_GALLERY = 10000;
    private static final int IMAGE_CAMERA = 10001;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final String BITAMP = "bitmap";
    private Uri uriCameraImage;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    private TextView txtPicture, txtUpload, txtCompare;
    Bitmap bitmap;
    Map<String, Object> extraData;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private ProgressDialog dialog;

    private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();

        // Min date is last 7 days
        cal.add(Calendar.DATE, -7);
        Date blueDate = cal.getTime();

        // Max date is next 7 days
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        Date greenDate = cal.getTime();

        if (caldroidFragment != null) {
//            ColorDrawable blue = new ColorDrawable(Color.BLACK);
//            ColorDrawable green = new ColorDrawable(Color.RED);
//            caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
//            caldroidFragment.setBackgroundDrawableForDate(green, greenDate);
//            caldroidFragment.setTextColorForDate(R.color.white, blueDate);
//            caldroidFragment.setTextColorForDate(R.color.white, greenDate);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        setupToolbar();
        init();
        setupCalendar();
        sharedPreferences = getSharedPreferences(AppConstant.PRE, Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
        //caldroidFragment = new CaldroidFragment();

        // //////////////////////////////////////////////////////////////////////
        // **** This is to show customized fragment. If you want customized
        // version, uncomment below line ****
		 caldroidFragment = new CaldroidSampleCustomFragment();

        // Setup arguments

        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState, "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            // Uncomment this to customize startDayOfWeek
            // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
            // CaldroidFragment.TUESDAY); // Tuesday

            // Uncomment this line to use Caldroid in compact mode
            // args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

            // Uncomment this line to use dark theme
//            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);

            caldroidFragment.setArguments(args);
        }

        setCustomResourceForDates();

        // Attach to the activity
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                Toast.makeText(getApplicationContext(), formatter.format(date), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SliderActivity.class);
                intent.putExtra(AppConstant.DATE, date);
                startActivity(intent);
            }

            @Override
            public void onChangeMonth(int month, int year) {
            }

            @Override
            public void onLongClickDate(Date date, View view) {
            }

            @Override
            public void onCaldroidViewCreated() {
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);
        final Bundle state = savedInstanceState;
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
        txtCalendar.setText("PXP CALENDAR");

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
                Toast.makeText(getApplicationContext(), uriCameraImage + "", Toast.LENGTH_SHORT).show();
                break;
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

    public static File getOutputMediaFile(int type) {
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
        new AsyncTask<String, Void, Bitmap>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(CaldroidSampleActivity.this);
                dialog.setMessage("Loading...");
                dialog.show();
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                try {
                    ExifInterface exif = new ExifInterface(uriCameraImage.getPath());
                    int orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);

                    int angle = 0;

                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                        angle = 90;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                        angle = 180;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                        angle = 270;
                    }

                    Matrix matrix = new Matrix();
                    matrix.postRotate(angle);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // downsizing image as it throws OutOfMemory Exception for larger
                    // images

                    options.inSampleSize = 4;
                    bitmap = BitmapFactory.decodeFile(uriCameraImage.getPath(), options);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                dialog.dismiss();
                SharePreference sharePreference = new SharePreference(getApplicationContext());
                sharePreference.setImagetoPre(bitmap);
                extraData = caldroidFragment.getExtraData();
                extraData.put(BITAMP, bitmap);
                caldroidFragment.refreshView();
            }
        }.execute();

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
            else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == IMAGE_GALLERY){
            if(resultCode == RESULT_OK){
                InputStream inputStream = null;
                try {
                    inputStream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                extraData = caldroidFragment.getExtraData();
                extraData.put(BITAMP, bitmap);
                caldroidFragment.refreshView();
                //txtCompare.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
                Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void upload(){
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, IMAGE_GALLERY);
    }

    private void setupCalendar(){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String date = year + "/" + month + "/" + day;
    }

    /**
     * Save current states of the Caldroid here
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }
    }
}
