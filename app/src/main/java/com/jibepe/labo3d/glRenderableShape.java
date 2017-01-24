package com.jibepe.labo3d;

import android.opengl.GLES20;

/**
 * Created by tbpk7658 on 24/01/2017.
 */
public abstract class glRenderableShape {

    protected final int FLOAT_FIELD_SIZE = 4;
    protected final int POSITION_DATA_SIZE = 3;
    protected final int NORMAL_DATA_SIZE = 3;
    protected final int UVCOORD_DATA_SIZE = 2;



    public glRenderableShape() {

    }

    abstract void render (float[] mMatrixVP, ShaderHelper sh, InterfaceSceneRenderer Scene);

    abstract void render (float[] mMatrixView, float[] mMatrixProjection,  ShaderHelper sh, InterfaceSceneRenderer Scene);

}
