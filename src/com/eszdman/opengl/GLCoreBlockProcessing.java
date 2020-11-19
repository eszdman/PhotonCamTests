package com.eszdman.opengl;

import com.eszdman.Bitmap;
import org.lwjgl.opengl.GL46C;
import org.lwjgl.opengl.GL46C.*;

import java.awt.*;
import java.nio.ByteBuffer;
import static org.lwjgl.opengl.GL46C.*;
public class GLCoreBlockProcessing extends GLContext {
    private static String TAG = "GLCoreBlockProcessing";
    public Bitmap mOut = null;
    private final int mOutWidth, mOutHeight;
    public final ByteBuffer mBlockBuffer;
    public final ByteBuffer mOutBuffer;
    private final GLFormat mglFormat;
    public boolean direct = false;

    public static void checkEglError(String op) {
        int error = GL46C.glGetError();
        if (error != GL46C.GL_NO_ERROR) {
            String msg = op + ": glError: " + (error) + " (" + Integer.toHexString(error) + ")";
            String TAG = "GLCoreBlockProcessing";
            System.out.println(TAG+msg);
        }
    }

    public GLCoreBlockProcessing(Point size, Bitmap out, GLFormat glFormat) {
        this(size, glFormat);
        mOut = out;
    }

    public GLCoreBlockProcessing(Point size, GLFormat glFormat) {
        super(size.x, GLConst.TileSize);
        mglFormat = glFormat;
        mOutWidth = size.x;
        mOutHeight = size.y;
        mBlockBuffer = ByteBuffer.allocate(mOutWidth * GLConst.TileSize * mglFormat.mFormat.mSize * mglFormat.mChannels);
        final int capacity = mOutWidth * mOutHeight * mglFormat.mFormat.mSize * mglFormat.mChannels;
        if(direct) mOutBuffer = ByteBuffer.allocateDirect(capacity);
        else {
            mOutBuffer = ByteBuffer.allocate(capacity);
        }
    }

    public void drawBlocksToOutput() {
        GLProg program = super.mProgram;
        GLBlockDivider divider = new GLBlockDivider(mOutHeight, GLConst.TileSize);
        int[] row = new int[2];
        mOutBuffer.position(0);
        mBlockBuffer.position(0);
        while (divider.nextBlock(row)) {
            int y = row[0];
            int height = row[1];
            glViewport(0, 0, mOutWidth, height);
            checkEglError("glViewport");
            program.setVar("yOffset", y);
            program.draw();
            checkEglError("program");
            mBlockBuffer.position(0);
            glReadPixels(0, 0, mOutWidth, height, mglFormat.getGLFormatExternal(), mglFormat.getGLType(), mBlockBuffer);
            checkEglError("glReadPixels");
            if (height < GLConst.TileSize) {
                // This can only happen 2 times at edges
                byte[] data = new byte[mOutWidth * height * mglFormat.mFormat.mSize * mglFormat.mChannels];
                mBlockBuffer.get(data);
                mOutBuffer.put(data);
            } else {
                mOutBuffer.put(mBlockBuffer);
            }
        }
        mOutBuffer.position(0);
        if (mOut != null) mOut.copyPixelsFromBuffer(mOutBuffer);
    }

    private final int[] bind = new int[1];

    public ByteBuffer drawBlocksToOutput(Point size, GLFormat glFormat) {
        glBindFramebuffer(GL_FRAMEBUFFER, bind[0]);
        checkEglError("glBindFramebuffer");
        GLProg program = super.mProgram;
        GLBlockDivider divider = new GLBlockDivider(size.y, GLConst.TileSize);
        int[] row = new int[2];
        ByteBuffer mBlockBuffer = ByteBuffer.allocate(size.x * GLConst.TileSize * glFormat.mFormat.mSize * glFormat.mChannels);
        ByteBuffer mOutBuffer = ByteBuffer.allocate(size.x * size.y * glFormat.mFormat.mSize * glFormat.mChannels);
        mOutBuffer.position(0);
        mBlockBuffer.position(0);
        while (divider.nextBlock(row)) {
            int y = row[0];
            int height = row[1];
            glViewport(0, 0, size.x, height);
            checkEglError("glViewport");
            program.setVar("yOffset", y);
            program.draw();
            checkEglError("program");
            mBlockBuffer.position(0);
            glReadPixels(0, 0, size.x, height, glFormat.getGLFormatExternal(), glFormat.getGLType(), mBlockBuffer);
            checkEglError("glReadPixels");
            if (height < GLConst.TileSize) {
                // This can only happen 2 times at edges
                byte[] data = new byte[size.x * height * glFormat.mFormat.mSize * glFormat.mChannels];
                mBlockBuffer.get(data);
                mOutBuffer.put(data);
            } else {
                mOutBuffer.put(mBlockBuffer);
            }
        }
        mOutBuffer.position(0);
        return mOutBuffer;
    }
}
