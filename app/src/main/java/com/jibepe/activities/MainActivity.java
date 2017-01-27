package com.jibepe.activities;



import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import com.jibepe.labo3d.*;
import com.jibepe.model.SceneGraph;
import com.jibepe.objparser.ObjLoader;
import com.jibepe.render3d.GLES20Renderer;
import com.jibepe.util.DownloadFilesTask;
import com.jibepe.util.DownloadHelper;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
	
	private final String TAG = "MainActivity";

    private GLES20View theView;

    GLES20Renderer mRenderer = null;
    SceneGraph theSceneGraph = null;

    /*********************************************/
    /***                PROCESS                 **/
    /*********************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DownloadFiles();
        TextureHandler.getInstance().reset();
        TextureHandler.getInstance().setContext(getApplication());
        ShaderHandler.getInstance().reset();
        ShaderHandler.getInstance().setContext(getApplication());

        theSceneGraph = new SceneGraph();
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
            mRenderer = new GLES20Renderer();
            // Request an OpenGL ES 2.0 compatible context.
            //setEGLContextClientVersion(2);

            // Create an OpenGL ES 2.0 context.
			theView = new GLES20View(getApplication(),mRenderer, theSceneGraph);
            theView.setEGLContextClientVersion(2);
            // Set the Renderer for drawing on the GLSurfaceView
            theView.setRenderer(mRenderer);
            theView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

			RelativeLayout outerView = 
	                (RelativeLayout)this.findViewById(R.id.lelayout);
	        View view1 =  this.findViewById(R.id.txt2glsurf);
	        outerView.removeView(view1);
	        outerView.addView(theView,0);
		}

    }

    private void DownloadFiles() {
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




}
