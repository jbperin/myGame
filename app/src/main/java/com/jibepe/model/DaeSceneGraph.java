package com.jibepe.model;

import android.util.Log;
import com.dddviewr.collada.Collada;
import com.dddviewr.collada.nodes.Node;
import com.dddviewr.collada.visualscene.BaseXform;
import com.dddviewr.collada.visualscene.Matrix;
import com.dddviewr.collada.visualscene.VisualScene;

import java.util.List;

/**
 * Created by tbpk7658 on 08/02/2017.
 */
public class DaeSceneGraph implements InterfaceSceneGraph {

    private String TAG = "DaeSceneGraph";

    private Collada theCollada;
    private float [] cameraMatrix = null;
    public DaeSceneGraph(Collada theCollada) {

        this.theCollada = theCollada;
        String sceneURL = theCollada.getScene().getInstanceVisualScene().getUrl();
        VisualScene vScene = theCollada.getLibraryVisualScenes().getScene(sceneURL);
        List <Node> lNodes = vScene.getNodes();
        for (Node nod: lNodes) {
            List <BaseXform> lXForms = nod.getXforms();
            lXForms.get(0).getSid();
            //theCollada.nod.getMatrix()
            if (nod.getName().startsWith("Camera")) {
                cameraMatrix = Dae2GlMatrix(nod.getMatrix().getData());
            }
            Matrix mat = nod.getMatrix();
            if (nod.getInstanceNode() != null) {
                String url = nod.getInstanceNode().getUrl();
                Log.d(TAG, "just read instance: " + url);
            }
        }

    }

    @Override
    public List<float[]> getLightsPos() {

        return null;
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
                daeMatrix[0], daeMatrix[1], daeMatrix[2], 0.0f
                    , daeMatrix[8], daeMatrix[9], daeMatrix[10], 0.0f
                    , -daeMatrix[4], -daeMatrix[5], -daeMatrix[6], 0.0f
                    , 0.0f, 0.0f, 0.0f, 1.0f
        };
        float posX = daeMatrix[3]*daeMatrix[0] + daeMatrix[11]*daeMatrix[1] + daeMatrix[7]*(daeMatrix[2]);
        float posY = daeMatrix[3]*daeMatrix[8] + daeMatrix[11]*daeMatrix[9] + daeMatrix[7]*(daeMatrix[10]);
        float posZ = daeMatrix[3]*(-daeMatrix[4]) + daeMatrix[11]*(-daeMatrix[5]) + daeMatrix[7]*(-daeMatrix[6]);
        glMatrix [12] = posX;
        glMatrix [13] = posY;
        glMatrix [14] = posZ;
        return glMatrix;

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
}
