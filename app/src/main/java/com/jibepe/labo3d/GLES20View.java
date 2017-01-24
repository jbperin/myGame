package com.jibepe.labo3d;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import com.jibepe.model.GrosBordel;

public class GLES20View extends GLSurfaceView  {
	Context mContext;
	GLES20Renderer mRenderer;
	SceneGraph mScene;
	GrosBordel grBordel;

	private float mPreviousX;
	private float mPreviousY;

	public GLES20View(Context context, SceneGraph theSceneGraph) {
		super(context);

		mContext = context;

		getHolder().setFormat(PixelFormat.TRANSLUCENT);
		//setEGLConfigChooser(8, 8, 8, 8, 8, 8);
		mScene = theSceneGraph;
		//grBordel = new GrosBordel(mContext);

		mRenderer = new GLES20Renderer (context, new SceneContentProvider(mScene));

		// Request an OpenGL ES 2.0 compatible context.
		setEGLContextClientVersion(2);

		// Set the Renderer for drawing on the GLSurfaceView
		setRenderer(mRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}
	public GLES20Renderer getRenderer (){
		return mRenderer;
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
				//mRenderer.rotateCam(dx / 2.0f );
				mScene.rotateCam(dx / 2.0f );
			}
			else 
			{
				//Log.e("onTouchEvent", "Translate: " + dy);
				//mRenderer.moveCam(- dy / 6.0f );
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
