package com.eszdman.opengl;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;
import org.lwjgl.system.Platform;

import java.util.Objects;

import static com.eszdman.GLFWUtil.glfwInvoke;
import static org.lwjgl.glfw.GLFW.*;

public class GLContext implements AutoCloseable {
    private long window;
    public GLProg mProgram;
    private Callback debugProc;
    public GLContext(int surfaceWidth, int surfaceHeight){
        GLFWErrorCallback.createPrint().set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize glfw");
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE);
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
        if (Platform.get() == Platform.MACOSX) {
            glfwWindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW_FALSE);
        }
        int WIDTH  = surfaceWidth;
        int HEIGHT = surfaceHeight;

        long window = glfwCreateWindow(WIDTH, HEIGHT, "GLContext", 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        glfwSetWindowSizeLimits(window, WIDTH, HEIGHT, GLFW_DONT_CARE, GLFW_DONT_CARE);
        //glfwSetWindowAspectRatio(window, 1, 1);

        long monitor = glfwGetPrimaryMonitor();

        GLFWVidMode vidmode = Objects.requireNonNull(glfwGetVideoMode(monitor));
        // Center window
        glfwSetWindowPos(
                window,
                (vidmode.width() - WIDTH) / 2,
                (vidmode.height() - HEIGHT) / 2
        );
        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        debugProc = GLUtil.setupDebugMessageCallback();



        glfwSwapInterval(1);
        glfwShowWindow(window);
        glfwInvoke(window, null, null);
        this.window = window;
        mProgram = new GLProg();
    }

    public void loop() {
        long lastUpdate = System.currentTimeMillis();
        while (!glfwWindowShouldClose(window)){
            glfwPollEvents();
            //glViewport(0, 0, 300, 300);

            mProgram.draw();
            glfwSwapBuffers(window);
        }
        int frames = 0;
        /*while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();


            glfwSwapBuffers(window);

            frames++;

            long time = System.currentTimeMillis();

            int UPDATE_EVERY = 5; // seconds
            if (UPDATE_EVERY * 1000L <= time - lastUpdate) {
                lastUpdate = time;

                System.out.printf("%d frames in %d seconds = %.2f fps\n", frames, UPDATE_EVERY, (frames / (float)UPDATE_EVERY));
                frames = 0;
            }
        }*/
    }

    @Override
    public void close() throws Exception {
        /*GL.setCapabilities(null);

        if (debugProc != null) {
            debugProc.free();
        }

        if (window != 0) {
            glfwFreeCallbacks(window);
            glfwDestroyWindow(window);
        }
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();*/
    }
}
