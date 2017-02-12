package com.jibepe.render3d;


import java.util.List;
//import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.*;
import android.opengl.GLSurfaceView.Renderer;
import android.os.ConditionVariable;
import android.util.Log;
import com.jibepe.labo3d.InterfaceSceneRenderer;
import com.jibepe.math.Vector;
import com.jibepe.recorder.GameRecorder;

public class GLES20Renderer implements Renderer {

	private String TAG = "GLES20Renderer";

	/** Store the projection matrix. This is used to project the scene onto a 2D viewport. */
	private float[] mProjectionMatrix = new float[16];
	/**
	 * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
	 * it positions things relative to our eye.
	 */
	private float[] mViewMatrix = new float[16];

	public int mWidth, mHeight;
	/**
	 * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
	 * of being located at the center of the universe) to world space.
	 */
	private float[] mVPMatrix = new float[16];

	public InterfaceSceneRenderer getScene() {
		return mScene;
	}

	public void setScene(InterfaceSceneRenderer mScene) {
		this.mScene = mScene;
	}

	private InterfaceSceneRenderer mScene = null;

//	// used by saveRenderState() / restoreRenderState()
//	private final float mSavedMatrix[] = new float[16];
//	private EGLDisplay mSavedEglDisplay;
//	private EGLSurface mSavedEglDrawSurface;
//	private EGLSurface mSavedEglReadSurface;
//	private EGLContext mSavedEglContext;
//	// Frame counter, used for reducing recorder frame rate.
//	private int mFrameCount;

    public GLES20Renderer() {
		super();
	}

	/**
	 * Performs one-time GL setup (creating programs, disabling optional features).
	 */
	private void glSetup() {
		// Set the clear color
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// Use culling to remove back faces.
		GLES20.glEnable(GLES20.GL_CULL_FACE);

		// Enable depth testing
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		glSetup();

//		// now repeat it for the game recorder
//		GameRecorder recorder = GameRecorder.getInstance();
//		if (recorder.isRecording()) {
//			Log.d(TAG, "configuring GL for recorder");
//			saveRenderState();
//			recorder.firstTimeSetup();
//			recorder.makeCurrent();
//			glSetup();
//			restoreRenderState();
//
//			mFrameCount = 0;
//		}

	}
//	/**
//	 * Saves the current projection matrix and EGL state.
//	 */
//	private void saveRenderState() {
//		System.arraycopy(mProjectionMatrix, 0, mSavedMatrix, 0, mProjectionMatrix.length);
//		mSavedEglDisplay = EGL14.eglGetCurrentDisplay();
//		mSavedEglDrawSurface = EGL14.eglGetCurrentSurface(EGL14.EGL_DRAW);
//		mSavedEglReadSurface = EGL14.eglGetCurrentSurface(EGL14.EGL_READ);
//		mSavedEglContext = EGL14.eglGetCurrentContext();
//	}
//
//	/**
//	 * Saves the current projection matrix and EGL state.
//	 */
//	private void restoreRenderState() {
//		// switch back to previous state
//		if (!EGL14.eglMakeCurrent(mSavedEglDisplay, mSavedEglDrawSurface, mSavedEglReadSurface,
//				mSavedEglContext)) {
//			throw new RuntimeException("eglMakeCurrent failed");
//		}
//		System.arraycopy(mSavedMatrix, 0, mProjectionMatrix, 0, mProjectionMatrix.length);
//	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);

		mWidth = width;
		mHeight = height;

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

		//gameState.calculateNextFrame();
		drawFrame();
//		GameRecorder recorder = GameRecorder.getInstance();
//		if (recorder.isRecording() && recordThisFrame()) {
//			saveRenderState();
//
//			// switch to recorder state
//			recorder.makeCurrent();
//			recorder.getProjectionMatrix(mProjectionMatrix);
//			recorder.setViewport();
//
//			// render everything again
//			drawFrame();
//			recorder.swapBuffers();
//
//			restoreRenderState();
//		}
//
		// Stop animating at 60fps (or whatever the refresh rate is) if the game is over.  Once
		// we do this, we won't get here again unless something explicitly asks the system to
		// render a new frame.  (As a handy side-effect, this prevents the paddle from actively
		// moving after the game is over.)
		//
		// It's a bit clunky to be polling for this, but it needs to be controlled by GameState,
		// and that class doesn't otherwise need to call back into us or have access to the
		// GLSurfaceView.
//		if (!gameState.isAnimating()) {
//			Log.d(TAG, "Game over, stopping animation");
//			// While not explicitly documented as such, it appears that setRenderMode() may be
//			// called from any thread.  The getRenderMode() function is documented as being
//			// available from any thread, and looking at the sources reveals setRenderMode()
//			// uses the same synchronization.  If it weren't allowed, we'd need to post an
//			// event to the UI thread to do this.
//			mSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
//		}
	}
    float [] Dae2GlCamMatrix(float[] daeMatrix) {
//        float [] glMatrix = new float []{
//                daeMatrix[0], daeMatrix[1], daeMatrix[2], daeMatrix[3]
//                , daeMatrix[8], daeMatrix[9], daeMatrix[10], daeMatrix[11]
//                , -daeMatrix[4], -daeMatrix[5], -daeMatrix[6], -daeMatrix[7]
//                ,daeMatrix[12], daeMatrix[13], daeMatrix[14], daeMatrix[15]
//        };
        float [] invGlMatrix = new float [16];
        android.opengl.Matrix.invertM(invGlMatrix, 0, daeMatrix, 0);
//        float [] invTransGlMatrix = new float [16];
//        android.opengl.Matrix.transposeM(invTransGlMatrix, 0, invGlMatrix, 0);
        return invGlMatrix;

    }

	private void drawFrame() {

        // clear Screen and Depth Buffer, we have set the clear color as black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        //GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        
        // Set the view matrix. This matrix can be said to represent the camera position.
		// NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
		// view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
		if (mScene != null ) {
//			float [] mPosCam = mScene.getCamPos();
//			float [] mRotCam = mScene.getCamRot();
			float mAngleCam = 0.0f ;// mRotCam [1];
			mViewMatrix = Dae2GlCamMatrix(mScene.getCamMatrix());
//			Matrix.setLookAtM(mViewMatrix, 0,
//					// Position the eye in front of the origin.
//					7.48113f, 5.34367f, 6.50767f,//mPosCam [0], mPosCam [1], mPosCam [2],// eyeX, eyeY, eyeZ,
//					// We are looking toward the distance
//					0.0f, 0.0f, 0.0f,//(float)(mPosCam [0] + (Math.cos(Math.toRadians(mAngleCam)))), 1.0f, (float)(mPosCam [2] + (Math.sin(Math.toRadians(mAngleCam)))), // lookX, lookY, lookZ,
//					// Set our up vector. This is where our head would be pointing were we holding the camera.
//					0.0f, 1.0f, 0.0f  //upX, upY, upZ
//					);
			//Matrix.multiplyMM(mVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

			List<glRenderableShape> ShapesToRender = mScene.getRenderableShapes();
			for (glRenderableShape shap : ShapesToRender) {
				//
				// shap.render(mVPMatrix, mShaderHelper, mScene);
				shap.render(mViewMatrix, mProjectionMatrix, mScene);
				checkGLError();
			}
		}
	}
//	/**
//	 * Decides whether we want to record the current frame, based on the target frame rate
//	 * and an assumed 60fps refresh rate.
//	 * <p>
//	 * We could be smarter here, and not drop a frame if the system dropped one inadvertently
//	 * (i.e. we missed a vsync by being too slow).
//	 */
//	private boolean recordThisFrame() {
//		final int TARGET_FPS = 30;
//
//		mFrameCount++;
//		switch (TARGET_FPS) {
//			case 60:
//				return true;
//			case 30:
//				return (mFrameCount & 0x01) == 0;
//			case 24:
//				// want 2 out of every 5 frames
//				int mod = mFrameCount % 5;
//				return mod == 0 || mod == 2;
//			default:
//				return true;
//		}
//	}
//

	void checkGLError()
    {
        for (int error = GLES20.glGetError(); error != 0; error = GLES20.glGetError())
            ;
    }

	public glRenderableShape getPointedShape (float mouseX, float mouseY) {
    	glRenderableShape result = null;
		float [] Hit = null;
		float [] closestHit = null;
		float bestDistance = 0.0f;

		float[] direction = getWorldSpaceFromMouseCoordinates(mouseX, mouseY);
		float [] posCam = mScene.getCamPos();

		List<glRenderableShape> ShapesToRender = mScene.getRenderableShapes();
        Log.d(TAG, "direction = (" + direction [0] +", " + direction [1] +", " +direction [2] +") ");
        Log.d(TAG, "posCam = (" + posCam [0] +", " + posCam [1] +", " +posCam [2] +") ");

		for (glRenderableShape shap : ShapesToRender) {
			//
			// shap.render(mVPMatrix, mShaderHelper, mScene);
			//Log.d(TAG, "Scanning : " + shap.getName());
			Hit = shap.intersectRay(posCam, direction);
			if (Hit != null) {
				// compute distance to intersection
				float distance  = ( new Vector( new float [] {
						Hit [0] - posCam[0]
						,Hit [1] - posCam[1]
						,Hit [2] - posCam[2] })).magnitude();

				if (closestHit == null) {
					closestHit = Hit;
					bestDistance = distance;
					result = shap;
				} else {
					if(distance < bestDistance) {
						closestHit = Hit;
						bestDistance = distance;
						result = shap;
					}
				}

			}
		}

    	return result;
	}
//    /**
//     * Handles pausing of the game Activity.  This is called by the View (via queueEvent) at
//     * pause time.  It tells GameState to save its state.
//     *
//     * @param syncObj Object to notify when we have finished saving state.
//     */
//    public void onViewPause(ConditionVariable syncObj) {
//        /*
//         * We don't explicitly pause the game action, because the main game loop is being driven
//         * by the framework's calls to our onDrawFrame() callback.  If we were driving the updates
//         * ourselves we'd need to do something more.
//         */
//
//         GameRecorder.getInstance().gamePaused();
//
//        syncObj.open();
//    }
	public float[] getWorldSpaceFromMouseCoordinates(float mouseX, float mouseY)
	{
		float[] farCoord = { 0.0f, 0.0f, 0.0f, 0.0f };
		float[] nearCoord = { 0.0f, 0.0f, 0.0f, 0.0f };

		int [] _viewport = new int[] { 0, 0, mWidth, mHeight };
		// mouse Y needs to be inverted
		//mouseY = (float) _viewport[3] - mouseY;
		Log.d(TAG, "Try to unproject :" + mouseX + "," + mouseY );
		// calling glReadPixels() with GL_DEPTH_COMPONENT is not supported in
		// GLES so now i will try to implement ray picking

		int result = GLU.gluUnProject(mouseX, mouseY, 1.0f, mViewMatrix, 0, mProjectionMatrix, 0, _viewport, 0,
				farCoord, 0);

		if (result == GL10.GL_TRUE)
		{
			farCoord[0] = farCoord[0] / farCoord[3];
			farCoord[1] = farCoord[1] / farCoord[3];
			farCoord[2] = farCoord[2] / farCoord[3];
		}

		result = GLU.gluUnProject(mouseX, mouseY, 0.0f, mViewMatrix, 0, mProjectionMatrix, 0, _viewport, 0, nearCoord,
				0);

		if (result == GL10.GL_TRUE)
		{
			nearCoord[0] = nearCoord[0] / nearCoord[3];
			nearCoord[1] = nearCoord[1] / nearCoord[3];
			nearCoord[2] = nearCoord[2] / nearCoord[3];
		}

		float [] direction = {
			farCoord[0] -  nearCoord[0]
			,farCoord[1] -  nearCoord[1]
			,farCoord[2] -  nearCoord[2]
		};

		Vector dirVector = new Vector(direction);
		direction [0] = direction[0]/dirVector.magnitude();
		direction [1] = - direction[1]/dirVector.magnitude(); // TODO
		direction [2] = direction[2]/dirVector.magnitude();

		float [] camPos =  mScene.getCamPos();

		//float [] dirVector = Vector.normalize(Vector.minus(farCoord, nearCoord));
//		float [] rayOrigin =  {0.0f, 0.0f, 3.0f};
//
//		Log.d(TAG, "Far Coordinate:" + farCoord[0] + "," + farCoord[1] + "," + farCoord[2]);
//		Log.d(TAG, "Near Coordinate:" + nearCoord[0] + "," + nearCoord[1] + "," + nearCoord[2]);
//		Log.d(TAG, "width :" + mWidth + ", height :" + mHeight);
//		Log.d(TAG, "Direction:" + direction[0] + "," + direction[1] + "," + direction[2]);

//		float [] vertices = { -0.5f, 0.5f, 0.0f, // top left
//				-0.5f, -0.5f, 0.0f, // bottom left
//				0.5f, -0.5f, 0.0f, // bottom right
//				0.5f, 0.5f, 0.0f }; // top right
//
//		// calculate normal for square
//		float[] v1 = { vertices[3] - vertices[0], vertices[4] - vertices[1], vertices[5] - vertices[2]};
//		float[] v2 = { vertices[9] - vertices[0], vertices[10] - vertices[1], vertices[11] - vertices[2]};
//
//		float[] n = Vector.normalize(Vector.crossProduct(v1, v2));
//
//		// now calculate intersection point as per following link
//		// http://antongerdelan.net/opengl/raycasting.html
//
//		// our plane passes through origin so findint 't' ll be
//
//		float t = -(Vector.dot(rayOrigin, n) / Vector.dot(dirVector, n));
//
//		// now substitute above t in ray equation gives us intersection point
//		float [] intersectionPoint = Vector.addition(rayOrigin, Vector.scalarProduct(t, dirVector));
//		Log.d("Ipoint:", "" + intersectionPoint[0] + "," + intersectionPoint[1] + "," + intersectionPoint[2]);
		return direction;
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



