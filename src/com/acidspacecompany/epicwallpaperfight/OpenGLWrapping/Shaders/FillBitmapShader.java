package com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Shaders;

import android.content.Context;
import android.opengl.GLES20;
import com.acidspacecompany.epicwallpaperfight.OpenGLWrapping.Generators.ShaderGenerator;
import com.acidspacecompany.epicwallpaperfight.R;

public class FillBitmapShader {

    private int programId;

    private int aPosition;
    public int get_aPosition() { return aPosition; }

    private int aTexturePosition;
    public int get_aTexturePosition() { return aTexturePosition; }

    private int uTextureDimensions;
    public void setTextureDimensions(float x, float y) {GLES20.glUniform2f(uTextureDimensions, x,y);}

    private int uDX;
    public void setDX(float dx) {GLES20.glUniform1f(uDX, dx);}

    private int uOpacity;
    public void setOpacity(float opacity) {
        GLES20.glUniform1f(uOpacity, opacity);
    }

    private int uTextureUnit;
    public void setTexture(int textureUnit) {
        GLES20.glUniform1i(uTextureUnit, textureUnit);
    }

    public void use() {
        GLES20.glUseProgram(programId);
    }

    public void validate() {
        ShaderGenerator.validateProgram(programId);
    }

    public void delete() {
        GLES20.glDeleteProgram(programId);
    }

    public FillBitmapShader(Context context) {
        programId = ShaderGenerator.createProgram(context, R.raw.fillbitmap_vertex_shader, R.raw.fillbitmap_fragment_shader);
        aPosition = GLES20.glGetAttribLocation(programId, "a_Position");
        aTexturePosition = GLES20.glGetAttribLocation(programId, "a_TexturePosition");
        uTextureUnit = GLES20.glGetUniformLocation(programId, "u_TextureUnit");
        uTextureDimensions = GLES20.glGetUniformLocation(programId, "u_TextureDimensions");
        uOpacity = GLES20.glGetUniformLocation(programId, "u_Opacity");
        uDX = GLES20.glGetUniformLocation(programId, "u_DX");

    }

}
