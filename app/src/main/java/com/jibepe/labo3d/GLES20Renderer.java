package com.jibepe.labo3d;


import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.opengl.GLSurfaceView.Renderer;

public class GLES20Renderer implements Renderer {

	/** Store the projection matrix. This is used to project the scene onto a 2D viewport. */
	private float[] mProjectionMatrix = new float[16];
	/**
	 * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
	 * it positions things relative to our eye.
	 */
	private float[] mViewMatrix = new float[16];


	/**
	 * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
	 * of being located at the center of the universe) to world space.
	 */
	private float[] mVPMatrix = new float[16];

    private InterfaceSceneRenderer mScene;


    public GLES20Renderer(InterfaceSceneRenderer scene) {
		super();
		mScene = scene;
	}


	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the clear color
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		// Use culling to remove back faces.
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		
		// Enable depth testing
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);
 
		// Create a new perspective projection matrix. The height will stay the same
		// while the width will vary as per aspect ratio.
		final float ratio = (float) width / height;
		
		Matrix.frustumM(mProjectionMatrix, 0, 
				-ratio, ratio, // left, right, 
				-1.0f, 1.0f,// bottom, top, 
				1.0f, 70.0f// near, far
				);
	}

	@Override
	public void onDrawFrame(GL10 gl) {


        // clear Screen and Depth Buffer, we have set the clear color as black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        //GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        
        // Set the view matrix. This matrix can be said to represent the camera position.
		// NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
		// view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        float [] mPosCam = mScene.getCamPos();
		float mPosCamX  = mPosCam [0];
        float mPosCamY = mPosCam [1];
        float [] mRotCam = mScene.getCamRot();
        float mAngleCam = mRotCam [1];

		Matrix.setLookAtM(mViewMatrix, 0, 
				// Position the eye in front of the origin.
				mPosCamX, 1.0f, mPosCamY,// eyeX, eyeY, eyeZ, 
				// We are looking toward the distance
				(float)(mPosCamX + (Math.cos(Math.toRadians(mAngleCam)))), 1.0f, (float)(mPosCamY + (Math.sin(Math.toRadians(mAngleCam)))), // lookX, lookY, lookZ, 
				// Set our up vector. This is where our head would be pointing were we holding the camera.
				0.0f, 1.0f, 0.0f  //upX, upY, upZ
				);		
		//Matrix.multiplyMM(mVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);


        List<glRenderableShape>ShapesToRender = mScene.getRenderableShapes();
        for (glRenderableShape shap : ShapesToRender) {
            //
            // shap.render(mVPMatrix, mShaderHelper, mScene);
            shap.render(mViewMatrix, mProjectionMatrix, mScene);
			checkGLError();
        }

	}

    void checkGLError()
    {
        for (int error = GLES20.glGetError(); error != 0; error = GLES20.glGetError())
            ;
    }

}

//		final float uvplan_buffer []=
//			{
//
//				-1.0f, 1.0f, 0.0f,  // Pt 1
//				0.0f, 0.0f, -1.0f,
//				1.0f, 0.0f,
//
//				1.0f, 1.0f, 0.0f,  // Pt 2
//				0.0f, 0.0f, -1.0f,
//				0.0f, 0.0f,
//
//				1.0f, -1.0f, 0.0f,   // Pt 3
//				0.0f, 0.0f, -1.0f,
//				0.0f, 1.0f,
//
//				-1.0f, -1.0f, 0.0f,  // Pt4
//				0.0f, 0.0f, -1.0f,
//				1.0f, 1.0f,
//
//				-1.0f, 1.0f, 0.0f,  // Pt 1
//				0.0f, 0.0f, -1.0f,
//				1.0f, 0.0f,
//
//				1.0f, -1.0f, 0.0f,   // Pt 3
//				0.0f, 0.0f, -1.0f,
//				0.0f, 1.0f,
//
//
////				-1.0f, 0.0f, -1.0f,  // Pt 1
////				0.0f, 1.0f, 0.0f,
////				0.0f, 0.0f,
////
////				-1.0f, 0.0f, 1.0f,  // Pt 2
////				0.0f, 1.0f, 0.0f,
////				0.0f, 1.0f,
////
////				1.0f, 0.0f, 1.0f,   // Pt 3
////				0.0f, 1.0f, 0.0f,
////				1.0f, 1.0f,
////
////				1.0f, 0.0f, -1.0f,  // Pt4
////				0.0f, 1.0f, 0.0f,
////				1.0f, 0.0f,
////
////				-1.0f, 0.0f, -1.0f,  // Pt 1
////				0.0f, 1.0f, 0.0f,
////				0.0f, 0.0f,
////
////				1.0f, 0.0f, 1.0f,   // Pt 3
////				0.0f, 1.0f, 0.0f,
////				1.0f, 1.0f,
////
//			};



