package com.jibepe.model;

/**
 * Created by tbpk7658 on 09/02/2017.
 */
public class Material implements InterfaceMaterial {

    public void setColor(float[] color) {
        this.color = color;
    }

    private float[] color = {0.5f, 0.5f, 0.5f, 1.0f};
    private String texture = null;

    public Material() {
        ;
    }
    public Material(float[] color) {
        this.color = color;
    }

    public Material(String texture) {
        this.texture = texture;
    }

    @Override
    public float[] getColor() {
        return color;
    }

    @Override
    public String getTexture() {
        return texture;
    }
}
