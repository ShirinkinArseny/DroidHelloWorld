package live.wallpaper.OpenGLIntegration.Shaders;

import java.nio.FloatBuffer;

public interface Shader {

    public void setMatrix(FloatBuffer matrix);
    public int get_aPosition();
    public void validate();
    public void use();
    public void delete();



}
