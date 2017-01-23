package com.jibepe.labo3d;

import java.util.List;

/**
 * Created by tbpk7658 on 23/01/2017.
 */
public interface InterfaceSceneRenderer {


    float [] getCamPos();
    float [] getCamRot();
    List<float[]>  getLightsPos ();
    List<float[]> getRenderableShapes();
}
