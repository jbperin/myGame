package com.jibepe.labo3d;

import android.opengl.GLES20;
import android.opengl.Matrix;

/**
 * Created by tbpk7658 on 24/01/2017.
 */
public class glPoint extends glRenderableShape {

    private float [] position = {0.0f, 0.0f, 0.0f}; // X,Y,Z
    private float [] color = {1.0f, 1.0f, 1.0f, 1.0f};//r, g, b, a
    private float size = 1.0f;

    public glPoint() {
        super ();
    }

    @Override
    void render(float[] mVPMatrix, ShaderHelper sh, InterfaceSceneRenderer Scene) {
        int program = sh.getShaderProgram(ShaderHelper.sSolidPointProgram);

        final int mMVPMatrixHandle = GLES20.glGetUniformLocation(program, "u_MVPMatrix");
        final int mColorHandle = GLES20.glGetUniformLocation(program, "u_Color");
        final int mSizeHandle = GLES20.glGetUniformLocation(program, "u_Size");

        final int mPositionHandle = GLES20.glGetAttribLocation(program, "a_Position");

        float[] mModelMatrix = new float[16];

        Matrix.setIdentityM(mModelMatrix, 0);
        //Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, -2.0f);
        //Matrix.rotateM(mLightModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);
        Matrix.translateM(mModelMatrix, 0, position[0], position[1], position[2]);


        /** Allocate storage for the final combined matrix. This will be passed into the shader program. */
        final float[] mMVPMatrix = new float[16];

        GLES20.glUseProgram(program);

        // Pass in the position.
        GLES20.glVertexAttrib3f(mPositionHandle, position [0], position [1], position [2]);

        // Color
        GLES20.glUniform4f(mColorHandle, color[0], color[1], color[2], color[3]);

        // Size
        GLES20.glUniform1f(mSizeHandle, size);

        // Since we are not using a buffer object, disable vertex arrays for this attribute.
        GLES20.glDisableVertexAttribArray(mPositionHandle);

        // Pass in the transformation matrix.
        Matrix.multiplyMM(mMVPMatrix, 0, mVPMatrix, 0, mModelMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        // Draw the point.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);


    }

    @Override
    void render(float[] mMatrixView, float[] mMatrixProjection, ShaderHelper sh, InterfaceSceneRenderer Scene) {
        float[] mVPMatrix = new float[16];
        Matrix.multiplyMM(mVPMatrix, 0, mMatrixProjection, 0, mMatrixView, 0);
        render (mVPMatrix,sh, Scene);
    }
}
