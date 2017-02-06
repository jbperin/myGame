package com.jibepe.objparser;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;


public class MaterialManager {
    private String TAG = "MaterialManager";
    private static Vector<Texture> mTextures;
    private static List mMatNames = new ArrayList();
    private static AssetManager assetManager = null;

    public MaterialManager(Resources res)
    {
        mTextures = new Vector<Texture>();
        assetManager = res.getAssets();
        addTexture("NoMaterial");
    }
    public int addTexture(String fileName)
    {
        int id = mTextures.size();
        for (int ii=0;ii<id;ii++)
        {
            if (mMatNames.get(ii).equals(fileName)) { return ii; }
        }
        //Arrays.asList(assetManager.list(""))
        try {
            if (Arrays.asList(assetManager.list("")).contains(fileName+".png"))
            {
                mMatNames.add(fileName);
                mTextures.add(Texture.loadTextureFromApk(fileName + ".png", assetManager));
            } else
            {
                if (Arrays.asList(assetManager.list("")).contains(fileName+".jpg"))
                {
                    mMatNames.add(fileName);
                    mTextures.add(Texture.loadTextureFromApk(fileName + ".jpg", assetManager));
                } else
                {
                    Log.d(TAG, "Unable to find : "+fileName);
                    return 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return id;
    }
    public void clear()
    {
        mTextures.clear();
        mTextures = null;
    }
    public String getMaterialName(int Id)
    {
        if (Id>mMatNames.size()) { return "NoMaterial"; }
        return (String) mMatNames.get(Id);
    }
    public static Vector<Texture> getTextures() { return mTextures;}
}
