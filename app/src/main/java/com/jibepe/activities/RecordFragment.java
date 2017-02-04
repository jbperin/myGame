package com.jibepe.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import com.jibepe.labo3d.SceneContentProvider;
import com.jibepe.model.SceneGraph;
import com.jibepe.recorder.glRecorder;

import java.io.File;
import java.net.URL;

/**
 * Created by Famille PERIN on 04/02/2017.
 */
public class RecordFragment extends Fragment {

    private final String TAG = "glRenderFragment";


    public static RecordFragment newInstance(File folder, Context context) {
        RecordFragment myFragment = new RecordFragment();

        //Bundle args = new Bundle();
        //args.putString("url", url.toString());
        //args.putString("folder", folder.toString());
        //myFragment.setArguments(args);

        SceneGraph mScene = null;
        SceneContentProvider mSceneView = null;
        SceneGraph theSceneGraph = new SceneGraph();

        mScene = theSceneGraph;
        mSceneView = new SceneContentProvider(mScene);

        glRecorder theRecorder = new glRecorder(folder, mSceneView, context);
        theRecorder.testEncodeVideoToMp4();

        return myFragment;
    }

    /**
     * This method will only be called once when the retained
     * Fragment is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Code here
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Code here
        }
    }
}