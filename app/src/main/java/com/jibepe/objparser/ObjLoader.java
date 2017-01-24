package com.jibepe.objparser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.text.MessageFormat;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.momchil_atanasov.data.front.error.WFException;
import com.momchil_atanasov.data.front.parser.IMTLParser;
import com.momchil_atanasov.data.front.parser.IOBJParser;
import com.momchil_atanasov.data.front.parser.MTLColor;
import com.momchil_atanasov.data.front.parser.MTLLibrary;
import com.momchil_atanasov.data.front.parser.MTLMaterial;
import com.momchil_atanasov.data.front.parser.MTLParser;
import com.momchil_atanasov.data.front.parser.OBJDataReference;
import com.momchil_atanasov.data.front.parser.OBJFace;
import com.momchil_atanasov.data.front.parser.OBJMesh;
import com.momchil_atanasov.data.front.parser.OBJModel;
import com.momchil_atanasov.data.front.parser.OBJNormal;
import com.momchil_atanasov.data.front.parser.OBJObject;
import com.momchil_atanasov.data.front.parser.OBJParser;
import com.momchil_atanasov.data.front.parser.OBJTexCoord;
import com.momchil_atanasov.data.front.parser.OBJVertex;

public class ObjLoader {

	public Dictionary<String, MaterialShape> dictionary;
	private final Context mContext;
	private final String TAG="ObjLoader";
	
	public ObjLoader(Context context){
		mContext = context;
	}
	/**
	 * @param args
	 */
	public void  loadModel (String sceneFilename) {// {int objRessourceId, int mtlRessourceId) {
		// Open a stream to your OBJ resource
		InputStream inObj;
		InputStream inMtl;
		AssetManager assetManager = mContext.getAssets();
		dictionary = new Hashtable<String, MaterialShape>();
		
		try {
			inObj = assetManager.open(sceneFilename+".obj"); // mContext.getResources().openRawResource(objRessourceId);
			inMtl = assetManager.open(sceneFilename+".mtl"); //mContext.getResources().openRawResource(mtlRessourceId);

			// Create an OBJParser and parse the resource
			final IOBJParser objParser = new OBJParser();
			final IMTLParser mtlParser = new MTLParser();
			OBJModel model;
			MTLLibrary library;
			try {

				library = mtlParser.parse(inMtl);
				model = objParser.parse(inObj);

				buildBuffersByMaterials(model, library);

				
			} catch (WFException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


	}

	private void buildBuffersByMaterials(OBJModel model,
			MTLLibrary library) {
		// Use the model representation to get some basic info
		Log.d(TAG, 
		        "OBJ model has "+ model.getVertices().size()+" vertices, "+  model.getNormals().size()+" normals, "+  model.getTexCoords().size()+" texture coordinates, and "+  model.getObjects().size()+" objects.");
		
		for (MTLMaterial material : library.getMaterials()) {
			float bufferObj[] = getFaceByMaterial(model, material.getName());
			String fileName = null;
//		    System.out.println(MessageFormat.format("Material with name ``{0}``.", material.getName()));
		    MTLColor color = material.getDiffuseColor();
		    
		    String filePath = material.getDiffuseTexture();
            if (filePath != null) {
                //            Log.d(TAG, "OBJ texture is " + material.getDiffuseTexture());
                int slash = filePath.lastIndexOf("/");
                int backslash = filePath.lastIndexOf("\\");
                if ((slash == -1) && (backslash == -1))
                {
                	fileName = filePath;
                }
                else
                {
                	fileName = filePath.substring(slash > backslash ? slash : backslash);
                }
                Log.d(TAG, "OBJ texture is " + fileName);
            }

//		    System.out.println(MessageFormat.format("color ({0}; {1}; {2}).", color.r, color.g, color.b));
//		    for (int ii=0; ii<bufferObj.length; ii+=3){
//		    	System.out.println(""+bufferObj[ii]+", "+ bufferObj[ii+1]+ ", "+ bufferObj[ii+2]);
//		    }
		    float [] col = {color.r, color.g, color.b};
		    dictionary.put(material.getName(), new MaterialShape (col , bufferObj, fileName));
		}
	}

	private float[] getFaceByMaterial(OBJModel model, String pMaterialName) {

		
		int nb_fl = 0;
		boolean hasUVcoords = false;

		// determine if material has UVcoords
		for (OBJObject object : model.getObjects()) {

			for (OBJMesh mesh : object.getMeshes()) {
		    	final String materialName = mesh.getMaterialName();
		    	if (materialName.compareTo(pMaterialName)==0) {

		    		for (OBJFace face : mesh.getFaces()) {
			        	for (OBJDataReference reference : face.getReferences()) {
			        		if (reference.hasTexCoordIndex()) {
			        			hasUVcoords = true;
			        		}
			        	}
		    		}
		    	}
			}
		}
		
		
		// compute number of float to be allocated to store the model
		for (OBJObject object : model.getObjects()) {

			for (OBJMesh mesh : object.getMeshes()) {
		    	final String materialName = mesh.getMaterialName();
		    	if (materialName.compareTo(pMaterialName)==0) {
		    		int nb_float_per_vertice =  (3+3); // 3 float coord + 3 float normal 
		        	if (hasUVcoords) {
		        		// take into account uv coordinates.
		        		nb_float_per_vertice += 2; // 2 float per UVcoord
		        	}
		    		nb_fl = nb_fl + mesh.getFaces().size()*nb_float_per_vertice*3;// 3 verts/face
		    	}
			}
		}

		
		FloatBuffer bufferObj = FloatBuffer.allocate(nb_fl);

		
		for (OBJObject object : model.getObjects()) {

			for (OBJMesh mesh : object.getMeshes()) {
		    	final String materialName = mesh.getMaterialName();
		    	if (materialName.compareTo(pMaterialName)==0) {

		    		for (OBJFace face : mesh.getFaces()) {

//		    			System.out.println(MessageFormat.format(
//			        			"OBJ {1} face {0}",face.toString(), object.getName())
//			        	);
			        	for (OBJDataReference reference : face.getReferences()) {
			        	    final OBJVertex vertex = model.getVertex(reference);
			        	    bufferObj.put(vertex.x);
			        	    bufferObj.put(vertex.y);
			        	    bufferObj.put(vertex.z);
//			        	    System.out.println(MessageFormat.format(
//			        	        "Vertex ({0}, {1}, {2})", vertex.x, vertex.y, vertex.z));
			        	    if (reference.hasNormalIndex()) {
			        	        final OBJNormal normal = model.getNormal(reference);
				        	    bufferObj.put(normal.x);
				        	    bufferObj.put(normal.y);
				        	    bufferObj.put(normal.z);
//			        	        System.out.println(MessageFormat.format(
//			        	            "Normal ({0}, {1}, {2})", normal.x, normal.y, normal.z));
			        	    }
			        	    if (reference.hasTexCoordIndex()) {
			        	        final OBJTexCoord texCoord = model.getTexCoord(reference);
				        	    bufferObj.put(texCoord.u);
				        	    bufferObj.put(1.0f-texCoord.v); // TODO expliqer ca
//			        	        System.out.println(MessageFormat.format(
//			        	            "TexCoord ({0}, {1})", texCoord.u, texCoord.v));
			        	    }
			        	}
			        	
			        }
		    	}
		    	
		    }
		}
		return (bufferObj.array());
	}

	public void listTexture(List<String> ltex) {
		 
        Enumeration<String> key = this.dictionary.keys();
        while(key.hasMoreElements()){
        	String matName = key.nextElement();
        	MaterialShape matShape = (MaterialShape)this.dictionary.get(matName);
        	if (matShape.texturename != null) {
        		if (! ltex.contains(matShape.texturename) )
        			ltex.add(matShape.texturename);
        	}
        }
	}

}
