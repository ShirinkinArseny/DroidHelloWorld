package live.wallpaper.OpenGLIntegration.Shaders;

import android.content.Context;
import android.opengl.GLES20;
import live.wallpaper.OpenGLIntegration.Shaders.Generators.ShaderGenerator;
import live.wallpaper.R;

import java.nio.FloatBuffer;

public class FontShader implements Shader {
    private int programId;

    private int uMatrix;
    public void setMatrix(FloatBuffer matrix) {
        GLES20.glUniformMatrix4fv(uMatrix, 1, false, matrix);
    }
    public void setMatrix(float[] matrix, int offset) {
        GLES20.glUniformMatrix4fv(uMatrix, 1, false, matrix, offset);
    }

    private int aPosition;
    public int get_aPosition() { return aPosition; }


    public void validate() {
        ShaderGenerator.validateProgram(programId);
    }

    private int aTextureCoordinates;
    public int get_aTextureCoordinates() { return aTextureCoordinates; }

    private int uTextureUnit;
    public void setTexture(int textureUnit) {
        GLES20.glUniform1i(uTextureUnit, textureUnit);
    }

    private int uColor;
    public void setColor(float r, float g, float b, float a) {
        GLES20.glUniform4f(uColor, r,g,b,a);
    }

    public void use() {
        GLES20.glUseProgram(programId);
    }

    public void delete() {
        GLES20.glDeleteProgram(programId);
    }

    public FontShader(Context context) {
        programId = ShaderGenerator.createProgram(context, R.raw.font_vertex_shader, R.raw.font_fragment_shader);
        uMatrix = GLES20.glGetUniformLocation(programId, "u_Matrix");
        aPosition = GLES20.glGetAttribLocation(programId, "a_Position");
        aTextureCoordinates = GLES20.glGetAttribLocation(programId, "a_TextureCoordinates");
        uTextureUnit = GLES20.glGetUniformLocation(programId, "u_TextureUnit");
        uColor = GLES20.glGetUniformLocation(programId, "u_Color");
    }
}
