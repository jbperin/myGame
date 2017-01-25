package com.jibepe.model;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLES20;
import com.jibepe.objparser.ObjLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by tbpk7658 on 24/01/2017.
 */
public class GrosBordel {

    //private ObjLoader mSceneLoader;
    private ObjLoader mObjLoader;


    private Context mContext;

    private int mAlphaTextureDataHandle, mTextureDataHandle;
    private Dictionary<String, Integer> dTextureHandlers;

    public GrosBordel(Context context) {
        //this.mSceneLoader = new ObjLoader(context);
        this.mObjLoader = new ObjLoader(context);
        this.mContext = context;



        // Load models
        //mSceneLoader.loadModel("scene");
        mObjLoader.loadModel("plantexture");

        // Identify the texture to load
        List<String> lTexture = new ArrayList<String>() ;
        //mSceneLoader.listTexture(lTexture);
        mObjLoader.listTexture(lTexture);


        dTextureHandlers = new Hashtable<String, Integer>();

        // Load the texture
        AssetManager assetManager = mContext.getAssets();
        ListIterator<String> it = lTexture.listIterator() ;
        while(it.hasNext()) {
            String texture_filename = it.next();
            InputStream inObj;
            try {
                inObj = assetManager.open(texture_filename);
                //int textureId = TextureHelper.loadPNGTexture(mContext, inObj);
                //dTextureHandlers.put(texture_filename, textureId);
                GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }
}
