package com.jibepe.objparser;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;


public class ColladaLoader {
    private String TAG = "ColladaLoader";

    private static final String ns = null;
    public List matEntries = new ArrayList();
    public List geoEntries = new ArrayList();
    private final Context mContext;
    private MaterialManager mMaterialManager;


    public ColladaLoader(Context context, MaterialManager matManager){
        mContext = context;
        mMaterialManager = matManager;
    }

    public void loadModel(String filename)
            throws XmlPullParserException, IOException {
        InputStream is = null;

        try {
            File folder = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            File colladaFile= new File (folder, filename);
            if (colladaFile.exists()) {
                is = new FileInputStream(colladaFile);
            } else {
                is = mContext.getAssets().open(filename);
            }
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            parser.nextTag();
            readCOLLADA(parser);
        } finally
        {
            is.close();
        }
    }

    private void readCOLLADA(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "COLLADA");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
/*            if (name.equals("library_materials")) {
//                readLibrary_materials(parser);
            } else */
            if (name.equals("library_geometries")) {
                readLibrary_geometries(parser);
            } else if (name.equals("library_visual_scenes")) {
                //   entries.add(readCollada(parser));
            } else if (name.equals("scene")) {
                //   entries.add(readCollada(parser));
            } else {
                skip(parser);
            }
        }
    }
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
    /**********************************************************************************************
     * Lecture de la balise Library_materials.
     * @param parser le parser XML
     * @throws XmlPullParserException
     * @throws IOException
     *********************************************************************************************/
   /* private void readLibrary_materials(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "library_materials");
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }
            String name = parser.getName();
            if (name.equals("material"))
            {
                Material mat = readMaterial(parser);
                matEntries.add(mat);
            } else { skip(parser); }
        }
        parser.require(XmlPullParser.END_TAG, ns, "library_materials");
    }*/

    /**********************************************************************************************
     * Lecture de la balise material.
     * @param parser le parser XML
     * @throws XmlPullParserException
     * @throws IOException
     *********************************************************************************************/
   /* private Material readMaterial(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "material");
        String name = parser.getAttributeValue(null, "name");
        Material mat = new Material(name);
        skip(parser);
        parser.require(XmlPullParser.END_TAG, ns, "material");
        return mat;
    }
*/
    /**********************************************************************************************
     * Lecture de la balise Library_geometries.
     * @param parser le parser XML
     * @throws XmlPullParserException
     * @throws IOException
     *********************************************************************************************/
    private void readLibrary_geometries(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "library_geometries");
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }
            String name = parser.getName();
            if (name.equals("geometry"))
            {
                String geomId = parser.getAttributeValue(null, "id");
                readGeometry(parser,geomId);
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "library_geometries");
    }

    /**********************************************************************************************
     * Lecture de la balise Library_geometries.
     * @param parser le parser XML
     * @throws XmlPullParserException
     * @throws IOException
     *********************************************************************************************/
    private void readGeometry(XmlPullParser parser, String geomId) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "geometry");
        MMesh mesh = null;
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }
            String name = parser.getName();
            if (name.equals("mesh"))
            {
                mesh = readMesh(parser,geomId);
                geoEntries.add(mesh);
            } else { skip(parser); }
        }
        parser.require(XmlPullParser.END_TAG, ns, "geometry");
    }

    /**********************************************************************************************
     * Lecture de la balise Library_geometries.
     * @param parser le parser XML
     * @throws XmlPullParserException
     * @throws IOException
     *********************************************************************************************/
    private MMesh readMesh(XmlPullParser parser,String geomId) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "mesh");
        MMesh mesh = new MMesh();
        ByteBuffer BuffVerts=null;
        ByteBuffer BuffIndices=null;
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }
            String name = parser.getName();
            if (name.equals("source"))
            {
                String srcId = parser.getAttributeValue(null, "id");
                if (srcId.equals(geomId+"-positions"))
                {
                    BuffVerts = readSource(parser,geomId+"-positions");
//                    mesh.setVerts(BuffVerts);
                    continue;
                }
                if (srcId.equals(geomId+"-normals"))
                {
                    ByteBuffer Buff = readSource(parser,geomId+"-normals");
                    mesh.setNorms(Buff);
                    continue;
                }
                if (srcId.equals(geomId+"-map-0"))
                {
                    ByteBuffer Buff = readSource(parser,geomId+"-map-0");
                    mesh.setUVs(Buff);
                    continue;
                }
                skip(parser);
            } else if (name.equals("polylist"))
            {
                BuffIndices = readpolylist(parser,mesh);
                mesh.setIndices(BuffIndices);
            } else {
                skip(parser);
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "mesh");

//        // TODO : Revoir l'algo pour enlever cette hérésie !
//        float f;
//        int cnt = mesh.getNumObjectIndex();
//        ByteBuffer Buff;
//        Buff = ByteBuffer.allocateDirect(cnt * 4*3);
//        Buff.order(ByteOrder.LITTLE_ENDIAN);
//        int ind =0;
//        for (int ii=0;ii<cnt;ii++)
//        {
//            ind = BuffIndices.getShort(ii*2);
//            f = BuffVerts.getFloat((ind*3)*4);
//            Buff.putFloat(f);
//            f = BuffVerts.getFloat((ind*3+1)*4);
//            Buff.putFloat(f);
//            f = BuffVerts.getFloat((ind*3+2)*4);
//            Buff.putFloat(f);
//        }
//        Buff.rewind();
//        mesh.setVerts(Buff);

        return mesh;
    }

    private ByteBuffer readpolylist(XmlPullParser parser,MMesh geom) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "polylist");
        ByteBuffer Buff = null;

        String materialtag = parser.getAttributeValue(null, "material");
        String matName = materialtag.substring(0,materialtag.length()-9); // -9 = "-material"
        Log.d (TAG, "Have to deal with material " + matName);
        Material material = new Material(matName, mMaterialManager.addTexture(matName));
//        geom.setMaterialId(material.getMaterialID());

        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }
            String name = parser.getName();
            if (name.equals("p"))
            {
                Buff = parseIndices(readText(parser));
            } else { skip(parser); }
        }
        parser.require(XmlPullParser.END_TAG, ns, "polylist");
        return Buff;
    }

    private ByteBuffer readSource(XmlPullParser parser,String ArrayId) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "source");
        ByteBuffer Buff = null;
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }
            String name = parser.getName();
            if (name.equals("float_array"))
            {
                String srcId = parser.getAttributeValue(null, "id");
                if (srcId.equals(ArrayId+"-array"))
                {
                    Buff = parseFloat(readText(parser));
                }
            } else { skip(parser); }
        }
        parser.require(XmlPullParser.END_TAG, ns, "source");
        return Buff;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private ByteBuffer parseFloat(String txt)
    {
        String[] Words = txt.split("\\s+");
        int cnt = Words.length;
        ByteBuffer Buff = ByteBuffer.allocateDirect(cnt * 4);
        Buff.order(ByteOrder.LITTLE_ENDIAN);
        float f;
        for (int ii=0;ii<cnt;ii++)
        {
            f = Float.parseFloat(Words[ii]);
            Buff.putFloat(f);
        }
        Buff.rewind();
        return Buff;
    }

    // On récupère la liste des indices... Les indices SONT DES SHORTS GRRRRRrrrrr !!!!!!
    /*
       <input semantic="VERTEX" source="#Cube-mesh-vertices" offset="0"/>
       <input semantic="NORMAL" source="#Cube-mesh-normals" offset="1"/>
       <input semantic="TEXCOORD" source="#Cube-mesh-map-0" offset="2" set="0"/>
     */
    private ByteBuffer parseIndices(String txt)
    {
        String[] Words = txt.split("\\s+");
        int cnt = Words.length/3;
        ByteBuffer Buff = ByteBuffer.allocateDirect(cnt * 2);
        Buff.order(ByteOrder.LITTLE_ENDIAN);
        for (int ii=0;ii<cnt;ii++)
        {
            Short s = Short.parseShort(Words[ii*3]);
            Buff.putShort(s);
        }
        Buff.rewind();
        return Buff;
    }

}
