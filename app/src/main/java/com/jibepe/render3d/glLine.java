package com.jibepe.render3d;

import android.opengl.GLES20;
import android.opengl.Matrix;
import com.jibepe.labo3d.InterfaceSceneRenderer;
import com.jibepe.labo3d.ShaderHandler;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by tbpk7658 on 24/01/2017.
 */
public class glLine extends glRenderableShape {

    public float[] getPosStart() {
        return posStart;
    }

    public void setPosStart(float[] posStart) {
        this.posStart = posStart;
    }

    public float[] getPosEnd() {
        return posEnd;
    }

    public void setPosEnd(float[] posEnd) {
        this.posEnd = posEnd;
    }


//    public void setColor(float[] color) {    this.color = color; }

    float [] posStart = {0.0f, 0.0f, 0.0f};
    float [] posEnd = {0.0f, 0.0f, 0.0f};
 //   private float [] color = {1.0f, 1.0f, 1.0f, 1.0f};//r, g, b, a

    public glLine( ) {
        super();
    }

    @Override
    float[] getIBObuffer(String type) {
        return null;
    }

    @Override
    short [] getIBOIndices() {
        return null;
    }

    @Override
    float[] getVBObuffer() {
        float [] buffer = {posStart[0], posStart[1], posStart[2], posEnd[0], posEnd[1], posEnd[2]};
        return buffer;
    }

    @Override
    void render(float[] mMatrixView, float[] mMatrixProjection, InterfaceSceneRenderer Scene) {
        float[] mVPMatrix = new float[16];
        Matrix.multiplyMM(mVPMatrix, 0, mMatrixProjection, 0, mMatrixView, 0);
        render (mVPMatrix, Scene);
    }

    void render(float[] mVPMatrix, InterfaceSceneRenderer Scene) {
        final float VertexData[] = getVBObuffer();
        //final float[] Color = {r, g, b, a};
        final float[] mMVPMatrix = new float[16];
        final float[] ModelMatrix = new float[16];

        int mSolidLineProgram = ShaderHandler.getInstance().getShaderProgramId(ShaderHandler.sSolidLineProgram);

        GLES20.glUseProgram(mSolidLineProgram);

        final int mPositionHandle = GLES20.glGetAttribLocation(mSolidLineProgram, "a_Position");

        /** This will be used to pass in the transformation matrix. */
        final int mMVPMatrixHandle = GLES20.glGetUniformLocation(mSolidLineProgram, "u_MVPMatrix");

        final int mColorHandle = GLES20.glGetUniformLocation(mSolidLineProgram, "u_Color");

        FloatBuffer mLinePositions = ByteBuffer.allocateDirect(VertexData.length * FLOAT_FIELD_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mLinePositions.put(VertexData).position(0);

        // Positions
        GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false,
                0, mLinePositions);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        Matrix.setIdentityM(ModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mVPMatrix, 0, ModelMatrix, 0);

        // Combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        // Color.
        float []color  = getColor();
        GLES20.glUniform4f(mColorHandle,  color[0], color[1], color[2], color[3]);

        GLES20.glDrawArrays(GLES20.GL_LINES, 0, 2);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
        //GLES20.glUseProgram(0);

    }

}
