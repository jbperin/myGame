package com.jibepe.labo3d;

import android.os.SystemClock;

import java.util.List;

/**
 * Created by tbpk7658 on 23/01/2017.
 */
public class SceneContentProvider implements InterfaceSceneRenderer{

    SceneGraph mSceneGraph;

    public SceneContentProvider (SceneGraph sg) {
        mSceneGraph = sg;
    }
    @Override
    public float[] getCamPos() {
        return mSceneGraph.getCamPos();
    }

    @Override
    public float[] getCamRot() {
        return mSceneGraph.getCamRot();
    }

    @Override
    public List<float[]> getLightsPos() {
        return mSceneGraph.getLightsPos();
    }

    @Override
    public List<float[]> getRenderableShapes() {
        return null;
    }


}
