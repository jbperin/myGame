package com.jibepe.render3d;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import com.jibepe.labo3d.InterfaceSceneRenderer;
import com.jibepe.labo3d.ShaderHandler;
import com.jibepe.labo3d.TextureHandler;
import com.jibepe.objparser.MaterialShape;
import com.jibepe.objparser.ObjLoader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Famille PERIN on 24/01/2017.
 */
public class glAlphaTexturedShape extends glRenderableShape{

    private String TAG = "glAlphaTexturedShape";
    private float size = 1.0f;
    //private float [] buffer;


    ObjLoader mShape;

    public glAlphaTexturedShape(ObjLoader Shape) {
        super();
        mShape = Shape;
        setTextured(true);

    }


    @Override
    short[] getIBOIndices() {
        return mShape.getFaceIndexBuffer();
    }

    @Override
    float[] getIBObuffer(String type) {

        if (type.equals(VERTICES)){
            return mShape.getVerticesBuffer();
        } else if (type.equals(TEX_COORDS)){
            return mShape.getTextureCoordinatesBuffer();
        } else if (type.equals(NORMALS)){
            return mShape.getNormalsBuffer();
        } else {
            return null;
        }
    }

    @Override
    float[] getVBObuffer() {

        FloatBuffer bufferObj = null;
        float [] buffer = null;
        Enumeration<String> key = mShape.dictionary.keys();
        List<String> list = Collections.list(key);


        int nb_float_in_buff = 0;
        for (String matName : list) {
            MaterialShape matShape = (MaterialShape) mShape.dictionary.get(matName);
            if (matShape.texturename != null) {
                nb_float_in_buff += matShape.buffer.length;
            } else {

            }
        }

        if (nb_float_in_buff > 0) {
            bufferObj = FloatBuffer.allocate(nb_float_in_buff);
            for (String matName : list) {
                MaterialShape matShape = (MaterialShape) mShape.dictionary.get(matName);
                if (matShape.texturename != null) {
                    bufferObj.put(matShape.buffer);
                } else {

                }
            }

        }

        if (bufferObj != null ) {
            return (bufferObj.array());
        }else {
            return (null);
        }
    }

    @Override
    public void render(float[] mMatrixView, float[] mMatrixProjection,  InterfaceSceneRenderer Scene) {

        Enumeration<String> key = mShape.dictionary.keys();

        while(key.hasMoreElements()) {
            String matName = key.nextElement();
            MaterialShape matShape = (MaterialShape) mShape.dictionary.get(matName);
            if (matShape.texturename != null) {


                int program;


                program = ShaderHandler.getInstance().getShaderProgramId(ShaderHandler.sSolidTexColorLightProgram);
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


                // "a_Position", "a_Color", "a_Normal", "a_TexCoordinate"
                final int mPositionHandle = GLES20.glGetAttribLocation(program, "a_Position");
                final int mNormalHandle = GLES20.glGetAttribLocation(program, "a_Normal");
                final int mTextureCoordinateHandle = GLES20.glGetAttribLocation(program, "a_TexCoordinate");

                final int mTextureUniformHandle = GLES20.glGetUniformLocation(program, "u_Texture");
                /** This will be used to pass in the transformation matrix. */
                final int mMVPMatrixHandle = GLES20.glGetUniformLocation(program, "u_MVPMatrix");
                /** This will be used to pass in the modelview matrix. */
                final int mMVMatrixHandle = GLES20.glGetUniformLocation(program, "u_MVMatrix");
                /** This will be used to pass in the light position. */
                final int mLightPosHandle = GLES20.glGetUniformLocation(program, "u_LightPos");
                final int mColorHandle = GLES20.glGetUniformLocation(program, "u_Color");

                /** Allocate storage for the final combined matrix. This will be passed into the shader program. */
                final float[] mMVPMatrix = new float[16];

                FloatBuffer FaceData = ByteBuffer.allocateDirect(matShape.buffer.length * FLOAT_FIELD_SIZE)
                        .order(ByteOrder.nativeOrder()).asFloatBuffer();
                FaceData.put(matShape.buffer).position(0);

                final int stride = (POSITION_DATA_SIZE + NORMAL_DATA_SIZE + UVCOORD_DATA_SIZE) * FLOAT_FIELD_SIZE;

                FaceData.position(0);
                GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false,
                        stride, FaceData);
                GLES20.glEnableVertexAttribArray(mPositionHandle);

                FaceData.position(POSITION_DATA_SIZE);
                GLES20.glVertexAttribPointer(mNormalHandle, NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false,
                        stride, FaceData);
                GLES20.glEnableVertexAttribArray(mNormalHandle);

                // Set the active texture unit to texture unit 0.
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

                // Bind the texture to this unit.
                int textureHandlerId = TextureHandler.getInstance().getTextureId(matShape.texturename);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandlerId);

                // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
                GLES20.glUniform1i(mTextureUniformHandle, 0);

                FaceData.position(POSITION_DATA_SIZE + NORMAL_DATA_SIZE);
                GLES20.glVertexAttribPointer(mTextureCoordinateHandle, UVCOORD_DATA_SIZE, GLES20.GL_FLOAT, false,
                        stride, FaceData);
                GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);

                // Color.{0.5f, 0.5f, 0.5f, 1.0f}
                GLES20.glUniform4f(mColorHandle, 0.5f, 0.5f, 0.5f, 1.0f);


                // DEB Alpha Blending
//        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
//        GLES20.glEnable(GLES20.GL_BLEND);
                GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
                GLES20.glEnable(GLES20.GL_BLEND);
//		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
                GLES20.glDisable(GLES20.GL_CULL_FACE);

                Matrix.setIdentityM(mModelMatrix, 0);

                Matrix.translateM(mModelMatrix, 0, getPosition()[0], getPosition()[1], getPosition()[2]);
                //Matrix.setRotateEulerM(mModelMatrix, 0, getRotation()[0], getRotation()[1], getRotation()[2]);
                Matrix.rotateM(mModelMatrix, 0, getRotation()[0], 1.0f, 0.0f, 0.0f);
//                Log.d (TAG, "Rotate X");
//                Log.d(TAG, "l1 = (" + mModelMatrix[0] + ", " + mModelMatrix[1] + ", " + mModelMatrix[2] + ", " + mModelMatrix[3]+ ")");
//                Log.d(TAG, "l2 = (" + mModelMatrix[4] + ", " + mModelMatrix[5] + ", " + mModelMatrix[6] + ", " + mModelMatrix[7]+ ")");
//                Log.d(TAG, "l3 = (" + mModelMatrix[8] + ", " + mModelMatrix[9] + ", " + mModelMatrix[10] + ", " + mModelMatrix[11]+ ")");
//                Log.d(TAG, "l4 = (" + mModelMatrix[12] + ", " + mModelMatrix[13] + ", " + mModelMatrix[14] + ", " + mModelMatrix[15]+ ")");
                Matrix.rotateM(mModelMatrix, 0, getRotation()[1], 0.0f, 1.0f, 0.0f);
//                Log.d (TAG, "Rotate Y");
//                Log.d(TAG, "l1 = (" + mModelMatrix[0] + ", " + mModelMatrix[1] + ", " + mModelMatrix[2] + ", " + mModelMatrix[3]+ ")");
//                Log.d(TAG, "l2 = (" + mModelMatrix[4] + ", " + mModelMatrix[5] + ", " + mModelMatrix[6] + ", " + mModelMatrix[7]+ ")");
//                Log.d(TAG, "l3 = (" + mModelMatrix[8] + ", " + mModelMatrix[9] + ", " + mModelMatrix[10] + ", " + mModelMatrix[11]+ ")");
//                Log.d(TAG, "l4 = (" + mModelMatrix[12] + ", " + mModelMatrix[13] + ", " + mModelMatrix[14] + ", " + mModelMatrix[15]+ ")");
                Matrix.rotateM(mModelMatrix, 0, getRotation()[2], 0.0f, 0.0f, 1.0f);
//                Log.d (TAG, "Rotate Z");
//                Log.d(TAG, "l1 = (" + mModelMatrix[0] + ", " + mModelMatrix[1] + ", " + mModelMatrix[2] + ", " + mModelMatrix[3]+ ")");
//                Log.d(TAG, "l2 = (" + mModelMatrix[4] + ", " + mModelMatrix[5] + ", " + mModelMatrix[6] + ", " + mModelMatrix[7]+ ")");
//                Log.d(TAG, "l3 = (" + mModelMatrix[8] + ", " + mModelMatrix[9] + ", " + mModelMatrix[10] + ", " + mModelMatrix[11]+ ")");
//                Log.d(TAG, "l4 = (" + mModelMatrix[12] + ", " + mModelMatrix[13] + ", " + mModelMatrix[14] + ", " + mModelMatrix[15]+ ")");

                //mModelMatrix[12] = getPosition()[0];
                //mModelMatrix[13] = getPosition()[1];
                //mModelMatrix[14] = getPosition()[2];

//                Log.d(TAG, "l1 = (" + mModelMatrix[0] + ", " + mModelMatrix[1] + ", " + mModelMatrix[2] + ", " + mModelMatrix[3]+ ")");
//                Log.d(TAG, "l2 = (" + mModelMatrix[4] + ", " + mModelMatrix[5] + ", " + mModelMatrix[6] + ", " + mModelMatrix[7]+ ")");
//                Log.d(TAG, "l3 = (" + mModelMatrix[8] + ", " + mModelMatrix[9] + ", " + mModelMatrix[10] + ", " + mModelMatrix[11]+ ")");
//                Log.d(TAG, "l4 = (" + mModelMatrix[12] + ", " + mModelMatrix[13] + ", " + mModelMatrix[14] + ", " + mModelMatrix[15]+ ")");
//
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

                // Draw the cube.
                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, matShape.buffer.length / (POSITION_DATA_SIZE + NORMAL_DATA_SIZE + UVCOORD_DATA_SIZE));


                GLES20.glDisableVertexAttribArray(mTextureCoordinateHandle);
                GLES20.glDisableVertexAttribArray(mNormalHandle);
                GLES20.glDisableVertexAttribArray(mPositionHandle);

                // DEB Alpha Blending
                GLES20.glDisable(GLES20.GL_BLEND);
                //GLES20.glDisableVertexAttribArray(mPositionHandle);
//		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
                GLES20.glEnable(GLES20.GL_CULL_FACE);

            } else {

            }
        }
    }
}
