package com.cout970.editor;

import com.cout970.editor.util.LoopTimer;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.Callbacks.glfwSetCallback;
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
    private static float zNEAR = 0.1f;
    private static float zFAR = 1000f;
    private static long window;
    //timer
    public static final LoopTimer counter = new LoopTimer();
    private static double delta;
    private static double time;
    //callbacks
    private static GLFWErrorCallback errorCallback;
    private static InputHandler.GLFWKeyboardCallback keyCallback;
    private static InputHandler.GLFWMouseCallback mouseCallback;

    //handlers
    public static Handler2D handler2D;
    public static Handler3D handler3D;

    public static void run() {
        try {
            init();
            loop();

            glfwDestroyWindow(getWindow());
            getKeyCallback().release();
        } finally {
            glfwTerminate();
            getErrorCallback().release();
        }
    }

    private static void loop() {

        while (glfwWindowShouldClose(getWindow()) == GL_FALSE) {
            counter.loopTick();
            delta = glfwGetTime() - time;
            time = glfwGetTime();

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

            //render code goes here
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
        window = glfwCreateWindow(WIDTH, HEIGHT, Editor.EDITOR_NAME, NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, keyCallback = new InputHandler.GLFWKeyboardCallback());
        glfwSetCallback(window, mouseCallback = new InputHandler.GLFWMouseCallback());

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

//        StateManager.INSTANCE.changeState(new GameStateLoading());

        GL.createCapabilities();
        IntBuffer width = BufferUtils.createIntBuffer(1), height = BufferUtils.createIntBuffer(1);
        glfwGetFramebufferSize(window, width, height);

        WIDTH = width.get(0);
        HEIGHT = height.get(0);

        glViewport(0, 0, WIDTH, HEIGHT);
        glClear(GL_COLOR_BUFFER_BIT);

        set3D();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
//        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        handler2D = new Handler2D();
        handler3D = new Handler3D();
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

    public static InputHandler.GLFWKeyboardCallback getKeyCallback() {
        return keyCallback;
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

    public static void terminate() {
        glfwSetWindowShouldClose(getWindow(), GL_TRUE);
    }

    public static double getDeltaNano() {
        return delta / 1E6;
    }

    public static double getDeltaMili() {
        return delta / 1E3;
    }

    public static double getDeltaSec() {
        return delta;
    }
}
