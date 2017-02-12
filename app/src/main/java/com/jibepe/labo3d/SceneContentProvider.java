package com.jibepe.labo3d;



import com.jibepe.model.InterfaceMesh;
import com.jibepe.model.InterfaceSceneGraph;
import com.jibepe.model.InterfaceSceneObject;
import com.jibepe.model.SceneGraph;
import com.jibepe.objparser.ObjLoader;
import com.jibepe.render3d.*;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

/**
 * Created by tbpk7658 on 23/01/2017.
 */
public class SceneContentProvider implements InterfaceSceneRenderer{

    InterfaceSceneGraph mSceneGraph;


    public SceneContentProvider (InterfaceSceneGraph sg) {
        mSceneGraph = sg;

    }

    @Override
    public float [] getCamMatrix() {
        return mSceneGraph.getCamMatrix();
    }


    @Override
    public float[] getCamPos() {
        return mSceneGraph.getCamPos();
    }

    @Override
    public float[] getCamRot() {
        return mSceneGraph.getCamRot();
    }

    @Override
    public List<float[]> getLightsPos() {
        return mSceneGraph.getLightsPos();
    }

    @Override
    public List<glRenderableShape> getRenderableShapes() {

        List <glRenderableShape> shapes2Render = new ArrayList<glRenderableShape>();

        glLine line1 = new glLine();
        line1.setName("Line 001");
        line1.setPosStart(new float [] {0.0f, 0.0f, 0.0f});
        line1.setPosEnd(new float [] {1.0f, 0.0f, 0.0f});
        line1.setColor(new float [] {1.0f, 0.0f, 0.0f, 1.0f}); //red

        shapes2Render.add(line1);

        glLine line2 = new glLine();
        line2.setName("Line 002");

        line2.setPosStart(new float [] {0.0f, 0.0f, 0.0f});
        line2.setPosEnd(new float [] {0.0f, 0.0f, -1.0f});
        line2.setColor(new float [] {0.0f, 1.0f, 0.0f, 1.0f});// green

        shapes2Render.add(line2);


        glLine line3 = new glLine();
        line3.setName("Line 003");
        line3.setPosStart(new float [] {0.0f, 0.0f, 0.0f});
        line3.setPosEnd(new float [] {0.0f, 1.0f, 0.0f});
        line3.setColor(new float [] {0.0f, 0.0f, 1.0f, 1.0f});// blue

        shapes2Render.add(line3);

//        int numberOfLine = 5;
//		for (int ii = -numberOfLine; ii <= numberOfLine; ii ++) {
//            glLine aLine = new glLine();
//            aLine.setPosStart (new float [] {(float)ii, 0.0f, (float)-numberOfLine});
//            aLine.setPosEnd (new float [] {(float)ii, 0.0f, (float) numberOfLine});
//            aLine.setColor(new float [] {0.5f, 0.5f , 0.5f ,1.0f});
//            shapes2Render.add(aLine);
//
//            glLine aLine2 = new glLine();
//            aLine2.setPosStart (new float [] {(float)-numberOfLine, 0.0f, (float)ii});
//            aLine2.setPosEnd (new float [] {(float) numberOfLine, 0.0f, (float)ii});
//            aLine2.setColor(new float [] {0.5f, 0.5f , 0.5f ,1.0f});
//            shapes2Render.add(aLine2);
//
//		}


//        glColoredShape shapeScene = new glColoredShape(mSceneGraph.getObj("scene"));
//        shapeScene.setName("Scene.001");
//        shapeScene.setPosition (new float [] {0.0f, 0.0f, 0.0f});
//        shapeScene.setRotation (new float [] {0.0f, 0.0f, 0.0f});
//
//        shapes2Render.add(shapeScene);

//        glAlphaTexturedShape shapePerso = new glAlphaTexturedShape(mSceneGraph.getObj("plantexture"));
//        shapePerso.setName("PlanTexture.001");
//
//        shapePerso.setPosition (new float [] {1.0f, 1.0f, 0.0f});
//        shapePerso.setRotation (new float [] {0.0f, 90.0f, 0.0f});
//
//        shapes2Render.add(shapePerso);



//        glDaeTexturedShape shapeCube = new glDaeTexturedShape(mSceneGraph.getDae("cubebleu"));
//        shapeCube.setName("CubeBleu.001");
//
//        shapeCube.setPosition (new float [] {0.0f, 0.0f, 0.0f});
//        shapeCube.setRotation (new float [] {0.0f, 00.0f, 0.0f});
//
//        shapes2Render.add(shapeCube);
//


        Square aSquare = new Square();
        aSquare.setName("aSquare.001");
        aSquare.setPosition(new float[] {-1.0f, 1.0f, -3.0f});
        aSquare.setColor(new float[] { 0.76953125f, 0.63671875f, 0.22265625f, 0.0f });
        aSquare.setScale(new float[] {1.0f, 3.0f, 1.0f});
        shapes2Render.add(aSquare);

        Triangle aTriangle = new Triangle();
        aTriangle.setName("aTriangle.001");
        aTriangle.setPosition(new float[] {1.0f, 1.0f, -3.0f});
        aTriangle.setColor(new float[] { 0.63671875f, 0.76953125f, 0.22265625f, 0.0f });
        shapes2Render.add(aTriangle);


//        glTexturedShape anIBOShape = new glTexturedShape();
//        anIBOShape.setName("anIBOShape.001");
//        anIBOShape.setPosition(new float [] {-1.0f, 1.0f, 0.0f});
//        shapes2Render.add(anIBOShape);
//



        List <InterfaceSceneObject> lObject = mSceneGraph.getSceneObjects();
        for (InterfaceSceneObject obj : lObject) {
            shapes2Render.addAll(convert2glShapes(obj));
        }

        return shapes2Render;
    }

    List <glRenderableShape> convert2glShapes(InterfaceSceneObject inObj) {
        List <glRenderableShape> lShapes = new ArrayList<glRenderableShape>();

        List <InterfaceMesh> lMeshes = inObj.getMeshes();

        for (InterfaceMesh mesh: lMeshes){

            String name = inObj.getName()+"."+mesh.getName();
            float [] matrix = inObj.getGlMatrix();
            float [] verts = mesh.getVertices();
            float [] norms = mesh.getNormals();
            float [] uvcoords = mesh.getTexCoordinates();
            short [] indexes = mesh.getFaceIndexes();
            float [] color = mesh.getMaterial().getColor();
            String textureName =  mesh.getMaterial().getTexture();

            if ((verts != null) && (norms != null) && (uvcoords != null) && (indexes != null) && ((textureName != null))) {

                    glVboTexturedShape theShape = new glVboTexturedShape();
                    theShape.setName(name);
                    theShape.setMatrix(matrix);
                    theShape.setVerts(verts);
                    theShape.setMaterialName(mesh.getMaterial().getTexture());
                    theShape.setNorms(norms);
                    theShape.setTexCoords(uvcoords);
                    theShape.setIndexBuffer(indexes);
                    lShapes.add(theShape);
            } else {
                if ((verts != null) && (norms != null) && (color != null)) {
                    glVboColoredShape theShape = new glVboColoredShape();
                    theShape.setName(name);
                    theShape.setMatrix(matrix);
                    theShape.setVerts(verts);
                    theShape.setColor(mesh.getMaterial().getColor());
                    theShape.setNorms(norms);
                    theShape.setTexCoords(uvcoords);

                    theShape.setIndexBuffer(indexes);
                    lShapes.add(theShape);
                }
            }

        }
        return (lShapes);
    }
}
