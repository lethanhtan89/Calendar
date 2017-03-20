package com.example.administrator.calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 3/10/2017.
 */

public class Utils {

    private Context _context;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";

    // constructor
    public Utils(Context context) {
        this._context = context;
    }

    // Reading file paths from SDCard
    public ArrayList<Image> getFilePaths(Date date) {
        ArrayList<Image> filePaths = new ArrayList<Image>();

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

        // check for directory
        if (file.isDirectory()) {
            // getting list of file paths
            File[] listFiles = file.listFiles();

            // Check for count
            if (listFiles.length > 0) {

                // loop through all files
                for (int i = 0; i < listFiles.length; i++) {

                    // get file path
                    String filePath = null;
                    String datetime = null;

                    filePath = listFiles[i].getPath();
                    date = new Date();
                    // check for supported file extension
                    if (IsSupportedFile(filePath)) {
                        // Add image path to array list
                        filePaths.add(new Image(filePath, date));
                    }
                }
            } else {
                // image directory is empty
                Toast.makeText(
                        _context,
                        AppConstant.IMAGE_DIRECTORY_NAME
                                + " is empty. Please load some images in it !",
                        Toast.LENGTH_LONG).show();
            }

        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(_context);
            alert.setTitle("Error!");
            alert.setMessage(AppConstant.IMAGE_DIRECTORY_NAME
                    + " directory path is not valid! Please set the image directory name AppConstant.java class");
            alert.setPositiveButton("OK", null);
            alert.show();
        }

        return filePaths;
    }

    // Check supported file extensions
    private boolean IsSupportedFile(String filePath) {
        String ext = filePath.substring((filePath.lastIndexOf(".") + 1), filePath.length());

        if (AppConstant.FILE_EXTN.contains(ext.toLowerCase(Locale.getDefault())))
            return true;
        else
            return false;

    }

    /*
     * getting screen width
     */
    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) _context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }
}
