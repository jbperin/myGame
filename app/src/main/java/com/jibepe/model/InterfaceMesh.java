package com.jibepe.model;

/**
 * Created by tbpk7658 on 09/02/2017.
 */
public interface InterfaceMesh {
    String getName();
    float [] getVertices(); // v0.x, v0.y, v0.z, v1.x, v1.y,, .., vn.z
    float [] getNormals ();
    float [] getTexCoordinates ();
    short [] getFaceIndexes ();

    InterfaceMaterial getMaterial ();


}
