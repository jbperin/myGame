package com.jibepe.model;

import com.dddviewr.collada.Collada;
import com.dddviewr.collada.effects.Effect;
import com.dddviewr.collada.effects.EffectMaterial;
import com.dddviewr.collada.geometry.Geometry;
import com.dddviewr.collada.geometry.Primitives;
import com.dddviewr.collada.materials.InstanceEffect;
import com.dddviewr.collada.materials.Material;
import com.dddviewr.collada.nodes.Node;
import com.dddviewr.collada.visualscene.*;

import java.nio.ShortBuffer;
import java.util.*;

/**
 * Created by tbpk7658 on 08/02/2017.
 */
public class SceneGraphDae implements InterfaceSceneGraph {

    private String TAG = "SceneGraphDae";

    private Collada theCollada;
    private float [] cameraMatrix = null;
    private float [] lightMatrix = null;
    public Dictionary<String, InterfaceSceneObject> dicObjects;


    public SceneGraphDae(Collada theCollada) {


        this.theCollada = theCollada;

        dicObjects = new Hashtable<String, InterfaceSceneObject>();



        String sceneURL = this.theCollada.getScene().getInstanceVisualScene().getUrl();
        VisualScene vScene = this.theCollada.getLibraryVisualScenes().getScene(sceneURL);
        List <Node> lNodes = vScene.getNodes();
        for (Node nod: lNodes) {
//            List <BaseXform> lXForms = nod.getXforms();
//            lXForms.get(0).getSid();
            //theCollada.nod.getMatrix()
            if (nod.getName().startsWith("Camera")) {
                cameraMatrix = Dae2GlCamMatrix(nod.getMatrix().getData());
            } else if (nod.getName().startsWith("Lamp")) {
                lightMatrix = Dae2GlCamMatrix(nod.getMatrix().getData());
            } else {

                String nodeName = nod.getName();
                SceneObject sObject = new SceneObject();
                sObject.setName(nodeName);
                if (nod.getMatrix() != null) {
                    float [] nodeMatrix = Dae2GlMatrix(nod.getMatrix().getData());//Dae2GlCamMatrix(nod.getMatrix().getData());
                    sObject.setGlMatrix(nodeMatrix);
                }
                List <InstanceGeometry> lInstGeom = nod.getInstanceGeometry();
                for (InstanceGeometry instGeom: lInstGeom) {
                    Mesh aMesh = new Mesh();

                    String geomUrl  = instGeom.getUrl();
                    aMesh.setName(geomUrl);

                    // Buffers of current mesh
                    Geometry theGeom = this.theCollada.findGeometry(geomUrl) ;
                    float [] verts = Dae2glVerts (theGeom.getMesh().getPositionData());
                    float [] norms = Dae2glNorms (theGeom.getMesh().getNormalData());
                    float [] uvcoords = Dae2glTexCoords ( theGeom.getMesh().getTexCoordData());
                    aMesh.setVertices(verts);
                    aMesh.setNormals(norms);
                    aMesh.setTexCoordinates(uvcoords);

                    List <Primitives> lPrim = theGeom.getMesh().getPrimitives();
                    int [] buff = null;
                    for (Primitives prim: lPrim) {
                        buff  = prim.getData();
                    }
                    ShortBuffer bufferObj = ShortBuffer.allocate(buff.length);
                    for (int ii = 0; ii < buff.length; ii++){
                        bufferObj.put((short )(buff[ii]));
                    }

                    aMesh.setFaceIndexes(bufferObj.array());


                    // Material of the current mesh
                    com.jibepe.model.Material mat = new com.jibepe.model.Material();
                    List <InstanceMaterial> lInstMaterial = instGeom.getInstanceMaterials();
                    for (InstanceMaterial instMaterial: lInstMaterial){
                       String materialSymbol =  instMaterial.getSymbol();
                        Material theMaterial = this.theCollada.findMaterial(materialSymbol);
                        InstanceEffect instanceEffect = theMaterial.getInstanceEffect();
                        Effect theEffect = this.theCollada.findEffect(instanceEffect.getUrl());
                        EffectMaterial effMat = theEffect.getEffectMaterial();
                        mat.setColor(effMat.getDiffuse().getData());
//                        List <NewParam> lNewParam = theEffect.getNewParams();
//                        for (NewParam nParam: lNewParam) {
//                            String paramSid = nParam.getSid();
//                        }
                    }
                    aMesh.setMaterial(mat);
                    sObject.addMeshes(aMesh);
                }
                dicObjects.put(sObject.getName(), sObject);
            }
//            Matrix mat = nod.getMatrix();
//            if (nod.getInstanceNode() != null) {
//                String url = nod.getInstanceNode().getUrl();
//                Log.d(TAG, "just read instance: " + url);
//            }
        }

    }

    private float[] Dae2glVerts(float[] positionData) {
        float [] res = new float[positionData.length];
        //int nbVerts = positionData.length /3;
        for (int ii = 0; ii< positionData.length ; ii+=3) {
            res[ii] = positionData[ii];
            res[ii+1] = positionData[ii+1];
            res[ii+2] = positionData[ii+2];
//            res[ii+1] = -positionData[ii+2];
//            res[ii+2] = positionData[ii+1];
        }

        return res;
    }
    private float[] Dae2glNorms(float[] normalData) {
        float [] res = new float[normalData.length];
        for (int ii = 0; ii< normalData.length ; ii+=3) {
            res[ii] = normalData[ii];
            res[ii+1] = normalData[ii+2];
            res[ii+2] = -normalData[ii+1];
        }
        return res;
    }
    private float[] Dae2glTexCoords(float[] uvcoordData) {
        float [] res = new float[uvcoordData.length];
        for (int ii = 0; ii< uvcoordData.length ; ii+=2) {
            res[ii] = uvcoordData[ii];
            res[ii+1] = uvcoordData[ii+1];
        }
        return res;
    }

    @Override
    public List<float[]> getLightsPos() {
        ArrayList<float[]> al = new ArrayList<float[]>();
        if (lightMatrix != null) {
            al.add(new float[] {lightMatrix[12], lightMatrix[13], lightMatrix[14]});
        } else {
            al.add(new float[] {1.0f, 1.0f, 1.0f});
        }
        return (al);

    }

    @Override
    public float[] getCamPos() {
        return new float [] {0.0f, 0.0f, 0.0f};
    }

    @Override
    public float[] getCamRot() {
        return new float [] {0.0f, 0.0f, 0.0f};
    }

    float [] Dae2GlMatrix(float[] daeMatrix) {
        float [] glMatrix = new float []{
                daeMatrix[0], daeMatrix[1], daeMatrix[2], daeMatrix[3]
                , daeMatrix[8], daeMatrix[9], daeMatrix[10], daeMatrix[11]
                , -daeMatrix[4], -daeMatrix[5], -daeMatrix[6], -daeMatrix[7]
                ,daeMatrix[12], daeMatrix[13], daeMatrix[14], daeMatrix[15]
        };
        float [] invGlMatrix = new float [16];
        android.opengl.Matrix.invertM(invGlMatrix, 0, glMatrix, 0);
        float [] invTransGlMatrix = new float [16];
        android.opengl.Matrix.transposeM(invTransGlMatrix, 0, invGlMatrix, 0);
        android.opengl.Matrix.rotateM(invTransGlMatrix, 0, 180.0f, 1.0f, 0.0f, 0.0f);
        return invTransGlMatrix;

    }


    float [] Dae2GlCamMatrix(float[] daeMatrix) {
        float [] glMatrix = new float []{
                daeMatrix[0], daeMatrix[1], daeMatrix[2], daeMatrix[3]
                , daeMatrix[8], daeMatrix[9], daeMatrix[10], daeMatrix[11]
                , -daeMatrix[4], -daeMatrix[5], -daeMatrix[6], -daeMatrix[7]
                ,daeMatrix[12], daeMatrix[13], daeMatrix[14], daeMatrix[15]
        };
        float [] invGlMatrix = new float [16];
        android.opengl.Matrix.invertM(invGlMatrix, 0, glMatrix, 0);
        float [] invTransGlMatrix = new float [16];
        android.opengl.Matrix.transposeM(invTransGlMatrix, 0, invGlMatrix, 0);
        return invTransGlMatrix;

    }
    @Override
    public float[] getCamMatrix() {
        return cameraMatrix;
//        return new float[] {
//                0.6859207f, -0.3240135f, 0.6515582f, 0.0f , //7.481132f,
//                0.0f, 0.8953956f, 0.4452714f, 0.0f , //5.343665f,
//                -0.7276763f, -0.3054208f, 0.6141704f, 0.0f , //-6.50764f,
//                -0.0f,   0.0f, -11.07270875f, 1.0f
//        };
//        return new float[] {
//                0.6859207f,  0.7276763f, 0.0f,       0.0f,
//                -0.3240135f, 0.3054208f, 0.8953956f, 0.0f,
//                0.6515582f, -0.6141704f, 0.4452714f, 0.0f,
//                7.481132f,  -6.50764f,   5.343665f,  1.0f
//        };
//        return new float[] {
//                0.6859207f, -0.3240135f, 0.6515582f, 7.481132f,
//                0.7276763f, 0.3054208f, -0.6141704f, -6.50764f,
//                0.0f, 0.8953956f, 0.4452714f, 5.343665f,
//                0.0f, 0.0f, 0.0f, 1.0f
//        };
//        return new float [] {
//                  1.0f, 0.0f, 0.0f, 0.0f
//                , 0.0f, 1.0f, 0.0f, 0.0f
//                , 0.0f, 0.0f, 1.0f, 0.0f
//                , 0.0f, 0.0f, 0.0f, 1.0f
//        };
    }


    public void rotateCam (float angle ) {

    }
    public void moveCam (float angle ) {

    }

    @Override
    public List<InterfaceSceneObject> getSceneObjects() {
        List <InterfaceSceneObject> lObjects = new ArrayList<InterfaceSceneObject>();

        Enumeration<String> key = this.dicObjects.keys();
        while(key.hasMoreElements()) {
            String objName = key.nextElement();

            lObjects.add(dicObjects.get(objName));
        }
        return lObjects;
    }
}
