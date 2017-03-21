package com.example.administrator.calendar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Administrator on 3/20/2017.
 */

public class CompareActivity extends AppCompatActivity {
    Image image;
    Bitmap[] arrBitmap;
    Bitmap bitmap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        ImageView imageView = (ImageView) findViewById(R.id.imgCompare);
        TextView textView = (TextView) findViewById(R.id.txtCompare);
        String string_intent = (String) getIntent().getExtras().get(AppConstant.CHECKBOX);
        String[] arrImg = string_intent.split("#");
        ArrayList<Image> imageArrayList = new ArrayList<>();
        for(int i = 0; i < arrImg.length; i++){
            imageArrayList.add(new Image(arrImg[i]));
        }
        for(int i = 0; i < imageArrayList.size(); i++){
            image = imageArrayList.get(i);
            bitmap = BitmapFactory.decodeFile(image.getUrl());
            arrBitmap = new Bitmap[]{bitmap};

            Toast.makeText(getApplicationContext(), arrBitmap + "", Toast.LENGTH_SHORT).show();
        }
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.ic_check);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.ic_check_select);
        Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(),R.drawable.ic_cancel);
        Bitmap bitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_check_select);



        Bitmap mergedImg= mergeMultiple(arrBitmap);

        imageView.setImageBitmap(mergedImg);

        textView.setText("" + arrBitmap);
    }

    private Bitmap mergeMultiple(Bitmap[] parts){

        Bitmap result = Bitmap.createBitmap(parts[0].getWidth() * 2, parts[0].getHeight() * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        for (int i = 0; i < parts.length; i++) {
            canvas.drawBitmap(parts[i], parts[i].getWidth() * (i % 2), parts[i].getHeight() * (i / 2), paint);
        }
        return result;
    }

    private Bitmap combineImageIntoOne(ArrayList<Bitmap> bitmap) {
        int w = 0, h = 0;
        for (int i = 0; i < bitmap.size(); i++) {
            if (i < bitmap.size() - 1) {
                w = bitmap.get(i).getWidth() > bitmap.get(i + 1).getWidth() ? bitmap.get(i).getWidth() : bitmap.get(i + 1).getWidth();
            }
            h += bitmap.get(i).getHeight();
        }

        Bitmap temp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(temp);
        int top = 0;
        for (int i = 0; i < bitmap.size(); i++) {
            Log.d("HTML", "Combine: "+i+"/"+bitmap.size()+1);

            top = (i == 0 ? 0 : top+bitmap.get(i).getHeight());
            canvas.drawBitmap(bitmap.get(i), 0f, top, null);
        }
        return temp;
    }

}
