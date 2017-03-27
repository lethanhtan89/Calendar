package com.example.administrator.calendar.images;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.calendar.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.example.administrator.calendar.R.id.textView;

/**
 * Created by Administrator on 3/17/2017.
 */

public class ImageAdapter extends PagerAdapter implements View.OnClickListener{
    private Context context;
    ArrayList<Image> imageArrayList;
    TextView txtTilte, txtTime, txtCancel;
    CheckBox checkBox;
    ImageView imageView;
    Image image;
    final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
    ImageListener imageListener;
    public ImageAdapter(Context context, ArrayList<Image> imageArrayList, ImageListener imageListener){
        this.context = context;
        this.imageArrayList = imageArrayList;
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
        imageView = (ImageView) view.findViewById(R.id.imageView);
        txtCancel = (TextView) view.findViewById(R.id.txtCancel);

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

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageListener != null){
                    imageListener.onRemoveImage(position);
                }
            }
        });

        image = imageArrayList.get(position);
        txtTilte.setText("Image: " + position);
        Glide.with(context).load(image.getUrl()).centerCrop().into(imageView);
        txtTime.setText(formatter.format(image.getDate()) + "");

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.refreshDrawableState();
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

     public interface ImageListener{
         void onCickCheckbox(int position);
         void onRemoveImage(int position);
     }

}