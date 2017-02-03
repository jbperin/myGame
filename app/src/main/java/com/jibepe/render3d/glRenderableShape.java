package com.jibepe.render3d;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import com.jibepe.labo3d.InterfaceSceneRenderer;
import com.jibepe.math.Util;
import com.jibepe.math.Vector;

/**
 * Created by tbpk7658 on 24/01/2017.
 */
public abstract class glRenderableShape {

    private static String TAG = "glRenderableShape";
    protected final int FLOAT_FIELD_SIZE = 4;
    protected final int SHORT_FIELD_SIZE = 2;
    protected final int POSITION_DATA_SIZE = 3;
    protected final int NORMAL_DATA_SIZE = 3;
    protected final int UVCOORD_DATA_SIZE = 2;
    protected final int COLOR_DATA_SIZE = 4;

    protected static final String VERTICES = "VERTICES";
    protected static final String TEX_COORDS = "TEX_COORDS";
    protected static final String NORMALS = "NORMS";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private  String name = "";

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

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    private boolean isVisible = true;

    public boolean isRaySensible() {
        return isRaySensible;
    }

    public void setRaySensible(boolean raySensible) {
        isRaySensible = raySensible;
    }

    private boolean isRaySensible = true;

    public boolean isTextured() {
        return isTextured;
    }

    protected void setTextured(boolean textured) {
        isTextured = textured;
    }

    private boolean isTextured = false;

    public glRenderableShape() {

    }

    private float[] convertWorldSpace (float [] inCoord) {
        //float [] outCoord = new float[4];
        float[] mModelMatrix = new float[16];
        float[] resultVec = new float[4];

//        Log.d(TAG, "inCoord = (" + inCoord[0] + ", " + inCoord[1] + ", " + inCoord[2] + ", " + inCoord[3]+ ")");

        Matrix.setIdentityM(mModelMatrix, 0);

        Matrix.translateM(mModelMatrix, 0, getPosition()[0], getPosition()[1], getPosition()[2]);
        //Matrix.setRotateEulerM(mModelMatrix, 0, getRotation()[0], getRotation()[1], getRotation()[2]);
        Matrix.rotateM(mModelMatrix, 0, getRotation()[0], 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, getRotation()[1], 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, getRotation()[2], 0.0f, 0.0f, 1.0f);

        //Matrix.setRotateEulerM(mModelMatrix, 0, rotation[0], rotation[1], rotation[2]);
//        Log.d(TAG, "Translate + Rotate");
//        Log.d(TAG, "l1 = (" + mModelMatrix[0] + ", " + mModelMatrix[1] + ", " + mModelMatrix[2] + ", " + mModelMatrix[3]+ ")");
//        Log.d(TAG, "l2 = (" + mModelMatrix[4] + ", " + mModelMatrix[5] + ", " + mModelMatrix[6] + ", " + mModelMatrix[7]+ ")");
//        Log.d(TAG, "l3 = (" + mModelMatrix[8] + ", " + mModelMatrix[9] + ", " + mModelMatrix[10] + ", " + mModelMatrix[11]+ ")");
//        Log.d(TAG, "l4 = (" + mModelMatrix[12] + ", " + mModelMatrix[13] + ", " + mModelMatrix[14] + ", " + mModelMatrix[15]+ ")");

        //Matrix.translateM(mModelMatrix, 0, inCoord[0], inCoord [1], inCoord [2]);
//        Log.d(TAG, "translate");
//        Log.d(TAG, "l1 = (" + mModelMatrix[0] + ", " + mModelMatrix[1] + ", " + mModelMatrix[2] + ", " + mModelMatrix[3]+ ")");
//        Log.d(TAG, "l2 = (" + mModelMatrix[4] + ", " + mModelMatrix[5] + ", " + mModelMatrix[6] + ", " + mModelMatrix[7]+ ")");
//        Log.d(TAG, "l3 = (" + mModelMatrix[8] + ", " + mModelMatrix[9] + ", " + mModelMatrix[10] + ", " + mModelMatrix[11]+ ")");
//        Log.d(TAG, "l4 = (" + mModelMatrix[12] + ", " + mModelMatrix[13] + ", " + mModelMatrix[14] + ", " + mModelMatrix[15]+ ")");

        Matrix.multiplyMV(resultVec , 0,  mModelMatrix, 0, inCoord,0);

//        Log.d(TAG, "vect = (" + resultVec[0] + ", " + resultVec[1] + ", " + resultVec[2] + ", " + resultVec[3]+ ")");

        return resultVec ;

    }
    public float[] intersectRay (float [] rayPoint , float [] rayDirection){
        float[] foundIntersection = null;
        float [] closestIntersection = null;
        float bestDistance = 0.0f;

        if (isRaySensible()) {
            float[] vboBuffer = getVBObuffer();
            if (vboBuffer != null) {

                //Log.d(TAG, "scanning  VBO defined Shape "+getName() );

                int incrementInVboBuffer;
                // vboBuffer is build
                // v0.x, v0.y, v0.z, n0.x, n0.y, n0.z, [uv0.u, uv0.v], v1.x, v1.y , .... etc ..
                //
                if (!isTextured()) {
                    incrementInVboBuffer = 6; // 3 for vertices coord + 3 for normal coord
                } else {
                    incrementInVboBuffer = 8; // 2 more for uv coord
                }

                int index = 0;
                while (index < vboBuffer.length - incrementInVboBuffer + 1) {

                    // RETRIEVE VERTEX COORDINATES

                    float[] vert1 = new float[]{vboBuffer[index], vboBuffer[index + 1], vboBuffer[index + 2], 1.0f};

                    index += incrementInVboBuffer;

                    float[] vert2 = new float[]{vboBuffer[index], vboBuffer[index + 1], vboBuffer[index + 2], 1.0f};

                    index += incrementInVboBuffer;

                    float[] vert3 = new float[]{vboBuffer[index], vboBuffer[index + 1], vboBuffer[index + 2], 1.0f};

                    index += incrementInVboBuffer;

//                    Log.d(TAG, "v1 = (" + vert1[0] + ", " + vert1[1] + ", " + vert1[2] + ")");
//                    Log.d(TAG, "v2 = (" + vert2[0] + ", " + vert2[1] + ", " + vert2[2] + ")");
//                    Log.d(TAG, "v3 = (" + vert3[0] + ", " + vert3[1] + ", " + vert3[2] + ")");


                    //setRotateEulerM(float[] rm, int rmOffset, float x, float y, float z);
                    foundIntersection = isFoundIntersection(rayPoint, rayDirection, vert1, vert2, vert3);


                    if (foundIntersection != null) {
                        // compute distance to intersection
                        float distance  = ( new Vector( new float [] {
                                foundIntersection [0] - rayPoint[0]
                                ,foundIntersection [1] - rayPoint[1]
                                ,foundIntersection [2] - rayPoint[2] })).magnitude();

                        if (closestIntersection == null) {
                            closestIntersection = foundIntersection;
                            bestDistance = distance;
                        } else {
                            if (distance < bestDistance) {
                                closestIntersection = foundIntersection;
                                bestDistance = distance;
                            }
                        }
                    }
                }

            } else {

                //Log.d(TAG, "scanning  IBO defined Shape "+getName() );

                float[] vertsBuffer = getIBObuffer(VERTICES);
                short[] indiceBuffer = getIBOIndices();

                if ((vertsBuffer != null) && (indiceBuffer != null)) {
                    // vertsBuffer is build that way:
                    // v0.x, v0.y, v0.z, v1.x, v1.y, v1.z, v2.x, v2.y , .... etc ..
                    // indiceBuffer give index of face's vertices in vertsBuffer:
                    // f0v0.idx, f0v1.idx, f0v2.idx, f1v0.idx, f1v1.idx, f1v2.idx, f2v0.idx,  .... etc ..
                    int incrementInIndiceBuffer = 3;
                    int index = 0;
                    while (index < indiceBuffer.length - incrementInIndiceBuffer + 1) {

                        float[] vert1 = new float[]{vertsBuffer[indiceBuffer[index]*3], vertsBuffer[indiceBuffer[index]*3 + 1 ], vertsBuffer[indiceBuffer[index]*3 + 2], 1.0f};

                        float[] vert2 = new float[]{vertsBuffer[indiceBuffer[index+1]*3], vertsBuffer[indiceBuffer[index+1]*3 + 1 ], vertsBuffer[indiceBuffer[index+1]*3 + 2], 1.0f};

                        float[] vert3 = new float[]{vertsBuffer[indiceBuffer[index+2]*3], vertsBuffer[indiceBuffer[index+2]*3 + 1 ], vertsBuffer[indiceBuffer[index+2]*3 + 2], 1.0f};

                        index += incrementInIndiceBuffer;

//                        Log.d(TAG, "v1 = (" + vert1[0] + ", " + vert1[1] + ", " + vert1[2] + ")");
//                        Log.d(TAG, "v2 = (" + vert2[0] + ", " + vert2[1] + ", " + vert2[2] + ")");
//                        Log.d(TAG, "v3 = (" + vert3[0] + ", " + vert3[1] + ", " + vert3[2] + ")");
                        // CONVERTS VERTEX COORDINATES TO WORLD SPACE
                        //setRotateEulerM(float[] rm, int rmOffset, float x, float y, float z);
                        foundIntersection = isFoundIntersection(rayPoint, rayDirection, vert1, vert2, vert3);
                        if (foundIntersection != null) {
                            // compute distance to intersection
                            float distance  = ( new Vector( new float [] {
                                    foundIntersection [0] - rayPoint[0]
                                    ,foundIntersection [1] - rayPoint[1]
                                    ,foundIntersection [2] - rayPoint[2] })).magnitude();

                            if (closestIntersection == null) {
                                closestIntersection = foundIntersection;
                                bestDistance = distance;
                            } else {
                                if (distance < bestDistance) {
                                    closestIntersection = foundIntersection;
                                    bestDistance = distance;
                                }
                            }
                        }
                    }
                }
            }
        }
        return closestIntersection;
    }

    private float [] isFoundIntersection( float[] rayPoint, float[] rayDirection,float[] vert1, float[] vert2, float[] vert3) {

        // CONVERTS VERTEX COORDINATES TO WORLD SPACE
        float [] wcVert1 =  convertWorldSpace(vert1);
        float [] wcVert2 =  convertWorldSpace(vert2);
        float [] wcVert3 =  convertWorldSpace(vert3);
//        Log.d(TAG, "wcv1 = (" + wcVert1[0] + ", " + wcVert1[1] + ", " + wcVert1[2] + ")");
//        Log.d(TAG, "wcv2 = (" + wcVert2[0] + ", " + wcVert2[1] + ", " + wcVert2[2] + ")");
//        Log.d(TAG, "wcv3 = (" + wcVert3[0] + ", " + wcVert3[1] + ", " + wcVert3[2] + ")");

        //                Log.d(TAG, "v1 = ("+vert1[0] + ", "+vert1[1] + ", "+vert1[2] + ")");
        //                Log.d(TAG, "v2 = ("+vert2[0] + ", "+vert2[1] + ", "+vert2[2] + ")");
        //                Log.d(TAG, "v3 = ("+vert3[0] + ", "+vert3[1] + ", "+vert3[2] + ")");

        // CHECK IF FACE is CROSSED BY RAY

        float [] targeted = Util.intersect3D_RayTriangle(
                rayPoint // posCam
                , rayDirection //, direction
                , new float [] { wcVert1[0], wcVert1[1], wcVert1[2]}  //triVertex0
                , new float [] { wcVert2[0], wcVert2[1], wcVert2[2]}  //triVertex1
                , new float [] { wcVert3[0], wcVert3[1], wcVert3[2]}  // triVertex2);
        );
        if (targeted != null){
            //Log.d(TAG, "Found intersection point in ( " + targeted[0] + ", "+ targeted[1] + ", "+ targeted[2] + ")");
            return targeted;
        }
        return null;
    }

    abstract short[] getIBOIndices ();
    abstract float[] getIBObuffer (String Type);
    abstract float[] getVBObuffer ();

    abstract void render (float[] mMatrixView, float[] mMatrixProjection,  InterfaceSceneRenderer Scene);

}
