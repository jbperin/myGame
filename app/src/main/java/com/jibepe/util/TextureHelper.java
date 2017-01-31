package com.jibepe.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class TextureHelper
{

	public static int loadTexture(final Context context, final int resourceId)
	{
		final int[] textureHandle = new int[1];
		
		GLES20.glGenTextures(1, textureHandle, 0);
		
		if (textureHandle[0] != 0)
		{
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;	// No pre-scaling

			// Read in the resource
			final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
						
			// Bind to the texture in OpenGL
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
			
			// Set filtering
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
			
			// Load the bitmap into the bound texture.
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
			
			// Recycle the bitmap, since its data has been loaded into OpenGL.
			bitmap.recycle();						
		}
		
		if (textureHandle[0] == 0)
		{
			throw new RuntimeException("Error loading texture.");
		}
		
		return textureHandle[0];
	}

	public static int loadAlphaTexture(final Context context, final int resourceId)
	{
	    int[] textureId = new int[1];
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;	// No pre-scaling

		// Read in the resource
		final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
	    byte[] buffer = new byte[bitmap.getWidth() * bitmap.getHeight() * 4];

	    for ( int y = 0; y < bitmap.getHeight(); y++ )
	        for ( int x = 0; x < bitmap.getWidth(); x++ )
	        {
	            int pixel = bitmap.getPixel(x, y);
	            buffer[(y * bitmap.getWidth() + x) * 4 + 0] = (byte)((pixel >> 16) & 0xFF);
	            buffer[(y * bitmap.getWidth() + x) * 4 + 1] = (byte)((pixel >> 8) & 0xFF);
	            buffer[(y * bitmap.getWidth() + x) * 4 + 2] = (byte)((pixel >> 0) & 0xFF);
	            buffer[(y * bitmap.getWidth() + x) * 4 + 3] = (byte)((pixel >> 24) & 0xFF);
	        }
	    
	    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bitmap.getWidth() * bitmap.getHeight() * 4);
	    byteBuffer.put(buffer).position(0);

	    GLES20.glGenTextures ( 1, textureId, 0 );
	    GLES20.glBindTexture ( GLES20.GL_TEXTURE_2D, textureId[0] );

	    GLES20.glTexImage2D ( GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap.getWidth(), bitmap.getHeight(), 0, 
	                          GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, byteBuffer );

	    GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR );
	    GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR );
	    GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE );
	    GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE );
		GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
	    return textureId[0];	}

	
	public static int loadPNGTexture(final InputStream in)
	{
	    int[] textureId = new int[1];
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;	// No pre-scaling

		// Read in the resource
		final Bitmap bitmap = BitmapFactory.decodeStream(in);// BitmapFactory.decodeResource(context.getResources(), resourceId, options);
	    byte[] buffer = new byte[bitmap.getWidth() * bitmap.getHeight() * 4];

	    for ( int y = 0; y < bitmap.getHeight(); y++ )
	        for ( int x = 0; x < bitmap.getWidth(); x++ )
	        {
	            int pixel = bitmap.getPixel(x, y);
	            buffer[(y * bitmap.getWidth() + x) * 4 + 0] = (byte)((pixel >> 16) & 0xFF);
	            buffer[(y * bitmap.getWidth() + x) * 4 + 1] = (byte)((pixel >> 8) & 0xFF);
	            buffer[(y * bitmap.getWidth() + x) * 4 + 2] = (byte)((pixel >> 0) & 0xFF);
	            buffer[(y * bitmap.getWidth() + x) * 4 + 3] = (byte)((pixel >> 24) & 0xFF);
	        }
	    
	    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bitmap.getWidth() * bitmap.getHeight() * 4);
	    byteBuffer.put(buffer).position(0);

	    GLES20.glGenTextures ( 1, textureId, 0 );
	    GLES20.glBindTexture ( GLES20.GL_TEXTURE_2D, textureId[0] );

	    GLES20.glTexImage2D ( GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap.getWidth(), bitmap.getHeight(), 0, 
	                          GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, byteBuffer );

	    GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR );
	    GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR );
	    GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE );
	    GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE );
		GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
	    return textureId[0];
	}

	public static int loadPNGTexture(String filepath)
	{
		int[] textureId = new int[1];
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;	// No pre-scaling
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		// Read in the resource
		File file = new File(filepath);
		if(file.exists()) {
				final Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);// BitmapFactory.decodeResource(context.getResources(), resourceId, options);
				if (bitmap != null) {
					byte[] buffer = new byte[bitmap.getWidth() * bitmap.getHeight() * 4];

					for (int y = 0; y < bitmap.getHeight(); y++)
						for (int x = 0; x < bitmap.getWidth(); x++) {
							int pixel = bitmap.getPixel(x, y);
							buffer[(y * bitmap.getWidth() + x) * 4 + 0] = (byte) ((pixel >> 16) & 0xFF);
							buffer[(y * bitmap.getWidth() + x) * 4 + 1] = (byte) ((pixel >> 8) & 0xFF);
							buffer[(y * bitmap.getWidth() + x) * 4 + 2] = (byte) ((pixel >> 0) & 0xFF);
							buffer[(y * bitmap.getWidth() + x) * 4 + 3] = (byte) ((pixel >> 24) & 0xFF);
						}

					ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bitmap.getWidth() * bitmap.getHeight() * 4);
					byteBuffer.put(buffer).position(0);

					GLES20.glGenTextures(1, textureId, 0);
					GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId[0]);

					GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap.getWidth(), bitmap.getHeight(), 0,
							GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, byteBuffer);

					GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
					GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
					GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
					GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
					GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

					return textureId[0];
				} else {
					return (-1);
				}
		} else {
			return (-1);
		}
	}

}
