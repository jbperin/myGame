package com.jibepe.model;

import android.opengl.*;
import android.opengl.Matrix;
import com.dddviewr.collada.Collada;
import com.dddviewr.collada.Source;
import com.dddviewr.collada.animation.Animation;
import com.dddviewr.collada.effects.Effect;
import com.dddviewr.collada.effects.EffectMaterial;
import com.dddviewr.collada.effects.NewParam;
import com.dddviewr.collada.geometry.Geometry;
import com.dddviewr.collada.geometry.Primitives;
import com.dddviewr.collada.materials.InstanceEffect;
import com.dddviewr.collada.materials.Material;
import com.dddviewr.collada.nodes.Node;
import com.dddviewr.collada.visualscene.*;
import com.jibepe.objparser.DaeLoader;
import com.jibepe.objparser.ObjLoader;

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
    public Dictionary<String, ObjLoader> objectDictionary;

    public SceneGraphDae(Collada theCollada) {


        this.theCollada = theCollada;

        dicObjects = new Hashtable<String, InterfaceSceneObject>();
        objectDictionary = new Hashtable<String, ObjLoader>();



        String sceneURL = this.theCollada.getScene().getInstanceVisualScene().getUrl();
        VisualScene vScene = this.theCollada.getLibraryVisualScenes().getScene(sceneURL);
        List <Node> lNodes = vScene.getNodes();
        for (Node nod: lNodes) {
//            List <BaseXform> lXForms = nod.getXforms();
//            lXForms.get(0).getSid();
            //theCollada.nod.getMatrix()
            if (nod.getName().startsWith("Camera")) {
                if (nod.getMatrix() != null) {
                    cameraMatrix = Dae2GlMatrix(nod.getMatrix().getData()); //Dae2GlCamMatrix(nod.getMatrix().getData());
                } else {
                    float[] matrice = getMatrixFromTransforms(nod);
                    cameraMatrix = Dae2GlMatrix(matrice);// Dae2GlCamMatrix(matrice);
                }
            } else if (nod.getName().startsWith("Lamp")) {
                if (nod.getMatrix() != null) {
                    lightMatrix = Dae2GlMatrix(nod.getMatrix().getData());
                } else {
                    float[] matrice = getMatrixFromTransforms(nod);
                    lightMatrix = Dae2GlMatrix(matrice);

                }
            } else {

                String nodeName = nod.getName();
                SceneObject sObject = new SceneObject();
                sObject.setName(nodeName);
                if (nod.getMatrix() != null) {
                    float [] nodeMatrix = Dae2GlMatrix(nod.getMatrix().getData());//Dae2GlCamMatrix(nod.getMatrix().getData());
                    sObject.setGlMatrix(nodeMatrix);
                }else {
                    float[] matrice = getMatrixFromTransforms(nod);
                    sObject.setGlMatrix(Dae2GlMatrix(matrice));

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
                    aMesh.setVertices(verts);
                    aMesh.setNormals(norms);
                    if (theGeom.getMesh().getTexCoordData() != null) {
                        float[] uvcoords = Dae2glTexCoords(theGeom.getMesh().getTexCoordData());
                        aMesh.setTexCoordinates(uvcoords);
                        aMesh.setUVtextured (true);
                    } else {
                        aMesh.setUVtextured (false);
                    }


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


                        String imageId = null;
                        String textureName = "";
                        List<NewParam> lNewParams = theEffect.getNewParams();
                        for (NewParam nParm : lNewParams) {
                            if ((nParm.getSurface() != null) && (nParm.getSurface().getInitFrom() != null)){
                                imageId = nParm.getSurface().getInitFrom();
                            }
                        }
                        if (imageId != null) {
                            textureName = theCollada.findImage(imageId).getInitFrom();
                            mat.setTexture(textureName);
                        }

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


        // ANIMATIONS
        if (this.theCollada.getLibraryAnimations() != null) {
            List<Animation> lAnims = this.theCollada.getLibraryAnimations().getAnimations();
            for (Animation anim : lAnims) {
                Dictionary<String, float[]> dicAnimsData = new Hashtable<String, float[]>();

                List<Source> lSources = anim.getSources();
                String[] types = null;
                for (Source src : lSources) {
                    String anim_name = src.getId();
                    if (src.getFloatArray() != null) {
                        float[] values = src.getFloatArray().getData();
                        dicAnimsData.put(anim_name, values);
                    }
                    if (src.getNameArray() != null) {
                        types = src.getNameArray().getData();
                    }
                }
                if ((types != null) && (types[0].equals("BEZIER"))) {
                    // register a new bezier curve.

                }


            }
        }

    }
    @Override
    public void addObj (String name, ObjLoader obj) {
        objectDictionary.put(name, obj);
    }
    @Override
    public ObjLoader getObj (String objectName) {
        return objectDictionary.get(objectName);
    }

    private float[] getMatrixFromTransforms(Node nod) {
        float [] matrice = new float [16];
        float [] outMatrice = new float [16];
        android.opengl.Matrix.setIdentityM(matrice, 0);

        List<BaseXform> lXForms = nod.getXforms();
        for (BaseXform xForm: lXForms) {
            if (xForm instanceof Translate) {
                Translate txForm = ((Translate) xForm);
                android.opengl.Matrix.translateM(matrice, 0, txForm.getX(), txForm.getY(), txForm.getZ() );
            } else if (xForm instanceof Rotate) {
                Rotate rxForm = ((Rotate) xForm);
                android.opengl.Matrix.rotateM(matrice, 0, rxForm.getAngle(), rxForm.getX(), rxForm.getY(), rxForm.getZ());
            } else if (xForm instanceof Scale) {
                Scale sxForm = ((Scale) xForm);
                android.opengl.Matrix.scaleM(matrice, 0 , sxForm.getX(), sxForm.getY(), sxForm.getZ());
            }

            android.opengl.Matrix.transposeM(outMatrice, 0, matrice, 0);
        }
        return outMatrice;
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
            res[ii+1] = 1.0f - uvcoordData[ii+1]; // TODO expliquer ca
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
        return new float [] {cameraMatrix[12],cameraMatrix[13],cameraMatrix[14]};
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
        //float [] invGlMatrix = new float [16];
        //android.opengl.Matrix.invertM(invGlMatrix, 0, glMatrix, 0);
        float [] transGlMatrix = new float [16];
        android.opengl.Matrix.transposeM(transGlMatrix, 0, glMatrix, 0);
        //android.opengl.Matrix.rotateM(invTransGlMatrix, 0, 180.0f, 1.0f, 0.0f, 0.0f);
        return transGlMatrix;

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
        //float [] rotateY = new float [16];
        //Matrix.setIdentityM(rotateY,0);
        //Matrix.setRotateM(rotateY, 0, angle, 0.0f, 1.0f, 0.0f);
//        float [] currentPosition = new float [] {cameraMatrix[12],cameraMatrix[13],cameraMatrix[14]};
//        cameraMatrix[12] = 0.0f;
//        cameraMatrix[13] = 0.0f;
//        cameraMatrix[14] = 0.0f;
//        Matrix.rotateM(cameraMatrix, 0, angle, 0.0f, 1.0f, 0.0f);
//        cameraMatrix[12] = currentPosition[0];
//        cameraMatrix[13] = currentPosition[1];
//        cameraMatrix[14] = currentPosition[2];
        //cameraMatrix
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
