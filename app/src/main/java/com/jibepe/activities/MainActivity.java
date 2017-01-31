package com.jibepe.activities;


import android.content.Intent;


import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.jibepe.labo3d.R;
//import com.jibepe.util.DownloadFilesTask;
import com.jibepe.labo3d.TextureHandler;
import com.jibepe.util.DownloadFilesTask;
import com.jibepe.util.DownloadHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements DownloadFragment.DownloadTaskCallbacks {

    private final String TAG = "MainActivity";

    private boolean goon =  false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        String fileName = "scene.mtl"; //"iclauncher.png"; //
//        String fileURL = "http://jb.perin.pagesperso-orange.fr";
//        File folder = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
//        URL url = null;
//        try {
//            url = new URL(fileURL);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        if (url != null) {
//            new pDownloadFilesTask(url, folder).execute(fileName);
//        }
//        while (!goon);
        setContentView(R.layout.activity_main);

        boolean canW = DownloadHelper.isExternalStorageWritable();
        boolean canR = DownloadHelper.isExternalStorageReadable();
        if(canW == canR == true) {

            //File folder = super.getActivity().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            File folder = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
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
        }
//        // Begin the transaction
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        // Replace the contents of the container with the new fragment
//        ft.replace(R.id.render3d_placeholder, (Fragment) new glRenderFragment());
//        // or ft.add(R.id.your_placeholder, new FooFragment());
//        // Complete the changes added above
//        ft.commit();


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
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onProgressUpdate(int percent) {
        Log.d(TAG, "in onProgressUpdate");
    }

    @Override
    public void onPostExecute() {
        Log.d(TAG, "in   onPostExecute\n");

    }
}
