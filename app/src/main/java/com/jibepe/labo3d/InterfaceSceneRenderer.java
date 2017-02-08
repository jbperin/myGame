package com.jibepe.labo3d;

import com.jibepe.render3d.glRenderableShape;

import java.util.List;

/**
 * Created by tbpk7658 on 23/01/2017.
 */
public interface InterfaceSceneRenderer {


    float [] getCamPos();
    float [] getCamRot();
    float [] getCamMatrix();
    List<float[]>  getLightsPos ();
    List<glRenderableShape> getRenderableShapes();
}
