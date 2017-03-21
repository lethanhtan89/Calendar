package com.example.administrator.calendar.images;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.calendar.Image;
import com.example.administrator.calendar.R;
import com.example.administrator.calendar.grid.CaldroidSampleActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static com.example.administrator.calendar.R.id.textView;

/**
 * Created by Administrator on 3/17/2017.
 */

public class ImageAdapter extends PagerAdapter implements View.OnClickListener{
    public static final String BITAMP = "bitmap";
    private Context context;
    Map<String, Object> extraData;
    ArrayList<Image> imageArrayList;
    private int imageWidth;
    TextView txtTilte, txtTime, txtCheck, txtCancel;
    CheckBox checkBox;
    Date date;
    Image image;
    Bitmap bitmap;
    ImageListener imageListener;
    public ImageAdapter(Context context, ArrayList<Image> imageArrayList, int imageWidth, ImageListener imageListener){
        this.context = context;
        this.imageArrayList = imageArrayList;
        this.imageWidth = imageWidth;
        this.imageListener = imageListener;
    }
    @Override
    public int getCount() {
        return imageArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (LinearLayout) object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View  view = inflater.inflate(R.layout.item_image, container, false);
        txtTilte = (TextView) view.findViewById(textView);
        txtTime = (TextView) view.findViewById(R.id.txtTime);
        checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        txtCancel = (TextView) view.findViewById(R.id.txtCancel);
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.lnImage);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    if(imageListener!= null){
                        imageListener.onCickCheckbox(position);
                    }
                }
            }
        });


        txtTilte.setText("Image: " + position);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeImage(position);
            }
        });


        new AsyncTask<String, Void, Bitmap>(){

            @Override
            protected Bitmap doInBackground(String... params) {

                image = imageArrayList.get(position);
                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(image.getUrl());
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
                    bitmap = decodeFile(image.getUrl(), imageWidth, imageWidth);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                linearLayout.setBackgroundDrawable(new BitmapDrawable(context.getResources(), bitmap));
                final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                txtTime.setText("" + formatter.format(image.getDate()));
            }
        }.execute();


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.refreshDrawableState();
    }

    public Map<String, Object> getExtraData() {
        return extraData;
    }

    public static Bitmap decodeFile(String filePath, int WIDTH, int HIGHT) {
        try {

            File f = new File(filePath);

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            final int REQUIRED_WIDTH = WIDTH;
            final int REQUIRED_HIGHT = HIGHT;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_WIDTH
                    && o.outHeight / scale / 2 >= REQUIRED_HIGHT)
                scale *= 2;

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
        }
    }

    public void setCheckBox(int position){
        Image image = imageArrayList.get(position);
        image.setSelect(!image.isSelect());
        notifyDataSetChanged();
    }

    public ArrayList<Image> getAllData(){
        return imageArrayList;
    }

    private void removeImage(int position){
        image = imageArrayList.remove(position);
        Intent intent = new Intent(context, CaldroidSampleActivity.class);
        context.startActivity(intent);
        Toast.makeText(context,"Removed", Toast.LENGTH_SHORT).show();
    }
     public interface ImageListener{
         void onCickCheckbox(int position);
     }
}