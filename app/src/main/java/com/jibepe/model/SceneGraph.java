package com.jibepe.model;

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
//    private float mPosCamX = 0.0f;
//
//    private float mPosCamY = 0.0f;

    private float objPos[] = {1.0f, 0.0f, -1.0f};
    private float objRot[] = {0.0f, 0.0f, 0.0f};

    private float camPos[] = {1.0f, 0.0f, -1.0f};
    private float camRot[] = {0.0f, -90.0f, 0.0f};

    public SceneGraph (){
        objectDictionary = new Hashtable<String, ObjLoader>();

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
        camPos[1] += f*Math.sin(Math.toRadians(camRot[1]));
    }


    public float getCamPosX() {
        return camPos[0];
    }

    public void setCamPosX(float mPosCamX) {
        this.camPos[0] = mPosCamX;
    }

    public float getCamPosY() {
        return camPos[1];
    }

    public void setCamPosY(float mPosCamY) {
        this.camPos[1] = mPosCamY;
    }



    public void setObjPos(String objName, float [] pos){
        // TODO: Gerer le dictionnaire d'objet
        objPos = pos;
    }
    public void setObjRot(String objName, float [] rot){
        // TODO: Gerer le dictionnaire d'objet
        objRot = rot;
    }
    public void addObj (String name, ObjLoader obj) {
        objectDictionary.put(name, obj);
    }
    public ObjLoader getObj (String objectName) {
        return objectDictionary.get(objectName);
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
