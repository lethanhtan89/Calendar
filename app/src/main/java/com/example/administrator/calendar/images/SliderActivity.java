package com.example.administrator.calendar.images;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.calendar.AppConstant;
import com.example.administrator.calendar.Image;
import com.example.administrator.calendar.R;
import com.example.administrator.calendar.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 3/17/2017.
 */

public class SliderActivity extends AppCompatActivity implements View.OnClickListener, ImageAdapter.ImageListener{
    ViewPager viewPager;
    ImageAdapter adapter;
    TextView textView;
    Bitmap bitmap;
    File file;
    Utils utils;
    private TextView[] dots;
    private LinearLayout dotsLayout;

    private Uri uriCameraImage;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    String targetPath;
    public static final String BITAMP = "bitmap";
    private int columnWidth;
    ArrayList<Image> imageArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        setupToolbar();
        init();
    }

    private void init(){
        utils = new Utils(this);

        //addBottomDots(0);
        Date date = (Date) getIntent().getExtras().get(AppConstant.DATE);

        imageArrayList = utils.getFilePaths(date);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        //dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        adapter = new ImageAdapter(this, imageArrayList, 1024, this);
        viewPager.setAdapter(adapter);
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

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < imageArrayList.size(); i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
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
                Bitmap bitmap;
                for(Image hold: adapter.getAllData()){
                    if(hold.isSelect()){
                        images += hold.getUrl() + "#";

                    }
                }
                bitmap = BitmapFactory.decodeFile(images);
                Toast.makeText(getApplicationContext(), "" + images, Toast.LENGTH_SHORT).show();

//                Intent intent = new Intent(getApplicationContext(), CompareActivity.class);
//                intent.putExtra(AppConstant.CHECKBOX, images);
//                startActivity(intent);

        }
    }


    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                AppConstant.GRID_PADDING, r.getDisplayMetrics());

        columnWidth = (int) ((utils.getScreenWidth() - ((AppConstant.NUM_OF_COLUMNS + 1) * padding)) / AppConstant.NUM_OF_COLUMNS);


    }

    @Override
    public void onCickCheckbox(int position) {

        adapter.setCheckBox(position);
    }
}
