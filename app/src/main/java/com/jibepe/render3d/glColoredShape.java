package com.jibepe.render3d;

import android.opengl.GLES20;
import android.opengl.Matrix;
import com.jibepe.labo3d.InterfaceSceneRenderer;
import com.jibepe.labo3d.ShaderHandler;
import com.jibepe.objparser.MaterialShape;
import com.jibepe.objparser.ObjLoader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Enumeration;

/**
 * Created by tbpk7658 on 24/01/2017.
 */
public class glColoredShape extends glRenderableShape {

    private float [] position = {0.0f, 0.0f, 0.0f}; // X,Y,Z
    private float [] rotation = {0.0f, 0.0f, 0.0f}; // rX,rY,rZ
    private float size = 1.0f;
    //private float [] buffer;


    ObjLoader mShape;

    public glColoredShape(ObjLoader Shape) {
        super();
        mShape = Shape;

    }

    @Override
    void render(float[] mMatrixView, float[] mMatrixProjection, InterfaceSceneRenderer Scene) {

        Enumeration<String> key = mShape.dictionary.keys();

        while(key.hasMoreElements()){
        	String matName = key.nextElement();
        	MaterialShape matShape = (MaterialShape)mShape.dictionary.get(matName);
        	if (matShape.texturename != null) {
//        		final float[] uvplan_color = {0.5f, 0.5f, 0.5f, 1.0f};
//        		final int textId = dTextureHandlers.get(matShape.texturename);
//        		drawAlphaTexturedBuffer(matShape.buffer, uvplan_color, textId);
        	}
        	else
        	{
            	final float [] matShapeColor= {matShape.r, matShape.g, matShape.b, 1.0f};
                int program = ShaderHandler.getInstance().getShaderProgramId(ShaderHandler.sSolidUColorLightProgram);
                GLES20.glUseProgram(program);


                /**
                 * Stores a copy of the model matrix specifically for the light position.
                 */
                float[] mLightModelMatrix = new float[16];
                /** Used to hold a light centered on the origin in model space. We need a 4th coordinate so we can get translations to work when
                 *  we multiply this by our transformation matrices. */
                float[] mLightPosInModelSpace = new float[] {0.0f, 0.0f, 0.0f, 1.0f};

                /** Used to hold the current position of the light in world space (after transformation via model matrix). */
                float[] mLightPosInWorldSpace = new float[4];

                /** Used to hold the transformed position of the light in eye space (after transformation via modelview matrix) */
                float[] mLightPosInEyeSpace = new float[4];

                /**
                 * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
                 * of being located at the center of the universe) to world space.
                 */
                float[] mModelMatrix = new float[16];


                final int mPositionHandle = GLES20.glGetAttribLocation(program, "a_Position");
                final int mNormalHandle = GLES20.glGetAttribLocation(program, "a_Normal");

                /** This will be used to pass in the transformation matrix. */
                final int mMVPMatrixHandle = GLES20.glGetUniformLocation(program, "u_MVPMatrix");
                /** This will be used to pass in the modelview matrix. */
                final int mMVMatrixHandle = GLES20.glGetUniformLocation(program, "u_MVMatrix");
                /** This will be used to pass in the light position. */
                final int mLightPosHandle = GLES20.glGetUniformLocation(program, "u_LightPos");

                final int mColorHandle = GLES20.glGetUniformLocation(program, "u_Color");

                final float[] mMVPMatrix = new float[16];

                final int stride = (POSITION_DATA_SIZE + NORMAL_DATA_SIZE) * FLOAT_FIELD_SIZE;


                FloatBuffer FaceData = ByteBuffer.allocateDirect(matShape.buffer.length * FLOAT_FIELD_SIZE)
                        .order(ByteOrder.nativeOrder()).asFloatBuffer();
                FaceData.put(matShape.buffer).position(0);

                FaceData.position(0);
                GLES20.glVertexAttribPointer(mPositionHandle,POSITION_DATA_SIZE, GLES20.GL_FLOAT, false,
                        stride, FaceData);
                GLES20.glEnableVertexAttribArray(mPositionHandle);

                FaceData.position(POSITION_DATA_SIZE);
                GLES20.glVertexAttribPointer(mNormalHandle,NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false,
                        stride, FaceData);
                GLES20.glEnableVertexAttribArray(mNormalHandle);



                Matrix.setIdentityM(mModelMatrix, 0);

                Matrix.rotateM(mModelMatrix, 0, rotation[0], 1.0f, 0.0f, 0.0f);
                Matrix.rotateM(mModelMatrix, 0, rotation[1], 0.0f, 1.0f, 0.0f);
                Matrix.rotateM(mModelMatrix, 0, rotation[2], 0.0f, 0.0f, 1.0f);

                mModelMatrix[12] = position[0];
                mModelMatrix[13] = position[1];
                mModelMatrix[14] = position[2];


                // Pass in the transformation matrix.
                //Matrix.multiplyMM(mMVPMatrix, 0, mMatrixVP, 0, mModelMatrix, 0);

                // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
                // (which currently contains model * view).
                // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
                // (which currently contains model * view).
                Matrix.multiplyMM(mMVPMatrix, 0, mMatrixView, 0, mModelMatrix, 0);

                // Pass in the modelview matrix.
                GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

                // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
                // (which now contains model * view * projection).
                Matrix.multiplyMM(mMVPMatrix, 0, mMatrixProjection, 0, mMVPMatrix, 0);

                // Pass in the combined matrix.
                GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);


                float [] lightPos = Scene.getLightsPos().get(0);

                // Calculate position of the light. Rotate and then push into the distance.
                Matrix.setIdentityM(mLightModelMatrix, 0);
                //Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, -2.0f);
                //Matrix.rotateM(mLightModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);
                Matrix.translateM(mLightModelMatrix, 0, lightPos[0], lightPos[1], lightPos[2]);

                Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
                Matrix.multiplyMV(mLightPosInEyeSpace, 0, mMatrixView, 0, mLightPosInWorldSpace, 0);

                // Pass in the light position in eye space.
                GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);

                // Color.
                GLES20.glUniform4f(mColorHandle, matShape.r, matShape.g, matShape.b, 1.0f);

                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, matShape.buffer.length/(POSITION_DATA_SIZE + NORMAL_DATA_SIZE));


                GLES20.glDisableVertexAttribArray(mNormalHandle);
                GLES20.glDisableVertexAttribArray(mPositionHandle);

            }
        }


    }

    public void setPosition(float[] floats) {
        position = floats;
    }

    public void setRotation(float[] floats) {
        rotation = floats;
    }

}
