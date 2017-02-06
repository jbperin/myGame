package com.jibepe.objparser;


public class Material {
    private int mMatID;
    private String name = "";
    public Material (String name, int matID)
    {
        this.name = name;
        mMatID = matID;
    }

    public int getMaterialID() { return mMatID; }
}
