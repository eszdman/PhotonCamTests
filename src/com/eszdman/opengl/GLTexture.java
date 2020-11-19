package com.eszdman.opengl;


import java.awt.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import static com.eszdman.opengl.GLCoreBlockProcessing.checkEglError;
import static org.lwjgl.opengl.GL46C.*;

public class GLTexture implements java.lang.AutoCloseable {
    public Point mSize;
    public final int mGLFormat;
    public final int mTextureID;
    public final GLFormat mFormat;
    public int filter;
    public int wrap;
    private int Cur;
    public GLTexture(GLTexture in) {
        this(in.mSize,in.mFormat,null,in.filter,in.wrap,0);
    }
    public GLTexture(int sizeX, int sizeY, GLFormat glFormat, Buffer pixels) {
        this(new Point(sizeX, sizeY), new GLFormat(glFormat), pixels, GL_NEAREST, GL_CLAMP_TO_EDGE,0);
    }
    public GLTexture(int sizeX, int sizeY, GLFormat glFormat, Buffer pixels,int textureFilter, int textureWrapper) {
        this(new Point(sizeX, sizeY), new GLFormat(glFormat), pixels, textureFilter, textureWrapper,0);
    }
    public GLTexture(Point size, GLFormat glFormat, Buffer pixels,int textureFilter, int textureWrapper) {
        this(new Point(size), new GLFormat(glFormat), pixels, textureFilter, textureWrapper,0);
    }
    public GLTexture(Point size, GLFormat glFormat, Buffer pixels) {
        this(new Point(size), new GLFormat(glFormat), pixels, GL_NEAREST, GL_CLAMP_TO_EDGE,0);
    }
    public GLTexture(int sizeX, int sizeY, GLFormat glFormat,int level) {
        this(new Point(sizeX, sizeY), new GLFormat(glFormat), null, GL_NEAREST, GL_CLAMP_TO_EDGE,level);
    }
    public GLTexture(int sizeX, int sizeY, GLFormat glFormat) {
        this(new Point(sizeX, sizeY), new GLFormat(glFormat), null, GL_NEAREST, GL_CLAMP_TO_EDGE,0);
    }
    public GLTexture(int sizeX, int sizeY, GLFormat glFormat,int textureFilter, int textureWrapper) {
        this(new Point(sizeX, sizeY), new GLFormat(glFormat), null, textureFilter, textureWrapper,0);
    }
    public GLTexture(Point size, GLFormat glFormat,int level) {
        this(new Point(size), new GLFormat(glFormat), null, GL_NEAREST, GL_CLAMP_TO_EDGE,level);
    }
    public GLTexture(Point size, GLFormat glFormat) {
        this(new Point(size), new GLFormat(glFormat), null, GL_NEAREST, GL_CLAMP_TO_EDGE,0);
    }

    public GLTexture(Point size, GLFormat glFormat, Buffer pixels, int textureFilter, int textureWrapper,int level) {
        filter = textureFilter;
        wrap = textureWrapper;
        mFormat = glFormat;
        this.mSize = size;
        this.mGLFormat = glFormat.getGLFormatInternal();
        int[] TexID = new int[1];
        glGenTextures(TexID);
        mTextureID = TexID[0];
        glActiveTexture(GL_TEXTURE1+mTextureID);
        glBindTexture(GL_TEXTURE_2D, mTextureID);
        System.out.println("GLTexture"+"Texture ID:"+mTextureID);
        if(pixels != null)
        glTexImage2D(GL_TEXTURE_2D, level, glFormat.getGLFormatInternal(), size.x, size.y, 0, glFormat.getGLFormatExternal(), glFormat.getGLType(), (ByteBuffer)pixels);
        else {
            //glTexStorage2D(GL_TEXTURE_2D, 1, glFormat.getGLFormatInternal(), size.x, size.y);
            glTexImage2D(GL_TEXTURE_2D, level, glFormat.getGLFormatInternal(), size.x, size.y, 0, glFormat.getGLFormatExternal(), glFormat.getGLType(), (ByteBuffer)pixels);
        }
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, textureFilter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, textureFilter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, textureWrapper);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, textureWrapper);
        checkEglError("Tex glTexParameteri");
    }

    public void BufferLoad() {
        int[] frameBuffer = new int[1];
        glGenFramebuffers(frameBuffer);
        glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer[0]);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, mTextureID, 0);
        glViewport(0, 0, mSize.x, mSize.y);
        checkEglError("Tex BufferLoad");
    }

    public void bind(int slot) {
        glActiveTexture(slot);
        glBindTexture(GL_TEXTURE_2D, mTextureID);
        checkEglError("Tex bind");
    }

    public ByteBuffer textureBuffer(GLFormat outputFormat) {
        ByteBuffer buffer = ByteBuffer.allocate(mSize.x * mSize.y * outputFormat.mFormat.mSize * outputFormat.mChannels);
        glReadPixels(0, 0, mSize.x, mSize.y, outputFormat.getGLFormatExternal(), outputFormat.getGLType(), buffer);
        return buffer;
    }

    @Override
    public String toString() {
        return "GLTexture{" +
                "mSize=" + mSize +
                ", mGLFormat=" + mGLFormat +
                ", mTextureID=" + mTextureID +
                ", mFormat=" + mFormat +
                '}';
    }

    @Override
    public void close() {
        glDeleteTextures(new int[]{mTextureID});
    }
}
