package com.jibepe.render3d;

import android.opengl.GLES20;
import android.opengl.Matrix;
import com.jibepe.labo3d.InterfaceSceneRenderer;
import com.jibepe.labo3d.TextureHandler;
import com.jibepe.util.ShaderHelper;

import java.nio.*;

/**
 * Created by tbpk7658 on 26/01/2017.
 */
public class glTexturedShape extends glRenderableShape{



//    private float [] position = {0.0f, 0.0f, 0.0f}; // X,Y,Z
//    private float [] rotation = {0.0f, 0.0f, 0.0f}; // rX,rY,rZ




    public glTexturedShape() {
        super();
        setTextured(true);
    }


    @Override
    short[] getIBOIndices (){
        short[] INDICES = { 2, 0, 1, 2, 1, 3};
        return (INDICES);
    }

    @Override
    float[] getIBObuffer(String type) {
        if (type.equals(VERTICES)){
            return getVertices ();
        } else if (type.equals(TEX_COORDS)){
            return getTexturesCoordinates ();
        } else if (type.equals(NORMALS)){
            return getNormals ();
        } else {
            return null;
        }
    }

    private float[] getNormals() {
        float[] NORMS = {
                0.000000f, 0.000000f, 1.000000f
                ,0.000000f, 0.000000f, 1.000000f
                ,0.000000f, 0.000000f, 1.000000f
                ,0.000000f, 0.000000f, 1.000000f
        };
        return NORMS;

    }

    private float[] getTexturesCoordinates() {
        float[] TEX_COORDS = {
                0.9999f,   0.9999f
                ,0.00f,     0.999900f
                ,0.999900f, 0.0f
                ,0.0f,      0.0f
        };

        return TEX_COORDS;
    }

    private float[] getVertices() {
         float[] VERTS = {
                -1.000000f, -1.000000f, 0.000000f
                ,1.000000f, -1.000000f, 0.000000f
                ,-1.000000f, 1.000000f, -0.000000f
                ,1.000000f, 1.000000f, -0.000000f
        };
        return VERTS;
    }

    @Override
    float[] getVBObuffer() {
        return null;
    }


     public static final String CUBE_MESH_VERTEX_SHADER = " \n" + "\n"
            + "attribute vec4 vertexPosition; \n"
            + "attribute vec2 vertexTexCoord; \n" + "\n"
            + "varying vec2 texCoord; \n" + "\n"
            + "uniform mat4 modelViewProjectionMatrix; \n" + "\n"
            + "void main() \n" + "{ \n"
            + "   gl_Position = modelViewProjectionMatrix * vertexPosition; \n"
            + "   texCoord = vertexTexCoord; \n"
            + "} \n";

    public static final String CUBE_MESH_FRAGMENT_SHADER = " \n" + "\n"
            + "precision mediump float; \n" + " \n"
            + "varying vec2 texCoord; \n"
            + "uniform sampler2D texSampler2D; \n" + " \n"
            + "void main() \n"
            + "{ \n" + "   gl_FragColor = texture2D(texSampler2D, texCoord); \n"
            + "} \n";


    @Override
    public void render(float[] mMatrixView, float[] mMatrixProjection, InterfaceSceneRenderer Scene) {

        /**
         * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
         * of being located at the center of the universe) to world space.
         */
        float[] mModelMatrix = new float[16];
        /** Allocate storage for the final combined matrix. This will be passed into the shader program. */
        final float[] mMVPMatrix = new float[16];

        Matrix.setIdentityM(mModelMatrix, 0);

        Matrix.rotateM(mModelMatrix, 0, getRotation()[0], 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, getRotation()[1], 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, getRotation()[2], 0.0f, 0.0f, 1.0f);

        mModelMatrix[12] = getPosition()[0];
        mModelMatrix[13] = getPosition()[1];
        mModelMatrix[14] = getPosition()[2];
        Matrix.scaleM(mModelMatrix, 0, getScale()[0], getScale()[1], getScale()[2] );

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mMatrixView, 0, mModelMatrix, 0);

        // Pass in the modelview matrix.
        //GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mMatrixProjection, 0, mMVPMatrix, 0);




        int shaderProgramID = ShaderHelper.createProgramFromShaderSrc(
                CUBE_MESH_VERTEX_SHADER,
                CUBE_MESH_FRAGMENT_SHADER);

        GLES20.glUseProgram(shaderProgramID);

        int vertexHandle = GLES20.glGetAttribLocation(shaderProgramID, "vertexPosition");

        FloatBuffer VertexData = ByteBuffer.allocateDirect(getIBObuffer(VERTICES).length * FLOAT_FIELD_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        VertexData.put(getIBObuffer(VERTICES)).position(0);

        GLES20.glVertexAttribPointer(vertexHandle, 3, GLES20.GL_FLOAT, false, 0, VertexData);

        ShortBuffer FaceData = ByteBuffer.allocateDirect(getIBOIndices().length * SHORT_FIELD_SIZE)
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        FaceData.put(getIBOIndices()).position(0);

        int textureCoordHandle = GLES20.glGetAttribLocation(shaderProgramID, "vertexTexCoord");

        FloatBuffer TexCoordData = ByteBuffer.allocateDirect(getTexturesCoordinates().length * FLOAT_FIELD_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        TexCoordData.put(getTexturesCoordinates()).position(0);


        GLES20.glVertexAttribPointer(textureCoordHandle, 2, GLES20.GL_FLOAT, false, 0, TexCoordData);


        // Render the video background with the custom shader
        // First, we enable the vertex arrays
        GLES20.glEnableVertexAttribArray(vertexHandle);
        GLES20.glEnableVertexAttribArray(textureCoordHandle);

        int mvpMatrixHandle = GLES20.glGetUniformLocation(shaderProgramID, "modelViewProjectionMatrix");
        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mMVPMatrix, 0);




        int texSampler2DHandle = GLES20.glGetUniformLocation(shaderProgramID, "texSampler2D");

        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // Bind the texture to this unit.
        int textureHandlerId = TextureHandler.getInstance().getTextureId("iclauncher.png");
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandlerId);

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(texSampler2DHandle, 0);



        // Then, we issue the render call
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, getIBOIndices().length, GLES20.GL_UNSIGNED_SHORT,
                FaceData);

        // Finally, we disable the vertex arrays
        GLES20.glDisableVertexAttribArray(vertexHandle);
        GLES20.glDisableVertexAttribArray(textureCoordHandle);
        GLES20.glUseProgram(0);

    }

}




//    private void setVerts() {
//        mVertBuff = fillBuffer(VERTS);
//        verticesNumber = VERTS.length / 3;
//    }
//
//    private void setTexCoords()
//    {
//        mTexCoordBuff = fillBuffer(TEX_COORDS);
//
//    }
//
//
//    private void setNorms()
//    {
//        mNormBuff = fillBuffer(NORMS);
//    }
//
//
//    private void setIndices()
//    {
//        mIndBuff = fillBuffer(INDICES);
//        indicesNumber = INDICES.length;
//    }

//    o Plane
//    v -1.000000 -1.000000 0.000000
//    v 1.000000 -1.000000 0.000000
//    v -1.000000 1.000000 -0.000000
//    v 1.000000 1.000000 -0.000000
//    vt 0.999900 0.000100
//    vt 0.999900 0.999900
//    vt 0.000100 0.999900
//    vt 0.000100 0.000100
//    vn 0.000000 0.000000 1.000000
//    usemtl None
//    s off
//    f 2/1/1 4/2/1 3/3/1
//    f 1/4/1 2/1/1 3/3/1


//
//public enum BUFFER_TYPE
//{
//    BUFFER_TYPE_VERTEX, BUFFER_TYPE_TEXTURE_COORD, BUFFER_TYPE_NORMALS, BUFFER_TYPE_INDICES
//}


//    public Buffer getVertices()
//    {
//        return getBuffer(BUFFER_TYPE.BUFFER_TYPE_VERTEX);
//    }
//
//
//    public Buffer getTexCoords()
//    {
//        return getBuffer(BUFFER_TYPE.BUFFER_TYPE_TEXTURE_COORD);
//    }
//
//
//    public Buffer getNormals()
//    {
//        return getBuffer(BUFFER_TYPE.BUFFER_TYPE_NORMALS);
//    }
//
//
//    public Buffer getIndices()
//    {
//        return getBuffer(BUFFER_TYPE.BUFFER_TYPE_INDICES);
//    }
//

//    protected Buffer fillBuffer(double[] array)
//    {
//        // Convert to floats because OpenGL doesn't work on doubles, and manually
//        // casting each input value would take too much time.
//        // Each float takes 4 bytes
//        ByteBuffer bb = ByteBuffer.allocateDirect(4 * array.length);
//        bb.order(ByteOrder.LITTLE_ENDIAN);
//        for (double d : array)
//            bb.putFloat((float) d);
//        bb.rewind();
//
//        return bb;
//
//    }
//
//
//    protected Buffer fillBuffer(float[] array)
//    {
//        // Each float takes 4 bytes
//        ByteBuffer bb = ByteBuffer.allocateDirect(4 * array.length);
//        bb.order(ByteOrder.LITTLE_ENDIAN);
//        for (float d : array)
//            bb.putFloat(d);
//        bb.rewind();
//
//        return bb;
//
//    }
//
//
//    protected Buffer fillBuffer(short[] array)
//    {
//        // Each short takes 2 bytes
//        ByteBuffer bb = ByteBuffer.allocateDirect(2 * array.length);
//        bb.order(ByteOrder.LITTLE_ENDIAN);
//        for (short s : array)
//            bb.putShort(s);
//        bb.rewind();
//
//        return bb;
//
//    }
//
//
//    public Buffer getBuffer(BUFFER_TYPE bufferType){
//        Buffer result = null;
//        switch (bufferType)
//        {
//            case BUFFER_TYPE_VERTEX:
//                result = mVertBuff;
//                break;
//            case BUFFER_TYPE_TEXTURE_COORD:
//                result = mTexCoordBuff;
//                break;
//            case BUFFER_TYPE_NORMALS:
//                result = mNormBuff;
//                break;
//            case BUFFER_TYPE_INDICES:
//                result = mIndBuff;
//            default:
//                break;
//
//        }
//
//        return result;
//
//    }
//
//    public int getNumObjectVertex(){
//        return verticesNumber;
//    }
//
//
//    public int getNumObjectIndex(){
//        return indicesNumber;
//    }
//
//    private Buffer mVertBuff;
//    private Buffer mTexCoordBuff;
//    private Buffer mNormBuff;
//    private Buffer mIndBuff;

//    private int indicesNumber = 0;
//    private int verticesNumber = 0;

