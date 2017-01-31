package com.jibepe.labo3d;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import com.jibepe.model.SceneGraph;
import com.jibepe.render3d.GLES20Renderer;

public class GLES20View extends GLSurfaceView  {

	GLES20Renderer mRenderer = null;
	SceneGraph mScene = null;
	SceneContentProvider mSceneView = null;

	private float mPreviousX;
	private float mPreviousY;

	public GLES20View(Context context, GLES20Renderer theRenderer, SceneGraph theSceneGraph) {
		super(context);


		getHolder().setFormat(PixelFormat.TRANSLUCENT);
		//setEGLConfigChooser(8, 8, 8, 8, 8, 8);
		mScene = theSceneGraph;
		mSceneView = new SceneContentProvider(mScene);

		mRenderer = theRenderer;
		mRenderer.setScene(mSceneView);


	}

	@Override public boolean onTrackballEvent(MotionEvent e) {
		//        mAngleX += e.getX();
		//        mAngleY += e.getY();
		requestRender();
		return true;
	}
	@Override public boolean  performClick(){
		return super.performClick();
	}
	@Override public boolean onTouchEvent(MotionEvent e) {
		super.onTouchEvent(e);
		float x = e.getX();
		float y = e.getY();
		mRenderer.getWorldSpaceFromMouseCoordinates(0, 0);
		mRenderer.getWorldSpaceFromMouseCoordinates(mRenderer.mWidth,0);
		mRenderer.getWorldSpaceFromMouseCoordinates(mRenderer.mWidth, mRenderer.mHeight);
		mRenderer.getWorldSpaceFromMouseCoordinates(0, mRenderer.mHeight);

		mRenderer.getWorldSpaceFromMouseCoordinates(mRenderer.mWidth/2, mRenderer.mHeight/2);

		mRenderer.getWorldSpaceFromMouseCoordinates(x, y);

		switch (e.getAction()) {
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			break;
		case MotionEvent.ACTION_DOWN:
			performClick();
			break;
		case MotionEvent.ACTION_MOVE:
			float dx = x - mPreviousX;
			float dy = y - mPreviousY;
			if (Math.abs(dx) > Math.abs(dy)) {
				//Log.e("onTouchEvent", "Rotate: " + dx);
				mScene.rotateCam(dx / 2.0f );
			}
			else 
			{
				//Log.e("onTouchEvent", "Translate: " + dy);
				mScene.moveCam(- dy / 6.0f );
			}
			requestRender();
			break;
		}
		mPreviousX = x;
		mPreviousY = y;
		return true;
	}


}
