package com.jibepe.objparser;


import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import com.dddviewr.collada.Collada;
import com.dddviewr.collada.Param;
import com.dddviewr.collada.Source;
import com.dddviewr.collada.effects.Effect;
import com.dddviewr.collada.effects.EffectMaterial;
import com.dddviewr.collada.effects.NewParam;
import com.dddviewr.collada.geometry.Geometry;
import com.dddviewr.collada.geometry.Mesh;
import com.dddviewr.collada.geometry.Primitives;
import com.dddviewr.collada.materials.Material;
import com.dddviewr.collada.nodes.Node;
import com.momchil_atanasov.data.front.parser.OBJNormal;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tbpk7658 on 07/02/2017.
 */
public class DaeLoader {

    private String TAG = "DaeLoader";

    public Collada getTheCollada() {
        return theCollada;
    }

    Collada theCollada = null;
    File mFolder = null;
    Context mContext;

    public DaeLoader(Context context){
        mContext = context;
        mFolder = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);


    }

    public void  loadModel (String sceneFilename) {
        File infile = new File(mFolder, sceneFilename);
        if (! infile.exists()) {
            AssetManager assetManager = mContext.getAssets();
            try {
                if (Arrays.asList(assetManager.list("")).contains(sceneFilename)) {
                    copyAssets(new String[] {sceneFilename});
                    infile = new File(mFolder, sceneFilename);
                } else {
                    infile = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                infile = null;
            }

        }
        if ((infile != null) && (infile.exists())){
            try {
                theCollada = Collada.readFile(infile.toString());
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private void copyAssets(String[] files) {
        AssetManager assetManager = mContext.getAssets();
//        String[] files = null;
//        try {
//            files = assetManager.list("");
//        } catch (IOException e) {
//            Log.e("tag", "Failed to get asset file list.", e);
//        }
        for(String filename : files) {
            boolean fileExist = false;
            try {
                fileExist = Arrays.asList(assetManager.list("")).contains(filename);
            } catch (IOException e) {
                //e.printStackTrace();
                Log.d(TAG, "File not found in assets" + filename, e);
            }
            if (fileExist) {
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = assetManager.open(filename);
                    File outFile = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), filename);
                    out = new FileOutputStream(outFile);
                    copyFile(in, out);
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                } catch (IOException e) {
                    Log.e(TAG, "Failed to copy asset file: " + filename, e);
                }
            }
        }
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }



    public float [] getVerticesBuffer () {
        float[] buff = null;
        List<Geometry> lGeom = theCollada.getLibraryGeometries().getGeometries();
        for (Geometry geom: lGeom) {
            Mesh aMesh = geom.getMesh();
            buff = aMesh.getPositionData();

        }
        return buff;

    }
    public float [] getNormalsBuffer (){

        float[] buff = null;
        List<Geometry> lGeom = theCollada.getLibraryGeometries().getGeometries();
        for (Geometry geom: lGeom) {
            Mesh aMesh = geom.getMesh();
            buff = aMesh.getNormalData();

        }
        return buff;

    }

    public float [] getTextureCoordinatesBuffer (){
        float[] buff = null;
        List<Geometry> lGeom = theCollada.getLibraryGeometries().getGeometries();
        for (Geometry geom: lGeom) {
            Mesh aMesh = geom.getMesh();
            buff = aMesh.getTexCoordData();

        }
        return buff;

    }
    public short[] getFaceIndexBuffer() {
        int[] buff = null;
        List<Geometry> lGeom = theCollada.getLibraryGeometries().getGeometries();
        //List<Node> lNode = theCollada.getLibraryNodes().getNodes();
        //theCollada.
        //for (Node nod: lNode) {
            //nod.
        //}

        for (Geometry geom: lGeom) {

            Mesh aMesh = geom.getMesh();
            List <Primitives> lPrim = aMesh.getPrimitives();
            for (Primitives prim: lPrim) {
                buff  = prim.getData();
            }
//            List <Source> lSources =  aMesh.getSources();
//            for (Source src: lSources){
//                List<Param> lParam = src.getAccessor().getParams();
//                for (Param prm : lParam) {
//                    prm.getName();
//                    prm.getType();
//                }
//                src.getId();
//            }
            //buff = aMesh.;

        }


        ShortBuffer bufferObj = ShortBuffer.allocate(buff.length);
        for (int ii = 0; ii < buff.length; ii++){
            bufferObj.put((short )(buff[ii]));
        }
        return bufferObj.array();
    }

    public float [] getMeshTextureCoordinatesBuffer (String id){
        float[] buff = null;
        Geometry geom = theCollada.findGeometry(id);

        Mesh aMesh = geom.getMesh();
        buff = aMesh.getTexCoordData();


        return buff;

    }

    public float [] getMeshVerticesBuffer (String id) {
        float[] buff = null;
        Geometry geom = theCollada.findGeometry(id);
        Mesh aMesh = geom.getMesh();
        buff = aMesh.getPositionData();


        return buff;

    }
    public float [] getMeshNormalsBuffer (String id){

        float[] buff = null;
        Geometry geom = theCollada.findGeometry(id);
        Mesh aMesh = geom.getMesh();
        buff = aMesh.getNormalData();


        return buff;

    }
    public String getMeshTextureName(String geomId) {
        String textureName = "";
        String materialName = "";
        String imageId = null;
        Geometry geom = theCollada.findGeometry(geomId);
        List <Primitives> lPrim = geom.getMesh().getPrimitives();
        for (Primitives prim : lPrim) {
            materialName = prim.getMaterial();

        }
        Material mat = theCollada.findMaterial(materialName);
        String matUrl = mat.getInstanceEffect().getUrl();
        Effect eff = theCollada.findEffect(matUrl.substring(1));

        EffectMaterial effmat = eff.getEffectMaterial();
        List<NewParam> lNewParams = eff.getNewParams();
        for (NewParam nParm : lNewParams) {
            if ((nParm.getSurface() != null) && (nParm.getSurface().getInitFrom() != null)){
                imageId = nParm.getSurface().getInitFrom();
            }
        }
        if (imageId != null) {
            textureName = theCollada.findImage(imageId).getInitFrom();
        }
        return textureName;
    }

    public short[] getMeshFaceIndexBuffer(String id) {
        int[] buff = null;
        Geometry geom = theCollada.findGeometry(id);
        Mesh aMesh = geom.getMesh();
        List <Primitives> lPrim = aMesh.getPrimitives();
        for (Primitives prim: lPrim) {
            buff  = prim.getData();
        }
        ShortBuffer bufferObj = ShortBuffer.allocate(buff.length);
        for (int ii = 0; ii < buff.length; ii++){
            bufferObj.put((short )(buff[ii]));
        }
        return bufferObj.array();
    }
}
