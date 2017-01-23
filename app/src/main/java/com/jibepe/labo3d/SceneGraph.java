package com.jibepe.labo3d;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tbpk7658 on 23/01/2017.
 */
public class SceneGraph {



//    private float mPosCamX = 0.0f;
//
//    private float mPosCamY = 0.0f;

    private float objPos[] = {1.0f, 0.0f, -1.0f};
    private float objRot[] = {0.0f, 0.0f, 0.0f};

    private float camPos[] = {1.0f, 0.0f, -1.0f};
    private float camRot[] = {0.0f, -90.0f, 0.0f};

    public SceneGraph (){

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


    public List<float[]> getLightsPos() {
//        // Do a complete rotation every 10 seconds.
//        long time = SystemClock.uptimeMillis() % 10000L;
//        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
        ArrayList<float[]> al = new ArrayList<float[]>();
        al.add(new float[] {0.0f, 1.0f, 2.0f});
        return (al);
    }
}
