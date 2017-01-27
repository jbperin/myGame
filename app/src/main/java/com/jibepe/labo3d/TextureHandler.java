package com.jibepe.labo3d;

import android.content.Context;
import android.content.res.AssetManager;
import com.jibepe.util.TextureHelper;

import java.io.IOException;
import java.io.InputStream;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tbpk7658 on 25/01/2017.
 */
public class TextureHandler {

    private Context mContext;

    private Map<String, Integer> dTextureHandlers;

    private static TextureHandler INSTANCE = new TextureHandler();

    private TextureHandler() {
        dTextureHandlers = new HashMap<String, Integer>();
    }
    public void reset(){
        dTextureHandlers.clear();
        //dTextureHandlers = new HashMap<String, Integer>();
    }
    public static TextureHandler getInstance()

    {
        return INSTANCE;
    }

    public void setContext (Context context) {
        mContext = context;
    }

    public int getTextureId(String filename) {
        if (! dTextureHandlers.containsKey(filename)){
            return (loadPNGTexture (filename));
        } else {
            return (dTextureHandlers.get(filename));
        }
    }

    public int loadPNGTexture(String filename) {
        int textureId = 0;
        // Load the texture
        AssetManager assetManager = mContext.getAssets();
        try {
            InputStream inObj = assetManager.open(filename);

            textureId = TextureHelper.loadPNGTexture(inObj);

            dTextureHandlers.put(filename, textureId);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return (textureId);
    }
}
