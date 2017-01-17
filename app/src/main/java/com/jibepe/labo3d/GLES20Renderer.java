package com.jibepe.labo3d;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.jibepe.objparser.MaterialShape;
import com.jibepe.objparser.ObjLoader;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;

public class GLES20Renderer implements Renderer {
	private Context mContext;
	private ObjLoader mSceneLoader;
	private ObjLoader mObjLoader;
	private Dictionary<String, Integer> dTextureHandlers;

	public GLES20Renderer(Context context) {
		super();
		mContext = context;
		mSceneLoader = new ObjLoader(mContext);
		mObjLoader = new ObjLoader(mContext);
	}

	private String getShader(int ressourceID)
	{
		return RawResourceReader.readTextFileFromRawResource(mContext, ressourceID);
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the clear color
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		// Use culling to remove back faces.
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		
		// Enable depth testing
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);

		final int sclShader_V = myShaderTB.compileShader(GLES20.GL_VERTEX_SHADER, getShader(R.raw.solid_color_light_v));		
		final int sclShader_F = myShaderTB.compileShader(GLES20.GL_FRAGMENT_SHADER, getShader(R.raw.solid_color_light_f));		
		mSolidColorLightProgram = myShaderTB.createAndLinkProgram(sclShader_V, sclShader_F, 
							new String[] {"a_Position",  "a_Color", "a_Normal"});	

		final int suclShader_V = myShaderTB.compileShader(GLES20.GL_VERTEX_SHADER, getShader(R.raw.solid_ucolor_light_v));		
		final int suclShader_F = myShaderTB.compileShader(GLES20.GL_FRAGMENT_SHADER, getShader(R.raw.solid_ucolor_light_f));		
		mSolidUColorLightProgram = myShaderTB.createAndLinkProgram(suclShader_V, suclShader_F, 
							new String[] {"a_Position", "a_Normal"});	

		final int pointShader_V = myShaderTB.compileShader(GLES20.GL_VERTEX_SHADER, getShader(R.raw.solid_color_point_v));		
		final int pointShader_F = myShaderTB.compileShader(GLES20.GL_FRAGMENT_SHADER, getShader(R.raw.solid_color_point_f));		
		mSolidPointProgram = myShaderTB.createAndLinkProgram(pointShader_V, pointShader_F, 
							new String[] {"a_Position"});	

		final int LineShader_V = myShaderTB.compileShader(GLES20.GL_VERTEX_SHADER, getShader(R.raw.solid_ucolor_nolight_v));		
		final int LineShader_F = myShaderTB.compileShader(GLES20.GL_FRAGMENT_SHADER, getShader(R.raw.solid_ucolor_nolight_f));		
		mSolidLineProgram = myShaderTB.createAndLinkProgram(LineShader_V, LineShader_F, 
							new String[] {"a_Position"});	

		final int stclShader_V = myShaderTB.compileShader(GLES20.GL_VERTEX_SHADER, getShader(R.raw.solid_tex_ucolor_light_v));		
		final int stclShader_F = myShaderTB.compileShader(GLES20.GL_FRAGMENT_SHADER, getShader(R.raw.solid_tex_ucolor_light_f));		
		mSolidTexColorLightProgram = myShaderTB.createAndLinkProgram(stclShader_V, stclShader_F, 
				new String[] {"a_Position", "a_Normal", "a_TexCoordinate"});

		final int stcShader_V = myShaderTB.compileShader(GLES20.GL_VERTEX_SHADER, getShader(R.raw.solid_tex_ucolor_nolight_v));		
		final int stcShader_F = myShaderTB.compileShader(GLES20.GL_FRAGMENT_SHADER, getShader(R.raw.solid_tex_ucolor_nolight_f));		
		mSolidTexColorNoLightProgram = myShaderTB.createAndLinkProgram(stcShader_V, stcShader_F, 
				new String[] {"a_Position", "a_TexCoordinate"});

		final int stlShader_V = myShaderTB.compileShader(GLES20.GL_VERTEX_SHADER, getShader(R.raw.solid_tex_light_v));		
		final int stlShader_F = myShaderTB.compileShader(GLES20.GL_FRAGMENT_SHADER, getShader(R.raw.solid_tex_light_f));		
		mSolidTexLightProgram = myShaderTB.createAndLinkProgram(stlShader_V, stlShader_F, 
				new String[] {"a_Position", "a_Normal", "a_TexCoordinate"});

		// Load models
		mSceneLoader.loadModel("scene");
		mObjLoader.loadModel("plantexture");
		
		// Identify the texture to load
		List<String> lTexture = new ArrayList<String>() ;
		mSceneLoader.listTexture(lTexture);
		mObjLoader.listTexture(lTexture);
		
		
		dTextureHandlers = new Hashtable<String, Integer>();
		
        // Load the texture
		AssetManager assetManager = mContext.getAssets();
		ListIterator<String> it = lTexture.listIterator() ;
		while(it.hasNext()) {
			String texture_filename = it.next();
			InputStream inObj;
			try {
				inObj = assetManager.open(texture_filename);
				int textureId = TextureHelper.loadPNGTexture(mContext, inObj);
				dTextureHandlers.put(texture_filename, textureId);
				GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
//        mAlphaTextureDataHandle = TextureHelper.loadAlphaTexture(mContext, R.drawable.ic_launcher);
//        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
		
//        mTextureDataHandle = TextureHelper.loadTexture(mContext, R.drawable.stone_wall_public_domain);
//        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
		
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

        // Do a complete rotation every 10 seconds.
        long time = SystemClock.uptimeMillis() % 10000L;        
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);                

        // clear Screen and Depth Buffer, we have set the clear color as black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        //GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        
        // Set the view matrix. This matrix can be said to represent the camera position.
		// NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
		// view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
		Matrix.setLookAtM(mViewMatrix, 0, 
				// Position the eye in front of the origin.
				mPosCamX, 1.0f, mPosCamY,// eyeX, eyeY, eyeZ, 
				// We are looking toward the distance
				(float)(mPosCamX + (Math.cos(Math.toRadians(mAngleCam)))), 1.0f, (float)(mPosCamY + (Math.sin(Math.toRadians(mAngleCam)))), // lookX, lookY, lookZ, 
				// Set our up vector. This is where our head would be pointing were we holding the camera.
				0.0f, 1.0f, 0.0f  //upX, upY, upZ
				);		
		Matrix.multiplyMM(mVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
		
        // Calculate position of the light. Rotate and then push into the distance.
        Matrix.setIdentityM(mLightModelMatrix, 0);
        //Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, -2.0f);      
        Matrix.rotateM(mLightModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);
        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 1.0f, 2.0f);
               
        Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0, mLightPosInWorldSpace, 0);                        

        drawGrid(5);

        drawLine (0.0f, 0.0f, 0.0f, 
    			  1.0f, 0.0f, 0.0f, 
    			  1.0f, 0.0f, 0.0f, 1.0f); //red
        
        drawLine (0.0f, 0.0f, 0.0f, 
  			  0.0f, 0.0f, -1.0f, 
  			  0.0f, 1.0f, 0.0f, 1.0f);  // green 

        drawLine (0.0f, 0.0f, 0.0f, 
  			  0.0f, 1.0f, 0.0f, 
  			  0.0f, 0.0f, 1.0f, 1.0f);  // blue


        mModelMatrix = mLightModelMatrix;

        drawPoint(mLightPosInModelSpace[0], mLightPosInModelSpace[1], mLightPosInModelSpace[2], 
        		5.0f, 
        		1.0f, 1.0f, 1.0f, 1.0f);

        final float scnPosition [] = {0.0f, 0.0f, 0.0f};
        final float scnOrientation [] = {0.0f, 0.0f, 0.0f};

        drawDroid (scnPosition, scnOrientation, mSceneLoader);
        

        final float position [] = {objPos[0], objPos[1], objPos[2]};
        final float orientation [] = {objRot[0], objRot[1], objRot[2]};
        
        drawDroid (position, orientation, mObjLoader);
        
	}
	private float objPos[] = {1.0f, 0.0f, -1.0f};
	private float objRot[] = {0.0f, 0.0f, 0.0f};
	
	private void drawDroid (float[] position, float[] orientation, ObjLoader Shape){
		
		
		
		Matrix.setIdentityM(mModelMatrix, 0);

		Matrix.rotateM(mModelMatrix, 0, orientation[0], 1.0f, 0.0f, 0.0f);
		Matrix.rotateM(mModelMatrix, 0, orientation[1], 0.0f, 1.0f, 0.0f);
		Matrix.rotateM(mModelMatrix, 0, orientation[2], 0.0f, 0.0f, 1.0f);

		mModelMatrix[12] = position[0];
		mModelMatrix[13] = position[1];
		mModelMatrix[14] = position[2];

		
        Enumeration<String> key = Shape.dictionary.keys();
        while(key.hasMoreElements()){
        	String matName = key.nextElement();
        	MaterialShape matShape = (MaterialShape)Shape.dictionary.get(matName);
        	if (matShape.texturename != null) {
        		final float[] uvplan_color = {0.5f, 0.5f, 0.5f, 1.0f};
        		final int textId = dTextureHandlers.get(matShape.texturename);
        		drawAlphaTexturedBuffer(matShape.buffer, uvplan_color, textId);
        	}
        	else
        	{
            	final float [] matShapeColor= {matShape.r, matShape.g, matShape.b, 1.0f};
                drawBuffer (matShape.buffer, matShapeColor);
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

		//drawAlphaTexturedBuffer(uvplan_buffer, uvplan_color);
	}
	
	private void drawPoint(float X, float Y, float Z, float size, 
			float r, float g, float b, float a)
	{
		int program = mSolidPointProgram;
		
        final int mMVPMatrixHandle = GLES20.glGetUniformLocation(program, "u_MVPMatrix");
        final int mColorHandle = GLES20.glGetUniformLocation(program, "u_Color");
        final int mSizeHandle = GLES20.glGetUniformLocation(program, "u_Size");

        final int mPositionHandle = GLES20.glGetAttribLocation(program, "a_Position");
        
    	/** Allocate storage for the final combined matrix. This will be passed into the shader program. */
    	final float[] mMVPMatrix = new float[16];

		GLES20.glUseProgram(program);	

        // Pass in the position.
		GLES20.glVertexAttrib3f(mPositionHandle, X, Y, Z);
		
        // Color
        GLES20.glUniform4f(mColorHandle, r, g, b, a);
        
        // Size 
        GLES20.glUniform1f(mSizeHandle, size);
        
		// Since we are not using a buffer object, disable vertex arrays for this attribute.
        GLES20.glDisableVertexAttribArray(mPositionHandle);  
		
		// Pass in the transformation matrix.
        Matrix.multiplyMM(mMVPMatrix, 0, mVPMatrix, 0, mModelMatrix, 0);   
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
		
		// Draw the point.
		GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
	
	}
	
	private void drawLine (float p1x, float p1y, float p1z, 
			float p2x, float p2y, float p2z, 
			float r, float g, float b, float a) {

		final float VertexData[] = {p1x, p1y, p1z, p2x, p2y, p2z};
    	//final float[] Color = {r, g, b, a};
    	final float[] mMVPMatrix = new float[16];
    	final float[] ModelMatrix = new float[16];

        GLES20.glUseProgram(mSolidLineProgram);

        final int mPositionHandle = GLES20.glGetAttribLocation(mSolidLineProgram, "a_Position");
		
        /** This will be used to pass in the transformation matrix. */
        final int mMVPMatrixHandle = GLES20.glGetUniformLocation(mSolidLineProgram, "u_MVPMatrix");

        final int mColorHandle = GLES20.glGetUniformLocation(mSolidLineProgram, "u_Color");

		FloatBuffer mLinePositions = ByteBuffer.allocateDirect(VertexData.length * FLOAT_FIELD_SIZE)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();							
		mLinePositions.put(VertexData).position(0);	

		// Positions
        GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false,
        		0, mLinePositions);        
        GLES20.glEnableVertexAttribArray(mPositionHandle);        
  
        Matrix.setIdentityM(ModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mVPMatrix, 0, ModelMatrix, 0);   
        
        // Combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        // Color.
        GLES20.glUniform4f(mColorHandle, r, g, b, a);

        GLES20.glDrawArrays(GLES20.GL_LINES, 0, 2);                               
	}
    
	private void drawGrid (int number) {
		for (int ii = -number; ii <= number; ii ++) {
	        drawLine ((float)ii, 0.0f, (float)-number, 
	        		  (float)ii, 0.0f, (float) number, 
	        		  0.5f, 0.5f , 0.5f ,1.0f);
	        drawLine ((float)-number, 0.0f, (float)ii, 
	        		  (float) number, 0.0f, (float)ii, 
	        		  0.5f, 0.5f , 0.5f ,1.0f);

		}		
	}

	private void drawBuffer(float[] buffer, float[] color) {
		
		int program = mSolidUColorLightProgram;
        GLES20.glUseProgram(program);
		
		final int mPositionHandle = GLES20.glGetAttribLocation(program, "a_Position");
		final int mNormalHandle = GLES20.glGetAttribLocation(program, "a_Normal");
        
        /** This will be used to pass in the transformation matrix. */
        final int mMVPMatrixHandle = GLES20.glGetUniformLocation(program, "u_MVPMatrix");
    	/** This will be used to pass in the modelview matrix. */
        final int mMVMatrixHandle = GLES20.glGetUniformLocation(program, "u_MVMatrix");
    	/** This will be used to pass in the light position. */
        final int mLightPosHandle = GLES20.glGetUniformLocation(program, "u_LightPos");

        final int mColorHandle = GLES20.glGetUniformLocation(program, "u_Color");
        
        final float[] mMVPMatrix = new float[16];
        
        final int stride = (POSITION_DATA_SIZE + NORMAL_DATA_SIZE) * FLOAT_FIELD_SIZE;
        

        FloatBuffer FaceData = ByteBuffer.allocateDirect(buffer.length * FLOAT_FIELD_SIZE)
		.order(ByteOrder.nativeOrder()).asFloatBuffer();
        FaceData.put(buffer).position(0);

        FaceData.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle,POSITION_DATA_SIZE, GLES20.GL_FLOAT, false,
        		stride, FaceData);        
        GLES20.glEnableVertexAttribArray(mPositionHandle);        

        FaceData.position(POSITION_DATA_SIZE);		
        GLES20.glVertexAttribPointer(mNormalHandle,NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false,
        		stride, FaceData);
        GLES20.glEnableVertexAttribArray(mNormalHandle);        

        
		// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
        
        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                
        
        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        
        // Pass in the light position in eye space.        
        GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);
        
        // Color.
        GLES20.glUniform4f(mColorHandle, color[0], color[1], color[2], color[3]);
                
		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, buffer.length/(POSITION_DATA_SIZE + NORMAL_DATA_SIZE));
        
	}

	
	private void drawAlphaTexturedBuffer(float[] buffer, float[] color, int textureHandler) {
		int program;
		
		
		program = mSolidTexColorNoLightProgram;
		GLES20.glUseProgram(program);
		
		// "a_Position", "a_Color", "a_Normal", "a_TexCoordinate"
		final int mPositionHandle = GLES20.glGetAttribLocation(program, "a_Position");
        final int mNormalHandle = GLES20.glGetAttribLocation(program, "a_Normal"); 
        final int mTextureCoordinateHandle = GLES20.glGetAttribLocation(program, "a_TexCoordinate");
        
        final int mTextureUniformHandle = GLES20.glGetUniformLocation(program, "u_Texture");
        /** This will be used to pass in the transformation matrix. */
        final int mMVPMatrixHandle = GLES20.glGetUniformLocation(program, "u_MVPMatrix");
    	/** This will be used to pass in the modelview matrix. */
        final int mMVMatrixHandle = GLES20.glGetUniformLocation(program, "u_MVMatrix");
    	/** This will be used to pass in the light position. */
        final int mLightPosHandle = GLES20.glGetUniformLocation(program, "u_LightPos");
        final int mColorHandle = GLES20.glGetUniformLocation(program, "u_Color");
        
    	/** Allocate storage for the final combined matrix. This will be passed into the shader program. */
    	final float[] mMVPMatrix = new float[16];
    	
        FloatBuffer FaceData = ByteBuffer.allocateDirect(buffer.length * FLOAT_FIELD_SIZE)
		.order(ByteOrder.nativeOrder()).asFloatBuffer();
        FaceData.put(buffer).position(0);

        final int stride = (POSITION_DATA_SIZE + NORMAL_DATA_SIZE + UVCOORD_DATA_SIZE) * FLOAT_FIELD_SIZE;
        
        FaceData.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle,POSITION_DATA_SIZE, GLES20.GL_FLOAT, false,
        		stride, FaceData);        
        GLES20.glEnableVertexAttribArray(mPositionHandle);        

        FaceData.position(POSITION_DATA_SIZE);		
        GLES20.glVertexAttribPointer(mNormalHandle,NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false,
        		stride, FaceData);
        GLES20.glEnableVertexAttribArray(mNormalHandle);        

        
        
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandler);
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);        

        FaceData.position(POSITION_DATA_SIZE + NORMAL_DATA_SIZE);		
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, UVCOORD_DATA_SIZE, GLES20.GL_FLOAT, false,
        		stride, FaceData);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);        
        
        // Color.
        GLES20.glUniform4f(mColorHandle, color[0], color[1], color[2], color[3]);
        

        // DEB Alpha Blending
//        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
//        GLES20.glEnable(GLES20.GL_BLEND); 
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);	        
//		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glDisable(GLES20.GL_CULL_FACE);
        
		// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);   
        
        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                
        
        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        
        // Pass in the light position in eye space.        
        GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);
        
        // Draw the cube.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, buffer.length/(POSITION_DATA_SIZE + NORMAL_DATA_SIZE + UVCOORD_DATA_SIZE));                               
        
        // DEB Alpha Blending
        GLES20.glDisable(GLES20.GL_BLEND);
        //GLES20.glDisableVertexAttribArray(mPositionHandle);
//		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);

	}
	
	public void rotateCam(float f) {
		mAngleCam += f;
	}

	public void moveCam(float f) {
		mPosCamX += f*Math.cos(Math.toRadians(mAngleCam));
		mPosCamY += f*Math.sin(Math.toRadians(mAngleCam));
	}	
	
	public void setObjPos(String objName, float [] pos){
		// TODO: Gerer le dictionnaire d'objet
		objPos = pos;
	}
	public void setObjRot(String objName, float [] rot){
		// TODO: Gerer le dictionnaire d'objet
		objRot = rot;
	}
	
	private float mAngleCam = -90.0f;
	private float mPosCamX = 0.0f;
	private float mPosCamY = 0.0f;

	

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
	private float[] mModelMatrix = new float[16];

	/**
	 * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
	 * of being located at the center of the universe) to world space.
	 */
	private float[] mVPMatrix = new float[16];
	
	/** 
	 * Stores a copy of the model matrix specifically for the light position.
	 */
	private float[] mLightModelMatrix = new float[16];	

	/** Used to hold a light centered on the origin in model space. We need a 4th coordinate so we can get translations to work when
	 *  we multiply this by our transformation matrices. */
	final float[] mLightPosInModelSpace = new float[] {0.0f, 0.0f, 0.0f, 1.0f};

	/** Used to hold the current position of the light in world space (after transformation via model matrix). */
	final float[] mLightPosInWorldSpace = new float[4];

	/** Used to hold the transformed position of the light in eye space (after transformation via modelview matrix) */
	final float[] mLightPosInEyeSpace = new float[4];
	
	private ShaderUtil myShaderTB = new ShaderUtil();

	/** This is a handle to our per-vertex cube shading program. */
	private int mSolidPointProgram;
	private int mSolidLineProgram;
	private int mSolidColorLightProgram;
	private int mSolidUColorLightProgram;
	private int mSolidTexColorLightProgram;
	private int mSolidTexColorNoLightProgram;
	private int mSolidTexLightProgram;

	/** This is a handle to our texture data. */
	private int mAlphaTextureDataHandle, mTextureDataHandle;
	
	private final int FLOAT_FIELD_SIZE = 4; 
	private final int POSITION_DATA_SIZE = 3;
	private final int NORMAL_DATA_SIZE = 3;
	private final int UVCOORD_DATA_SIZE = 2;
	private final int COLOR_DATA_SIZE = 4;
}
