package com.jibepe.model;

import com.jibepe.objparser.ObjLoader;

import java.util.List;

/**
 * Created by tbpk7658 on 08/02/2017.
 */
public interface InterfaceSceneGraph {

    public List<float[]> getLightsPos();

    public float[] getCamPos();
    public float[] getCamRot();

    public float[] getCamMatrix();

    public void rotateCam (float angle );
    public void moveCam (float angle );

    public List<InterfaceSceneObject> getSceneObjects();

    public void addObj (String name, ObjLoader obj);

    public ObjLoader getObj (String objectName);

}
