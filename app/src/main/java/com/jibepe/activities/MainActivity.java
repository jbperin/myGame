package com.jibepe.activities;


import android.content.Context;
import android.content.Intent;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.jibepe.labo3d.R;
//import com.jibepe.util.DownloadFilesTask;
import com.jibepe.labo3d.SceneContentProvider;
import com.jibepe.model.SceneGraph;
import com.jibepe.recorder.GameRecorder;
import com.jibepe.util.DownloadHelper;
import com.jibepe.recorder.glRecorder;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements DownloadFragment.DownloadTaskCallbacks {

    private final String TAG = "MainActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.setProperty("org.xml.sax.driver","org.xmlpull.v1.sax2.Driver");
        setContentView(R.layout.activity_main);

        //File folder = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

        //startDownloadFragment(folder);
        //startRecordFragment(folder);




        //GameRecorder.getInstance().prepareEncoder(this);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void startRecordFragment(File folder) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        RecordFragment fragmentRecord = RecordFragment.newInstance(folder, getApplicationContext());
        ft.replace(R.id.recorder_placeholder, fragmentRecord);
        ft.commit();
    }

    private void startDownloadFragment(File folder) {
        boolean canW = DownloadHelper.isExternalStorageWritable();
        boolean canR = DownloadHelper.isExternalStorageReadable();
         if((canW == canR == true) && (isNetworkAvailable())) {

            //File folder = super.getActivity().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

            //DownloadHelper.verifyStoragePermissions(this);
            String FullUrl = "http://jb.perin.pagesperso-orange.fr/";
            URL url = null;
            try {
                url = new URL(FullUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            if (url != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                DownloadFragment fragmentDownload = DownloadFragment.newInstance(url, folder);
                ft.replace(R.id.download3d_placeholder, fragmentDownload);
                ft.commit();
                //new DownloadFilesTask(url, folder).execute(fileName);
            }
        } else {
             CharSequence text = "";
             if (!(canW == canR == true))
             {
                 text = text + "No store media available.";
             }
             if (!isNetworkAvailable()) {
                 text = text + " network not reachable.";
             }
             Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);

             toast.show();
         }

    }


    /*********************************************/
    /***                GUI                     **/
    /*********************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), ConfigActivity.class));
            return true;
        } else if (id == R.id.action_record) {
            startRecordFragment(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
            return true;
        } else if (id == R.id.action_update) {
            startDownloadFragment(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onProgressUpdate(int percent) {
        Log.d(TAG, "in onProgressUpdate = " + percent + "% ");
    }

    @Override
    public void onPostExecute() {
        Log.d(TAG, "in   onPostExecute\n");

        CharSequence text = "Download Complete !!";

        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);

        toast.show();
    }
}
