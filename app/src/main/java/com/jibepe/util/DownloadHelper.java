package com.jibepe.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Famille PERIN on 21/01/2017.
 */
public class DownloadHelper {

    private static final String TAG = "DownloadHelper";

    private static final int TIMEOUT_CONNECTION = 1000;
    private static final int TIMEOUT_SOCKET = 1000;



    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static long DownloadFile (URL url, File folder, String fileName)  {

        boolean success = false;
        long totalSize = 0;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        String destinationDir = folder.getAbsolutePath();
        //String fileName = "scene.mtl";
        File outFile = new File(destinationDir, fileName);

        try {
            String newline = "\r\n";
            FileOutputStream fos = new FileOutputStream(outFile);

            URLConnection uconn = null;
            uconn = url.openConnection();
            uconn.setReadTimeout(TIMEOUT_CONNECTION);
            uconn.setConnectTimeout(TIMEOUT_SOCKET);
            InputStream is = null;
            try {
                is = uconn.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedInputStream bufferinstream = new BufferedInputStream(is);

            byte[] buffer = new byte[4 * 1024];
            int count;

            try {
                while ((count = bufferinstream.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                    totalSize += count;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            fos.flush();
            fos.close();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (success == true)  return totalSize;
        else return 0;
    }




}
