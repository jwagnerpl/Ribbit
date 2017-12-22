package com.teamtreehouse.ribbit.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.teamtreehouse.ribbit.ui.InboxFragment.MEDIA_TYPE_VIDEO;
import static com.teamtreehouse.ribbit.ui.MainActivity.MEDIA_TYPE_IMAGE;

public class OutputMediaFile {

    private static final String TAG = "OutputMediaFile";

    public Uri getOutputMediaFileUri(Context context, int mediaType) {


        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        if (isExternalStorageAvailable()) {
            // get the URI

            // 1. Get the external storage directory
            File mediaStorageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            Log.d(TAG, mediaStorageDir.toString() + " media storage string");

            // 2. Create a unique file name
            String fileName = "";
            String fileType = "";
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            Log.d(TAG, timeStamp.toString() + " media storage string");

            if (mediaType == MEDIA_TYPE_IMAGE) {
                fileName = "IMG_" + timeStamp;
                fileType = ".jpg";
            } else if (mediaType == MEDIA_TYPE_VIDEO) {
                fileName = "VID_" + timeStamp;
                fileType = ".mp4";
            } else {
                return null;
            }

            // 3. Create the file
            File mediaFile;
            try {
                mediaFile = File.createTempFile(fileName, fileType, mediaStorageDir);
                Log.i(TAG, "File: " + Uri.fromFile(mediaFile));

                // 4. Return the file's URI
                return Uri.fromFile(mediaFile);
            } catch (IOException e) {
                Log.e(TAG, "Error creating file: " +
                        mediaStorageDir.getAbsolutePath() + fileName + fileType);
            }
        }

        // something went wrong
        Log.d(TAG, "error & returning null");
        return null;
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();

        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            Log.d(TAG, Environment.getExternalStorageState().toString());
            return false;
        }
    }
}

