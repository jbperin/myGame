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
public class SceneGraph {


    public Dictionary<String, ObjLoader> objectDictionary;
    public Dictionary<String, DaeLoader> colladaDictionary;

    private float camPos[] = {1.0f, 1.0f, -1.0f};
    private float camRot[] = {0.0f, -90.0f, 0.0f};

    public SceneGraph (){
        objectDictionary = new Hashtable<String, ObjLoader>();
        colladaDictionary = new Hashtable<String, DaeLoader>();

    }
    public float [] getCamPos(){
        return camPos;
    }
    public float[] getCamRot() {
        return camRot;
    }

    public void rotateCam(float f) {
        camRot[1] += f;
    }

    public void moveCam(float f) {
        camPos[0] += f*Math.cos(Math.toRadians(camRot[1]));
        camPos[2] += f*Math.sin(Math.toRadians(camRot[1]));
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

    public List<float[]> getLightsPos() {
//        // Do a complete rotation every 10 seconds.
//        long time = SystemClock.uptimeMillis() % 10000L;
//        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
        ArrayList<float[]> al = new ArrayList<float[]>();
        al.add(new float[] {1.0f, 1.0f, 1.0f});
        return (al);
    }
}
