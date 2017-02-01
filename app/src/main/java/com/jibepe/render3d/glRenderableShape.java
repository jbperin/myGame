package com.jibepe.render3d;

import android.opengl.GLES20;
import com.jibepe.labo3d.InterfaceSceneRenderer;

/**
 * Created by tbpk7658 on 24/01/2017.
 */
public abstract class glRenderableShape {

    protected final int FLOAT_FIELD_SIZE = 4;
    protected final int SHORT_FIELD_SIZE = 2;
    protected final int POSITION_DATA_SIZE = 3;
    protected final int NORMAL_DATA_SIZE = 3;
    protected final int UVCOORD_DATA_SIZE = 2;
    protected final int COLOR_DATA_SIZE = 4;

    protected static final String VERTICES = "VERTICES";
    protected static final String TEX_COORDS = "TEX_COORDS";
    protected static final String NORMALS = "NORMS";

    public float[] getPosition() {
        return position;
    }

    public void setPosition(float[] position) {
        this.position = position;
    }

    public float[] getRotation() {
        return rotation;
    }

    public void setRotation(float[] rotation) {
        this.rotation = rotation;
    }

    public float[] getScale() {
        return scale;
    }

    public void setScale(float[] scale) {
        this.scale = scale;
    }

    private float [] position = {0.0f, 0.0f, 0.0f}; // X,Y,Z
    private float [] rotation = {0.0f, 0.0f, 0.0f}; // rX,rY,rZ
    private float [] scale = {1.0f, 1.0f, 1.0f}; // sX,sY,sZ

    public float[] getColor() {
        return color;
    }

    public void setColor(float[] color) {
        this.color = color;
    }

    private float [] color = {0.5f, 0.5f, 0.5f, 1.0f}; // red, green, blue, alpha

    public glRenderableShape() {

    }

    abstract short[] getIBOIndices ();
    abstract float[] getIBObuffer (String Type);
    abstract float[] getVBObuffer ();

    abstract void render (float[] mMatrixView, float[] mMatrixProjection,  InterfaceSceneRenderer Scene);

}
