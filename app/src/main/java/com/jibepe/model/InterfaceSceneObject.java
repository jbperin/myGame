package com.jibepe.model;

import java.util.List;

/**
 * Created by tbpk7658 on 09/02/2017.
 */
public interface InterfaceSceneObject {

    String getName ();

    float [] getGlMatrix ();

    List <InterfaceMesh> getMeshes();

    InterfaceSceneObject getParent ();

    List<InterfaceSceneObject> getChilds ();

}
