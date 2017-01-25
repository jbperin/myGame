package com.jibepe.labo3d;

import android.opengl.GLES20;
import android.opengl.Matrix;
import com.jibepe.objparser.MaterialShape;
import com.jibepe.objparser.ObjLoader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Enumeration;

/**
 * Created by Famille PERIN on 24/01/2017.
 */
public class glAlphaTexturedShape extends glRenderableShape{


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

    private float [] position = {0.0f, 0.0f, 0.0f}; // X,Y,Z
    private float [] rotation = {0.0f, 0.0f, 0.0f}; // rX,rY,rZ
    private float size = 1.0f;
    //private float [] buffer;


    ObjLoader mShape;

    public glAlphaTexturedShape(ObjLoader Shape) {
        super();
        mShape = Shape;

    }


    @Override
    void render(float[] mMatrixVP,  InterfaceSceneRenderer Scene) {

    }

    @Override
    void render(float[] mMatrixView, float[] mMatrixProjection,  InterfaceSceneRenderer Scene) {

        Enumeration<String> key = mShape.dictionary.keys();

        while(key.hasMoreElements()) {
            String matName = key.nextElement();
            MaterialShape matShape = (MaterialShape) mShape.dictionary.get(matName);
            if (matShape.texturename != null) {


                int program;


                program = ShaderHandler.getInstance().getShaderProgram(ShaderHandler.sSolidTexColorNoLightProgram);
                GLES20.glUseProgram(program);
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

                Matrix.rotateM(mModelMatrix, 0, rotation[0], 1.0f, 0.0f, 0.0f);
                Matrix.rotateM(mModelMatrix, 0, rotation[1], 0.0f, 1.0f, 0.0f);
                Matrix.rotateM(mModelMatrix, 0, rotation[2], 0.0f, 0.0f, 1.0f);

                mModelMatrix[12] = position[0];
                mModelMatrix[13] = position[1];
                mModelMatrix[14] = position[2];


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

                float[] mLightPosInEyeSpace = Scene.getLightsPos().get(0);
                // Pass in the light position in eye space.
                GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);

                // Draw the cube.
                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, matShape.buffer.length / (POSITION_DATA_SIZE + NORMAL_DATA_SIZE + UVCOORD_DATA_SIZE));

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
