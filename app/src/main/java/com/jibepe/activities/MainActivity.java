package com.jibepe.activities;



import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import com.jibepe.labo3d.*;
import com.jibepe.objparser.ObjLoader;
import com.jibepe.util.DownloadFilesTask;
import com.jibepe.util.DownloadHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
	
	private final String TAG = "MainActivity";

    /*********************************************/
    /***                PROCESS                 **/
    /*********************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String DownloadUrl = "http://jb.perin.pagesperso-orange.fr/";
        String fileName = "scene.mtl";

        boolean canW = DownloadHelper.isExternalStorageWritable();
        boolean canR = DownloadHelper.isExternalStorageReadable();
        if(canW == canR == true) {

            File folder = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            //DownloadHelper.verifyStoragePermissions(this);
            String FullUrl = DownloadUrl;
            URL url = null;
            try {
                url = new URL(FullUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            if (url != null) {
                new DownloadFilesTask(url, folder).execute(fileName);
            }
        }

        TextureHandler.getInstance().setContext(getApplication());
        ShaderHandler.getInstance().setContext(getApplication());

        SceneGraph theSceneGraph = new SceneGraph();
        //File file = new File(Environment.getExternalStoragePublicDirectory(
        //        Environment.DIRECTORY_PICTURES), "scene.mtl");
        ObjLoader mSceneLoader = new ObjLoader(getApplication());
        mSceneLoader.loadModel("scene");
        theSceneGraph.addObj("scene", mSceneLoader);

        ObjLoader mPersoLoader = new ObjLoader(getApplication());
        mPersoLoader.loadModel("plantexture");
        theSceneGraph.addObj("plantexture", mPersoLoader);

        //try {
            //DownloadHelper.DownloadFile("scene.obj");
            //DownloadHelper.DownloadFile("scene.obj");
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2)
		{
			// Create an OpenGL ES 2.0 context.
			theView = new GLES20View(getApplication(), theSceneGraph);
			
			RelativeLayout outerView = 
	                (RelativeLayout)this.findViewById(R.id.lelayout);
	        View view1 =  this.findViewById(R.id.txt2glsurf);
	        outerView.removeView(view1);
	        outerView.addView(theView,0);
		}

    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    
    @Override
    protected void onDestroy() {
      // Unregister since the activity is about to be closed.
      super.onDestroy();
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


	private GLES20View theView;


}
