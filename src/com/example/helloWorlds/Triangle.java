package com.example.helloWorlds;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Triangle {

    //All the buffers used for rendering
    private FloatBuffer vertexBuffer = null;
    private ShortBuffer indicesBuffer = null;

    private int numOfIndices = 0;

    private float vertexList[] = {
            -1.0f,-1.0f,//0.0f, //left
            1.0f,-1.0f,//0.0f, //right
            0.0f,1.0f,//0.0f	//top
    };


    // Set triangle border buffer with vertex indices
    private short indexList[] = {
            0,1,2
    };

    public void setPos(float x, float y) {

        vertexList = new float[]{
                x-1f,  y-1f,// 0, //left
                x+1f,  y-1f,// 0, //right
                x,     y+1f,// 0	//top
        };
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertexList.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertexList);
        vertexBuffer.position(0);
    }

    public Triangle(){

        // Set vertex buffer
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertexList.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertexList);
        vertexBuffer.position(0);

        // Set indices buffer
        numOfIndices = indexList.length;
        ByteBuffer tbibb = ByteBuffer.allocateDirect(numOfIndices * 2);
        tbibb.order(ByteOrder.nativeOrder());
        indicesBuffer = tbibb.asShortBuffer();
        indicesBuffer.put(indexList);
        indicesBuffer.position(0);

    }

    public void draw(GL10 gl){

        float c1=OpenGLActivity.f;

        float c2=OpenGLActivity.f+0.33f;
        if (c2>1) c2-=1f;

        float c3=OpenGLActivity.f+0.66f;
        if (c3>1) c3-=1f;

        float colorList[] = {
                c1, c2, c3, 1.0f,
                c3, c1, c2, 1.0f,
                c2, c3, c1, 1.0f
        };
        ByteBuffer cbb = ByteBuffer.allocateDirect(colorList.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        FloatBuffer colorBuffer;
        colorBuffer = cbb.asFloatBuffer();
        colorBuffer.put(colorList);
        colorBuffer.position(0);

        gl.glFrontFace(GL10.GL_CW);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES, numOfIndices,
                GL10.GL_UNSIGNED_SHORT, indicesBuffer);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }



}