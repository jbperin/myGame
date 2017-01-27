package com.jibepe.labo3d;

import android.content.Context;
import android.opengl.GLES20;
import com.jibepe.util.RawResourceReader;
import com.jibepe.util.ShaderHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tbpk7658 on 25/01/2017.
 */
public class ShaderHandler {

    private Context mContext;

    private int mSolidPointProgram;
    private int mSolidLineProgram;
    private int mSolidColorLightProgram;
    private int mSolidUColorLightProgram;
    private int mSolidTexColorLightProgram;
    private int mSolidTexColorNoLightProgram;
    private int mSolidTexLightProgram;

    public final static String sSolidPointProgram = "SolidPointProgram";
    public final static String sSolidLineProgram = "SolidLineProgram";
    public final static String sSolidColorLightProgram = "SolidColorLightProgram";
    public final static String sSolidUColorLightProgram = "SolidUColorLightProgram";
    public final static String sSolidTexColorLightProgram = "SolidTexColorLightProgram";
    public final static String sSolidTexColorNoLightProgram = "SolidTexColorNoLightProgram";
    public final static String sSolidTexLightProgram = "SolidTexLightProgram";

    private static ShaderHandler INSTANCE = new ShaderHandler();
    private Map<String, Integer> dShaderHandler;

    private ShaderHandler() {
        dShaderHandler = new HashMap<String, Integer>();
    }
    public static ShaderHandler getInstance()
    {	return INSTANCE;
    }
    public void reset(){
        dShaderHandler.clear();
        //dShaderHandler = new HashMap<String, Integer>();
    }
    public void setContext (Context context) {
        mContext = context;
    }
    /** This is a handle to our per-vertex cube shading program. */
    private String getShader(int ressourceID)
    {
        return RawResourceReader.readTextFileFromRawResource(mContext, ressourceID);
    }

    public int getShaderProgramId(String progname) {
        if (! dShaderHandler.containsKey(progname)){
            return (getShaderProgram (progname));
        } else {
            return (dShaderHandler.get(progname));
        }
    }

    public int getShaderProgram(String shaderName){

        if (shaderName.equals(sSolidPointProgram)){

            final int pointShader_V = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, getShader(R.raw.solid_color_point_v));
            final int pointShader_F = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, getShader(R.raw.solid_color_point_f));
            mSolidPointProgram = ShaderHelper.createAndLinkProgram(pointShader_V, pointShader_F,
                    new String[]{"a_Position"});
            dShaderHandler.put(shaderName, mSolidPointProgram);

            return (mSolidPointProgram);
        }else if (shaderName.equals(sSolidLineProgram)){

            final int LineShader_V = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, getShader(R.raw.solid_ucolor_nolight_v));
            final int LineShader_F = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, getShader(R.raw.solid_ucolor_nolight_f));
            mSolidLineProgram = ShaderHelper.createAndLinkProgram(LineShader_V, LineShader_F,
                    new String[]{"a_Position"});

            dShaderHandler.put(shaderName, mSolidLineProgram);
            return (mSolidLineProgram);
        }else if (shaderName.equals(sSolidColorLightProgram)){

            final int sclShader_V = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, getShader(R.raw.solid_color_light_v));
            final int sclShader_F = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, getShader(R.raw.solid_color_light_f));
            mSolidColorLightProgram = ShaderHelper.createAndLinkProgram(sclShader_V, sclShader_F,
                    new String[]{"a_Position", "a_Color", "a_Normal"});

            dShaderHandler.put(shaderName, mSolidColorLightProgram);
            return (mSolidColorLightProgram);
        }else if (shaderName.equals(sSolidUColorLightProgram)){

            final int suclShader_V = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, getShader(R.raw.solid_ucolor_light_v));
            final int suclShader_F = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, getShader(R.raw.solid_ucolor_light_f));
            mSolidUColorLightProgram = ShaderHelper.createAndLinkProgram(suclShader_V, suclShader_F,
                    new String[]{"a_Position", "a_Normal"});


            dShaderHandler.put(shaderName, mSolidUColorLightProgram);
            return (mSolidUColorLightProgram);
        }else if (shaderName.equals(sSolidTexColorLightProgram)){


            final int stclShader_V = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, getShader(R.raw.solid_tex_ucolor_light_v));
            final int stclShader_F = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, getShader(R.raw.solid_tex_ucolor_light_f));
            mSolidTexColorLightProgram = ShaderHelper.createAndLinkProgram(stclShader_V, stclShader_F,
                    new String[]{"a_Position", "a_Normal", "a_TexCoordinate"});



            dShaderHandler.put(shaderName, mSolidTexColorLightProgram);
            return (mSolidTexColorLightProgram);
        }else if (shaderName.equals(sSolidTexColorNoLightProgram)){


            final int stcShader_V = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, getShader(R.raw.solid_tex_ucolor_nolight_v));
            final int stcShader_F = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, getShader(R.raw.solid_tex_ucolor_nolight_f));
            mSolidTexColorNoLightProgram = ShaderHelper.createAndLinkProgram(stcShader_V, stcShader_F,
                    new String[]{"a_Position", "a_TexCoordinate"});

            dShaderHandler.put(shaderName, mSolidTexColorNoLightProgram);
            return (mSolidTexColorNoLightProgram);
        }else if (shaderName.equals(sSolidTexLightProgram)){


            final int stlShader_V = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, getShader(R.raw.solid_tex_light_v));
            final int stlShader_F = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, getShader(R.raw.solid_tex_light_f));
            mSolidTexLightProgram = ShaderHelper.createAndLinkProgram(stlShader_V, stlShader_F,
                    new String[]{"a_Position", "a_Normal", "a_TexCoordinate"});

            dShaderHandler.put(shaderName, mSolidTexLightProgram);
            return (mSolidTexLightProgram);
        } else {
            return (mSolidPointProgram);
            //return -1;
        }
    }

}
