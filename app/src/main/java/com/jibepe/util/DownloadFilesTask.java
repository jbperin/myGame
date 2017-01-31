package com.jibepe.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.net.URL;

/**
 * Created by Famille PERIN on 21/01/2017.
 */

public class DownloadFilesTask extends AsyncTask<String, Integer, Long> {
    private String TAG = "DownloadFilesTask";
    private URL base_url;
    private File target_folder;

    public DownloadFilesTask (URL url, File folder) {
        super();
        base_url = url;
        target_folder = folder;
    }

    protected Long doInBackground(String... urls) {
        int count = urls.length;
        long totalSize = 0;
        for (int i = 0; i < count; i++) {
            totalSize += DownloadHelper.DownloadFile(base_url, target_folder, urls[i]);
            if (isCancelled()) break;
        }
        return totalSize;
    }

    protected void onProgressUpdate(Integer... progress) {
        Log.d(TAG, "onProgressUpdate");
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Long result) {
        Log.d(TAG, "onPostExecute");
        //showDialog("Downloaded " + result + " bytes");
    }
}