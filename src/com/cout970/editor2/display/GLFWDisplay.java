package com.cout970.editor2.display;

import com.cout970.editor2.Editor;
import com.cout970.editor2.render.texture.TextureStorage;
import com.cout970.editor2.util.LoopTimer;
import com.cout970.editor2.util.Vect2i;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by cout970 on 10/02/2016.
 */
public class GLFWDisplay {

    private static int WIDTH = 1260;
    private static int HEIGHT = 720;
    private static float FOV = 90f;
    private static float ASPECT_RATIO = (float) WIDTH / (float) HEIGHT;
    private static float zNEAR = 0.001f;
    private static float zFAR = 1000f;
    private static long window;
    private static boolean init = false;
    //timer
    public static final LoopTimer counter = new LoopTimer();
    private static double delta;
    private static double time;
    //callbacks
    private static GLFWErrorCallback errorCallback;
    private static InputHandler.KeyboardCallback keyCallback;
    private static InputHandler.MouseButtonCallback mouseButtonCallback;
    private static InputHandler.MouseWheelCallback mouseWheelCallback;
    private static InputHandler.MousePosCallback mousePosCallback;
    private static WindowSizeCallback windowSizeCallback;
    private static FrameBufferSizeCallback frameBufferSizeCallback;
    private static InputHandler.CharCallback charCallback;

    //handlers
    public static Handler2D handler2D;
    public static Handler3D handler3D;

    private static Vect2i frameBufferSize;


    public static void run() {
        try {
            loop();
            glfwDestroyWindow(getWindow());
        } finally {
            glfwTerminate();
            keyCallback.release();
            mouseButtonCallback.release();
            mouseWheelCallback.release();
            mousePosCallback.release();
            windowSizeCallback.release();
            frameBufferSizeCallback.release();
            charCallback.release();
            getErrorCallback().release();
        }
    }

    private static void loop() {

        while (glfwWindowShouldClose(getWindow()) == GL_FALSE) {
            counter.loopTick();

            delta = glfwGetTime() - time;
            time = glfwGetTime();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

            //updateAndRender code goes here
            InputHandler.updateCursor(true);
            handler3D.update();
            handler2D.update();

            glfwSwapBuffers(getWindow());
            glfwPollEvents();
        }
    }

    public static void init() {
        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
        if (glfwInit() != GL_TRUE)
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable
        glfwWindowHint(GLFW_DECORATED, GL_TRUE); // the window will have borders

        // Create the window
        window = GLFW.glfwCreateWindow(WIDTH, HEIGHT, Editor.EDITOR_NAME, NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, keyCallback = new InputHandler.KeyboardCallback());
        glfwSetMouseButtonCallback(window, mouseButtonCallback = new InputHandler.MouseButtonCallback());
        glfwSetScrollCallback(window, mouseWheelCallback = new InputHandler.MouseWheelCallback());
        glfwSetCursorPosCallback(window, mousePosCallback = new InputHandler.MousePosCallback());
        glfwSetWindowSizeCallback(window, windowSizeCallback = new WindowSizeCallback());
        glfwSetFramebufferSizeCallback(window, frameBufferSizeCallback = new FrameBufferSizeCallback());
        glfwSetCharCallback(window, charCallback = new InputHandler.CharCallback());

        // Get the resolution of the primary monitor
        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - WIDTH) / 2,
                (GLFWvidmode.height(vidmode) - HEIGHT) / 2);

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);//1 v-sync, 0 no v-sync

        // Make the window visible
        glfwShowWindow(window);

        GL.createCapabilities();
        IntBuffer width = BufferUtils.createIntBuffer(1), height = BufferUtils.createIntBuffer(1);
        glfwGetFramebufferSize(window, width, height);

        WIDTH = width.get(0);
        HEIGHT = height.get(0);

        glViewport(0, 0, WIDTH, HEIGHT);
        glClear(GL_COLOR_BUFFER_BIT);
        glEnable(GL_TEXTURE_2D);

        set3D();

        glClearColor(0.89f / 2, 0.882f / 2, 0.867f / 2, 1.0f);
//        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        handler2D = new Handler2D();
        handler3D = new Handler3D();

        InputHandler.registerMouseWheelCallback(handler3D);
        InputHandler.registerKeyboardCallback(handler2D);
        TextureStorage.INSTANCE.reloadTextures(Editor.EDITOR_NAME.toLowerCase());
        handler2D.init();
        handler3D.init();
        init = true;
    }

    public static void set3D() {

        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        Matrix4f m = new Matrix4f();
        m.setPerspective((float) Math.toRadians(FOV), ASPECT_RATIO, zNEAR, zFAR).get(fb);
        glMatrixMode(GL_PROJECTION);
        glLoadMatrixf(fb);
        m.setLookAt(0.0f, 0.0f, 10.0f,
                0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f).get(fb);
        glMatrixMode(GL_MODELVIEW);
        glLoadMatrixf(fb);

        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
    }

    public static void set2D() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public static long getWindow() {
        return window;
    }

    public static GLFWErrorCallback getErrorCallback() {
        return errorCallback;
    }

    public static int getWindowWidth() {
        return WIDTH;
    }

    public static int getWindowHeight() {
        return HEIGHT;
    }

    public static Vect2i getFrameBufferSize() {
        return frameBufferSize;
    }

    public static void terminate() {
        glfwSetWindowShouldClose(getWindow(), GL_TRUE);
    }

    public static double getDeltaNano() {
        return delta * 1E6;
    }

    public static double getDeltaMili() {
        return delta * 1E3;
    }

    public static double getDeltaSec() {
        return delta;
    }

    private static class WindowSizeCallback extends GLFWWindowSizeCallback {

        @Override
        public void invoke(long window, int x, int y) {
//            Log.debug("x: "+x+", y: "+y);
            WIDTH = x;
            HEIGHT = y;
            ASPECT_RATIO = (float) WIDTH / (float) HEIGHT;
        }
    }

    private static class FrameBufferSizeCallback extends GLFWFramebufferSizeCallback {

        @Override
        public void invoke(long window, int x, int y) {
//            Log.debug("x: " + x + ", y: " + y);
            frameBufferSize = new Vect2i(x, y);
            if (init) {
                glViewport(0, 0, x, y);
                handler2D.onResize();
            }
        }
    }
}
