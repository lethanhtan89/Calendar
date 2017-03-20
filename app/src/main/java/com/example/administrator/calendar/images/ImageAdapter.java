package com.example.administrator.calendar.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.calendar.Image;
import com.example.administrator.calendar.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static com.example.administrator.calendar.R.id.textView;

/**
 * Created by Administrator on 3/17/2017.
 */

public class ImageAdapter extends PagerAdapter {
    public static final String BITAMP = "bitmap";
    //private int[] images = {R.drawable.ic_travel, R.drawable.ic_movie, R.drawable.ic_food, R.drawable.ic_discount};
    private String[] img;
    private Context context;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    String targetPath;
    Map<String, Object> extraData;
    ArrayList<Image> imageArrayList;
    private int imageWidth;
    TextView txtTilte, txtTime;
    Date date;
    Image image;
    public ImageAdapter(Context context, ArrayList<Image> imageArrayList, int imageWidth){
        this.context = context;
        this.imageArrayList = imageArrayList;
        this.imageWidth = imageWidth;
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
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View  view = inflater.inflate(R.layout.item_image, container, false);
        final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        txtTilte = (TextView) view.findViewById(textView);
        txtTime = (TextView) view.findViewById(R.id.txtTime);
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.lnImage);
        //Image image = imageArrayList.get(position);

//        Bitmap image = (Bitmap) getExtraData().get(BITAMP);

        txtTilte.setText("Image: " + position);

        new AsyncTask<String, Void, Bitmap>(){

            @Override
            protected Bitmap doInBackground(String... params) {
                image = imageArrayList.get(position);
                Bitmap bitmap = decodeFile(image.getUrl(), imageWidth, imageWidth);
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                //imageView.setImageBitmap(bitmap);
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
}
