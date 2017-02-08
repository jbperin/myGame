package com.jibepe.activities;



import android.app.Activity;
import android.app.ActivityManager;
import android.support.v4.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.dddviewr.collada.Collada;
import com.jibepe.labo3d.*;
import com.jibepe.model.DaeSceneGraph;
import com.jibepe.model.SceneGraph;
import com.jibepe.objparser.DaeLoader;
import com.jibepe.objparser.ObjLoader;
import com.jibepe.render3d.GLES20Renderer;
import com.jibepe.util.DownloadHelper;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;


public class glRenderFragment extends Fragment {
	
	private final String TAG = "glRenderFragment";

    private GLES20View theView;

    GLES20Renderer mRenderer = null;
    DaeSceneGraph theSceneGraph = null;
    FragmentActivity listener;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_render3d);
//    }


    // This event fires 1st, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
    // This method is called when the fragment is no longer connected to the Activity
    // Any references saved in onAttach should be nulled out here to prevent memory leaks.
    @Override
    public void onDetach() {
        super.onDetach();
    }
    // This method is called after the parent Activity's onCreate() method has completed.
    // Accessing the view hierarchy of the parent activity must be done in the onActivityCreated.
    // At this point, it is safe to search for activity View objects by their ID, for example.
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // This event fires 2nd, before views are created for the fragment
    // The onCreate method is called when the Fragment instance is being created, or re-created.
    // Use onCreate for any standard setup that does not require the activity to be fully created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setRetainInstance(true); // Open GL  context needs to be kept

    }


    /*********************************************/
    /***                PROCESS                 **/
    /*********************************************/
    @Override
    public View  onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //View renderView = inflater.inflate(R.layout.fragment_render3d, container, false);

        FragmentActivity faActivity  = (FragmentActivity)    super.getActivity();

        LinearLayout llLayout    = (LinearLayout)    inflater.inflate(R.layout.fragment_render3d, container, false);


        //DownloadFiles();
        TextureHandler.getInstance().reset();
        TextureHandler.getInstance().setContext(super.getActivity().getApplication());
        ShaderHandler.getInstance().reset();
        ShaderHandler.getInstance().setContext(super.getActivity().getApplication());

        //theSceneGraph = new SceneGraph();
        //File file = new File(Environment.getExternalStoragePublicDirectory(
        //        Environment.DIRECTORY_PICTURES), "scene.mtl");
//        ObjLoader mSceneLoader = new ObjLoader(super.getActivity().getApplication());
//        mSceneLoader.loadModel("scene");
//        theSceneGraph.addObj("scene", mSceneLoader);
//
//        ObjLoader mPersoLoader = new ObjLoader(super.getActivity().getApplication());
//        mPersoLoader.loadModel("plantexture");
//        theSceneGraph.addObj("plantexture", mPersoLoader);

        DaeLoader mCollLoader = new DaeLoader(super.getActivity().getApplication());
        mCollLoader.loadModel("cubebleu.dae");
        theSceneGraph = new DaeSceneGraph(mCollLoader.getTheCollada());

        //theSceneGraph.loadFromDae(mCollLoader);
//        float [] verts = mCollLoader.getVerticesBuffer();
//        float [] norms = mCollLoader.getNormalsBuffer();
//        float [] coords = mCollLoader.getTextureCoordinatesBuffer();
//        short [] faceindex = mCollLoader.getFaceIndexBuffer();

        //try {
            //DownloadHelper.DownloadFile("scene.obj");
            //DownloadHelper.DownloadFile("scene.obj");
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
        final ActivityManager activityManager = (ActivityManager) faActivity.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        if (supportsEs2)
		{
            mRenderer = new GLES20Renderer();
            // Request an OpenGL ES 2.0 compatible context.
            //setEGLContextClientVersion(2);

            // Create an OpenGL ES 2.0 context.
			theView = new GLES20View(faActivity.getApplication(),mRenderer, theSceneGraph);
            theView.setEGLContextClientVersion(2);
            // Set the Renderer for drawing on the GLSurfaceView
            theView.setRenderer(mRenderer);
            theView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

			LinearLayout outerView =
	                (LinearLayout)faActivity.findViewById(R.id.lelayout);
	        View view1 =  faActivity.findViewById(R.id.txt2glsurf);
            llLayout.removeView(view1);
            llLayout.addView(theView,0);
		}
        return (llLayout);
    }


    @Override
    public void onStart() {
    	super.onStart();
    	
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    
    @Override
    public void onDestroy() {
      // Unregister since the activity is about to be closed.
      super.onDestroy();
    }    
  


}
