package com.example.helloWorlds;

import android.content.Context;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLSurfaceView extends GLSurfaceView {

    MyRenderer m;
    float w;
    float h;
    float wh;


    public MyGLSurfaceView(Context context, Point size){
        super(context);
        w=size.x;
        h=size.y;
        wh=w/h;

        // Set the Renderer for drawing on the GLSurfaceView
        m=new MyRenderer();
        setRenderer(m);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = (w-2*e.getX())/h;
        float y = 1-2*e.getY()/h;

        Log.i("MyApp", x+" "+y);

        m.setPos(x, y);
        return true;
    }
}

class MyRenderer implements GLSurfaceView.Renderer {


    Triangle tr = new Triangle();

    /*  Called once to set up the view's OpenGL ES environment. */
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glDisable(GL10.GL_DITHER);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        gl.glClearColor(0f, 0f, 0f, 1f);// set background color
        gl.glClearDepthf(1f);
    }

    /* Called if the geometry of the view changes,
     *  for example when the device's screen orientation changes.*/
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustumf(-ratio, ratio, -1, 1, 1, 25);
    }

    public void setPos(float dx, float dy) {
        tr.setPos(dx, dy);
    }

    /* Called for each redraw of the view. */
    public void onDrawFrame(GL10 gl) {
        //gl.glDisable(GL10.GL_DITHER);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl, 0, 0, -1.0000001f, 0, 0, 0, 0, 2, 0);

        tr.draw(gl);
    }
}