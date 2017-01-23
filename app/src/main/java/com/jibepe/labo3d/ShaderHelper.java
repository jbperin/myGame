package com.jibepe.labo3d;


import android.content.Context;
import android.opengl.GLES20;

public class ShaderHelper {

	private Context context;

	private int mSolidPointProgram;
	private int mSolidLineProgram;
	private int mSolidColorLightProgram;
	private int mSolidUColorLightProgram;
	private int mSolidTexColorLightProgram;
	private int mSolidTexColorNoLightProgram;
	private int mSolidTexLightProgram;

	public final static String sSolidPointProgram = "SolidPointProgram";
	public final static String sSolidLineProgram = "SolidPointProgram";
	public final static String sSolidColorLightProgram = "SolidColorLightProgram";
	public final static String sSolidUColorLightProgram = "SolidUColorLightProgram";
	public final static String sSolidTexColorLightProgram = "SolidTexColorLightProgram";
	public final static String sSolidTexColorNoLightProgram = "SolidTexColorNoLightProgram";
	public final static String sSolidTexLightProgram = "SolidTexLightProgram";

	public ShaderHelper(Context mContext) {
		context = mContext;

	}
	/** This is a handle to our per-vertex cube shading program. */
	private String getShader(int ressourceID)
	{
		return RawResourceReader.readTextFileFromRawResource(context, ressourceID);
	}
	public int getShaderProgram(String shaderName){
		if (shaderName.equals(sSolidPointProgram)){
			return (mSolidPointProgram);
		}else if (shaderName.equals(sSolidLineProgram)){
			return (mSolidLineProgram);
		}else if (shaderName.equals(sSolidColorLightProgram)){
			return (mSolidColorLightProgram);
		}else if (shaderName.equals(sSolidUColorLightProgram)){
			return (mSolidUColorLightProgram);
		}else if (shaderName.equals(sSolidTexColorLightProgram)){
			return (mSolidTexColorLightProgram);
		}else if (shaderName.equals(sSolidTexColorNoLightProgram)){
			return (mSolidTexColorNoLightProgram);
		}else if (shaderName.equals(sSolidTexLightProgram)){
			return (mSolidTexLightProgram);
		} else {
			return (mSolidPointProgram);
		}
	}
	public void loadShaders () {

		final int sclShader_V = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, getShader(R.raw.solid_color_light_v));
		final int sclShader_F = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, getShader(R.raw.solid_color_light_f));
		mSolidColorLightProgram = ShaderHelper.createAndLinkProgram(sclShader_V, sclShader_F,
				new String[]{"a_Position", "a_Color", "a_Normal"});

		final int suclShader_V = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, getShader(R.raw.solid_ucolor_light_v));
		final int suclShader_F = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, getShader(R.raw.solid_ucolor_light_f));
		mSolidUColorLightProgram = ShaderHelper.createAndLinkProgram(suclShader_V, suclShader_F,
				new String[]{"a_Position", "a_Normal"});

		final int pointShader_V = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, getShader(R.raw.solid_color_point_v));
		final int pointShader_F = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, getShader(R.raw.solid_color_point_f));
		mSolidPointProgram = ShaderHelper.createAndLinkProgram(pointShader_V, pointShader_F,
				new String[]{"a_Position"});

		final int LineShader_V = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, getShader(R.raw.solid_ucolor_nolight_v));
		final int LineShader_F = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, getShader(R.raw.solid_ucolor_nolight_f));
		mSolidLineProgram = ShaderHelper.createAndLinkProgram(LineShader_V, LineShader_F,
				new String[]{"a_Position"});

		final int stclShader_V = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, getShader(R.raw.solid_tex_ucolor_light_v));
		final int stclShader_F = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, getShader(R.raw.solid_tex_ucolor_light_f));
		mSolidTexColorLightProgram = ShaderHelper.createAndLinkProgram(stclShader_V, stclShader_F,
				new String[]{"a_Position", "a_Normal", "a_TexCoordinate"});

		final int stcShader_V = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, getShader(R.raw.solid_tex_ucolor_nolight_v));
		final int stcShader_F = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, getShader(R.raw.solid_tex_ucolor_nolight_f));
		mSolidTexColorNoLightProgram = ShaderHelper.createAndLinkProgram(stcShader_V, stcShader_F,
				new String[]{"a_Position", "a_TexCoordinate"});

		final int stlShader_V = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, getShader(R.raw.solid_tex_light_v));
		final int stlShader_F = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, getShader(R.raw.solid_tex_light_f));
		mSolidTexLightProgram = ShaderHelper.createAndLinkProgram(stlShader_V, stlShader_F,
				new String[]{"a_Position", "a_Normal", "a_TexCoordinate"});
	}
	/** 
	 * Helper function to compile a shader.
	 * 
	 * @param shaderType The shader type.
	 * @param shaderSource The shader source code.
	 * @return An OpenGL handle to the shader.
	 */
	public static int compileShader(final int shaderType, final String shaderSource)
	{
		int shaderHandle = GLES20.glCreateShader(shaderType);

		if (shaderHandle != 0) 
		{
			// Pass in the shader source.
			GLES20.glShaderSource(shaderHandle, shaderSource);

			// Compile the shader.
			GLES20.glCompileShader(shaderHandle);

			// Get the compilation status.
			final int[] compileStatus = new int[1];
			GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

			// If the compilation failed, delete the shader.
			if (compileStatus[0] == 0) 
			{
				//Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle));
				GLES20.glDeleteShader(shaderHandle);
				shaderHandle = 0;
			}
		}

		if (shaderHandle == 0)
		{			
			throw new RuntimeException("Error creating shader.");
		}
		
		return shaderHandle;
	}	
	
	/**
	 * Helper function to compile and link a program.
	 * 
	 * @param vertexShaderHandle An OpenGL handle to an already-compiled vertex shader.
	 * @param fragmentShaderHandle An OpenGL handle to an already-compiled fragment shader.
	 * @param attributes Attributes that need to be bound to the program.
	 * @return An OpenGL handle to the program.
	 */
	public static int createAndLinkProgram(final int vertexShaderHandle, final int fragmentShaderHandle, final String[] attributes)
	{
		int programHandle = GLES20.glCreateProgram();
		
		if (programHandle != 0) 
		{
			// Bind the vertex shader to the program.
			GLES20.glAttachShader(programHandle, vertexShaderHandle);			

			// Bind the fragment shader to the program.
			GLES20.glAttachShader(programHandle, fragmentShaderHandle);
			
			// Bind attributes
			if (attributes != null)
			{
				final int size = attributes.length;
				for (int i = 0; i < size; i++)
				{
					GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
				}						
			}
			
			// Link the two shaders together into a program.
			GLES20.glLinkProgram(programHandle);

			// Get the link status.
			final int[] linkStatus = new int[1];
			GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

			// If the link failed, delete the program.
			if (linkStatus[0] == 0) 
			{				
				//Log.e(TAG, "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
				GLES20.glDeleteProgram(programHandle);
				programHandle = 0;
			}
		}
		
		if (programHandle == 0)
		{
			throw new RuntimeException("Error creating program.");
		}
		
		return programHandle;
	}

}
