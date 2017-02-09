package com.jibepe.model;

import com.jibepe.objparser.DaeLoader;
import com.jibepe.objparser.MaterialShape;
import com.jibepe.objparser.ObjLoader;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by tbpk7658 on 23/01/2017.
 */
public class SceneGraph implements InterfaceSceneGraph{


    public Dictionary<String, ObjLoader> objectDictionary;
    public Dictionary<String, DaeLoader> colladaDictionary;

    private float camPos[] = {1.0f, 1.0f, -1.0f};
    private float camRot[] = {0.0f, -90.0f, 0.0f};

    public SceneGraph (){
        objectDictionary = new Hashtable<String, ObjLoader>();
        colladaDictionary = new Hashtable<String, DaeLoader>();
        //lSceneObjects.add();

    }
    @Override
    public float[] getCamMatrix() {
//        return new float[] {
//                0.6859207f, -0.3240135f, 0.6515582f, 7.481132f,
//                0.7276763f, 0.3054208f, -0.6141704f, -6.50764f,
//                0.0f, 0.8953956f, 0.4452714f, 5.343665f,
//                0.0f, 0.0f, 0.0f, 1.0f
//        };
        return new float [] {
                1.0f, 0.0f, 0.0f, 0.0f
                , 0.0f, 1.0f, 0.0f, 0.0f
                , 0.0f, 0.0f, 1.0f, 0.0f
                , 0.0f, 0.0f, 0.0f, 1.0f
        };
    }

    @Override
    public float [] getCamPos(){
        return camPos;
    }
    @Override
    public float[] getCamRot() {
        return camRot;
    }
    @Override
    public void rotateCam(float f) {
        camRot[1] += f;
    }
    @Override
    public void moveCam(float f) {
        camPos[0] += f*Math.cos(Math.toRadians(camRot[1]));
        camPos[2] += f*Math.sin(Math.toRadians(camRot[1]));
    }

    List<InterfaceSceneObject> lSceneObjects ;

    @Override
    public List<InterfaceSceneObject> getSceneObjects() {
        return (lSceneObjects);

        //        glColoredShape shapeScene = new glColoredShape(mSceneGraph.getObj("scene"));
//        shapeScene.setPosition (new float [] {0.0f, 0.0f, 0.0f});
//        shapeScene.setRotation (new float [] {0.0f, 0.0f, 0.0f});
//
//        shapes2Render.add(shapeScene);

//        glAlphaTexturedShape shapePerso = new glAlphaTexturedShape(mSceneGraph.getObj("plantexture"));
//        shapePerso.setName("PlanTexture.001");
//
//        shapePerso.setPosition (new float [] {1.0f, 1.0f, 0.0f});
//        shapePerso.setRotation (new float [] {0.0f, 90.0f, 0.0f});
//
//        shapes2Render.add(shapePerso);

    }


    public void addObj (String name, ObjLoader obj) {
        objectDictionary.put(name, obj);
    }
    public void addDae (String name, DaeLoader dae) {
        colladaDictionary.put(name, dae);
    }

    public ObjLoader getObj (String objectName) {
        return objectDictionary.get(objectName);
    }
    public DaeLoader getDae (String objectName) {
        return colladaDictionary.get(objectName);
    }
    @Override
    public List<float[]> getLightsPos() {
//        // Do a complete rotation every 10 seconds.
//        long time = SystemClock.uptimeMillis() % 10000L;
//        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
        ArrayList<float[]> al = new ArrayList<float[]>();
        al.add(new float[] {1.0f, 1.0f, 1.0f});
        return (al);
    }

}
