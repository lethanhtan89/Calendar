package com.example.administrator.calendar.images;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.calendar.AppConstant;
import com.example.administrator.calendar.CompareActivity;
import com.example.administrator.calendar.R;
import com.example.administrator.calendar.Utils;
import com.example.administrator.calendar.grid.CaldroidSampleActivity;
import com.example.administrator.calendar.grid.SharePreference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Administrator on 3/17/2017.
 */

public class SliderActivity extends AppCompatActivity implements View.OnClickListener, ImageAdapter.ImageListener, ViewPager.OnPageChangeListener{
    ViewPager viewPager;
    ImageAdapter adapter;
    TextView textView;
    Utils utils;
    private TextView[] dots;
    private LinearLayout dotsLayout;

    ArrayList<Image> imageArrayList;
    SharePreference sharePreference;
    SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        setupToolbar();
        init();
        sharePreference = new SharePreference(getApplicationContext());
        dateFormat = new SimpleDateFormat("yyyyMMdd");
    }

    private void init(){
        utils = new Utils(this);
//        Date date = (Date) getIntent().getExtras().get(AppConstant.DATE);

       // imageArrayList = utils.getFilePaths(date);
        imageArrayList = (ArrayList<Image>) getIntent().getExtras().get(AppConstant.DATE);
//        imageArrayList = new ArrayList<>();
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        addBottomDots(0);
        adapter = new ImageAdapter(this, imageArrayList, 1024, this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
    }

    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.slider_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");
        textView = (TextView) findViewById(R.id.txtToolBarImage);
        textView.setText("Watch & Compare");
        textView.setOnClickListener(this);


    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[imageArrayList.size()];

        dotsLayout.removeAllViews();
        for (int i = 0; i < imageArrayList.size(); i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.colorPrimary));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }



    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_slider, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_cancel)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.txtToolBarImage:
                String images = "";
                for(Image hold: adapter.getAllData()){
                    if(hold.isSelect()){
                        images += hold.getUrl() + "#";
                    }
                }

                Intent intent = new Intent(getApplicationContext(), CompareActivity.class);
                intent.putExtra(AppConstant.CHECKBOX, images);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onCickCheckbox(int position) {
        adapter.setCheckBox(position);
    }

    @Override
    public void onRemoveImage(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(SliderActivity.this);
        builder.setTitle("Delete");
        builder.setMessage("Would you like to delete this image ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                remove(position);
                Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        addBottomDots(position);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void remove(int position){
        for(position = imageArrayList.size() - 1; position >= 0; position--) {
            imageArrayList.remove(imageArrayList.get(position));
            adapter.notifyDataSetChanged();
        }
        Intent intent = new Intent(getApplicationContext(), CaldroidSampleActivity.class);
        startActivity(intent);
    }
}
