/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jibepe.render3d;

import android.opengl.GLES20;
import android.opengl.Matrix;
import com.jibepe.labo3d.InterfaceSceneRenderer;
import com.jibepe.util.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * A two-dimensional triangle for use as a drawn object in OpenGL ES 2.0.
 */
public class Triangle extends glRenderableShape {

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            // the matrix must be included as a modifier of gl_Position
            // Note that the uMVPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            "  gl_Position = uMVPMatrix * vPosition;" +
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}";

    private final FloatBuffer vertexBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = {
            // in counterclockwise order:
            0.0f,  0.622008459f, 0.0f,   // top
           -0.5f, -0.311004243f, 0.0f,   // bottom left
            0.5f, -0.311004243f, 0.0f    // bottom right
    };
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex


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



    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Triangle() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader = ShaderHelper.loadShader(
                GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = ShaderHelper.loadShader(
                GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

    }

    @Override
    short[] getIBOIndices() {
        short [] indices = {0, 1 , 2};
        return indices;
    }

    @Override
    float[] getIBObuffer(String type) {
        if (type.equals(VERTICES)){
            return triangleCoords;
        } else if (type.equals(TEX_COORDS)){
            return null;
        } else if (type.equals(NORMALS)){
            return null;
        } else {
            return null;
        }
    }

    @Override
    float[] getVBObuffer() {
        return null;
    }


    @Override
    public void render(float[] mMatrixView, float[] mMatrixProjection, InterfaceSceneRenderer Scene) {
        /**
         * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
         * of being located at the center of the universe) to world space.
         */
        float[] mModelMatrix = new float[16];
        final float[] mMVPMatrix = new float[16];


        Matrix.setIdentityM(mModelMatrix, 0);

        Matrix.rotateM(mModelMatrix, 0, rotation[0], 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, rotation[1], 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, rotation[2], 0.0f, 0.0f, 1.0f);

        mModelMatrix[12] = position[0];
        mModelMatrix[13] = position[1];
        mModelMatrix[14] = position[2];

        Matrix.scaleM(mModelMatrix, 0, getScale()[0], getScale()[1], getScale()[2] );

        // Pass in the transformation matrix.
        //Matrix.multiplyMM(mMVPMatrix, 0, mMatrixVP, 0, mModelMatrix, 0);

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mMatrixView, 0, mModelMatrix, 0);

        // Pass in the modelview matrix.
        // GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mMatrixProjection, 0, mMVPMatrix, 0);

        draw(mMVPMatrix);

    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        float [] color = getColor();
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        //MyGLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        //MyGLRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

}
