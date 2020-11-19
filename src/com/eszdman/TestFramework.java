package com.eszdman;

import com.eszdman.opengl.*;

import java.awt.*;

import static org.lwjgl.opengl.GL11C.glViewport;

public class TestFramework {
    public static void main(String[] args) {
        GLContext glContext = new GLContext(300,300);
        //GLCoreBlockProcessing glCoreBlockProcessing = new GLCoreBlockProcessing(new Point(300,300),null,new GLFormat(GLFormat.DataType.UNSIGNED_8,4));
        GLProg glProg = glContext.mProgram;
        glProg.useProgramName("test.glsl");
        GLTexture outp = new GLTexture(300,300,new GLFormat(GLFormat.DataType.FLOAT_16,GLConst.WorkDim));
        glProg.drawBlocks(outp);
        outp.BufferLoad();
        glViewport(0, 0, 300, 300);
        glContext.loop();
    }
}
