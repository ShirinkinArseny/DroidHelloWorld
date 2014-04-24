package com.example.helloWorlds;

import android.app.Activity;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

public class OpenGLActivity extends Activity {

    public static float f=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // (NEW)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // (NEW)
        WindowManager w = getWindowManager();
        Display d = w.getDefaultDisplay();
        Point p=new Point(0 ,0);
        d.getSize(p);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                f+=0.01;
                if (f>=1) f-=1f;
            }
        }, 0, 15);


        GLSurfaceView mGLView = new MyGLSurfaceView(this, p);
        setContentView(mGLView);

    }
}