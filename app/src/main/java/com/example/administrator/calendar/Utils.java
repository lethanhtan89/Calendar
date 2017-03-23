package com.example.administrator.calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.example.administrator.calendar.images.Image;

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
    public ArrayList<Image> getFilePaths(Date data) {
        ArrayList<Image> filePaths = new ArrayList<Image>();
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
        // check for directory
        if (file.isDirectory()) {
            // getting list of file paths
            File[] listFiles = file.listFiles();

            // Check for count
            if (listFiles.length > 0) {

                // loop through all files
                for (int i = listFiles.length - 1; i >= 0; i--) {
                    // get file path
                    String filePath = null;

                    filePath = listFiles[i].getPath();
                    //date = new Date();
                    Long date = listFiles[i].lastModified();
                    data = new Date(date);
                    // check for supported file extension
                    if (IsSupportedFile(filePath)) {
                        // Add image path to array list
                        filePaths.add(new Image(filePath, data));
                    }
                }
            } else {
                // image directory is empty
                Toast.makeText(_context, AppConstant.IMAGE_DIRECTORY_NAME + " is empty. Please load some images in it !", Toast.LENGTH_LONG).show();
            }

        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(_context);
            alert.setTitle("Error!");
            alert.setMessage(AppConstant.IMAGE_DIRECTORY_NAME + " directory path is not valid! Please set the image directory name AppConstant.java class");
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
}
