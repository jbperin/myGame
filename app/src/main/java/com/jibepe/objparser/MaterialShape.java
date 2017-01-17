package com.jibepe.objparser;

public class MaterialShape {
	public float r;
	public float g;
	public float b;
	public float [] buffer;
	public String texturename;
	
	MaterialShape (float [] color, float [] buffer, String filename){
		this.r = color[0];
		this.g = color[1];
		this.b = color[2];
		this.buffer = buffer;
		this.texturename = filename;
	}
	
}
