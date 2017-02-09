package com.jibepe.model;

import com.jibepe.render3d.glRenderableShape;

import java.util.*;

/**
 * Created by tbpk7658 on 09/02/2017.
 */
public class SceneObject implements  InterfaceSceneObject{

    public void setName(String name) {
        this.name = name;
    }

    public void setGlMatrix(float[] matrix) {
        this.matrix = matrix;
    }

    public String name;

    @Override
    public String getName (){return name;}

    public float[] matrix = null;

    public float [] getGlMatrix (){
        if (matrix == null) {
            matrix = new float[]{
                  1.0f, 0.0f, 0.0f, 0.0f
                , 0.0f, 1.0f, 0.0f, 0.0f
                , 0.0f, 0.0f, 1.0f, 0.0f
                , 0.0f, 0.0f, 0.0f, 1.0f};
        }
        return matrix;
    }

    public List <InterfaceMesh> meshes = new  ArrayList<InterfaceMesh>();;
    public List <InterfaceMesh> getMeshes (){return meshes;}
    public void addMeshes (InterfaceMesh nMesh){meshes.add(nMesh);}

    public InterfaceSceneObject parent = null;
    public InterfaceSceneObject getParent (){return parent;}
    public void setParent(InterfaceSceneObject parent){parent = parent;}

    public List<InterfaceSceneObject> childs = new  ArrayList<InterfaceSceneObject>();
    public List<InterfaceSceneObject> getChilds (){return childs;}
    public void addChild (InterfaceSceneObject child){childs.add(child);}


}
