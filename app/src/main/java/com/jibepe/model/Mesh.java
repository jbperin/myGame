package com.jibepe.model;

/**
 * Created by tbpk7658 on 09/02/2017.
 */
public class Mesh implements InterfaceMesh {


    public void setName(String name) {
        this.name = name;
    }

    private String name = null;
    @Override
    public String getName() {
        return name;
    }

    float[] vertices = null;
    @Override
    public float[] getVertices() {
        return vertices;
    }

    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    public void setNormals(float[] normals) {
        this.normals = normals;
    }

    public void setTexCoordinates(float[] texCoordinates) {
        this.texCoordinates = texCoordinates;
    }

    public void setFaceIndexes(short[] faceIndexes) {
        this.faceIndexes = faceIndexes;
    }

    public void setMaterial(InterfaceMaterial material) {
        this.material = material;
    }

    float[] normals = null;
    @Override
    public float[] getNormals() {
        return normals;
    }

    float[] texCoordinates = null;
    @Override
    public float[] getTexCoordinates() {
        return texCoordinates;
    }

    short[] faceIndexes = null;
    @Override
    public short[] getFaceIndexes() {
        return faceIndexes;
    }

    InterfaceMaterial material = null;
    @Override
    public InterfaceMaterial getMaterial() {
        return material;
    }
}
