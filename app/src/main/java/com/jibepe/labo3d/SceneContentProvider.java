package com.jibepe.labo3d;



import com.jibepe.model.SceneGraph;
import com.jibepe.render3d.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tbpk7658 on 23/01/2017.
 */
public class SceneContentProvider implements InterfaceSceneRenderer{

    SceneGraph mSceneGraph;


    public SceneContentProvider (SceneGraph sg) {
        mSceneGraph = sg;

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

        int numberOfLine = 5;
		for (int ii = -numberOfLine; ii <= numberOfLine; ii ++) {
            glLine aLine = new glLine();
            aLine.setPosStart (new float [] {(float)ii, 0.0f, (float)-numberOfLine});
            aLine.setPosEnd (new float [] {(float)ii, 0.0f, (float) numberOfLine});
            aLine.setColor(new float [] {0.5f, 0.5f , 0.5f ,1.0f});
            shapes2Render.add(aLine);

            glLine aLine2 = new glLine();
            aLine2.setPosStart (new float [] {(float)-numberOfLine, 0.0f, (float)ii});
            aLine2.setPosEnd (new float [] {(float) numberOfLine, 0.0f, (float)ii});
            aLine2.setColor(new float [] {0.5f, 0.5f , 0.5f ,1.0f});
            shapes2Render.add(aLine2);

		}

//        glColoredShape shapeScene = new glColoredShape(mSceneGraph.getObj("scene"));
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
//
//
//        Square aSquare = new Square();
//        aSquare.setName("aSquare.001");
//        aSquare.setPosition(new float[] {-1.0f, 1.0f, -3.0f});
//        aSquare.setColor(new float[] { 0.76953125f, 0.63671875f, 0.22265625f, 0.0f });
//        shapes2Render.add(aSquare);
//
//        Triangle aTriangle = new Triangle();
//        aTriangle.setName("aTriangle.001");
//        aTriangle.setPosition(new float[] {1.0f, 1.0f, -3.0f});
//        aTriangle.setColor(new float[] { 0.63671875f, 0.76953125f, 0.22265625f, 0.0f });
//        shapes2Render.add(aTriangle);
//
//
//        glTexturedShape anIBOShape = new glTexturedShape();
//        anIBOShape.setName("anIBOShape.001");
//        anIBOShape.setPosition(new float [] {-1.0f, 1.0f, 0.0f});
//        shapes2Render.add(anIBOShape);
//

        return shapes2Render;
    }


}
