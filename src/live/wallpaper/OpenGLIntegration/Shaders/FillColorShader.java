package live.wallpaper.OpenGLIntegration.Shaders;

import android.content.Context;
import android.opengl.GLES20;
import live.wallpaper.OpenGLIntegration.Shaders.Generators.ShaderGenerator;
import live.wallpaper.R;

import java.nio.FloatBuffer;

public class FillColorShader implements Shader {

    private int programId;
    public void use() {
        GLES20.glUseProgram(programId);
    }

    public void delete() {
        GLES20.glDeleteProgram(programId);
    }

    private int uMatrix;
    public void setMatrix(FloatBuffer matrix) {
        GLES20.glUniformMatrix4fv(uMatrix, 1, false, matrix);
    }

    private int aPosition;
    public int get_aPosition() { return aPosition; }

    public void validate() {
        ShaderGenerator.validateProgram(programId);
    }

    private int uColor;
    public void setColor(float r, float g, float b, float a) {
        GLES20.glUniform4f(uColor, r,g,b,a);
    }

    public FillColorShader(Context context) {
        programId = ShaderGenerator.createProgram(context, R.raw.fillcolor_vertex_shader, R.raw.fillcolor_fragment_shader);
        uMatrix = GLES20.glGetUniformLocation(programId, "u_Matrix");
        aPosition = GLES20.glGetAttribLocation(programId, "a_Position");
        uColor = GLES20.glGetUniformLocation(programId, "u_Color");
    }




}
