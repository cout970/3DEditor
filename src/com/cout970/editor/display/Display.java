package com.cout970.editor.display;

import com.cout970.editor.ConfigurationFile;
import com.cout970.editor.Editor;
import com.cout970.editor.util.LoopTimer;
import com.cout970.editor.util.Vect2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL13;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by cout970 on 10/02/2016.
 */
public class Display {

    //las dimensiones de la ventana
    private static int WIDTH = 1260;
    private static int HEIGHT = 720;
    //el identificador de la ventana
    private static long window;
    //timer
    public static final LoopTimer counter = new LoopTimer();
    private static double delta;
    //callbacks
    private static GLFWErrorCallback errorCallback;
    private static FrameBufferSizeCallback frameBufferSizeCallback;


    public static void runLoop() {
        try {
            loop();
            glfwDestroyWindow(getWindow());
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            glfwTerminate();
            InputHandler.releaseEvents();
            frameBufferSizeCallback.release();
            errorCallback.release();
        }
    }

    private static void loop() {

        while (glfwWindowShouldClose(getWindow()) == GL_FALSE) {
            //contador de fps
            counter.loopTick();
            //la diferencia de tiempo entre este frame y el anterior
            delta = counter.getDelta();

            // se vacian los buffers
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

            //actualiza la posicion del raton
            InputHandler.updateCursor();
            //renderiza la escena y la interfaz
            Editor.getEditorHandler().update();
            Editor.getGuiHandler().update();

            //se intercambian los buffers
            glFlush();
            glfwSwapBuffers(getWindow());
            //se atienden los eventos
            glfwPollEvents();
        }
    }

    /**
     * crea y configura la ventana
     */
    public static void initWindow() {
        //se registra la funcion de salida de errores para que muestre en el
        //log los errores del gestor de ventanas
        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
        //se inicializxa el gestor de ventanas
        if (glfwInit() != GL_TRUE) {
            throw new IllegalStateException("No se pudo inicializar el gestor de ventanas GLFW");
        }

        //se cargan las configuraciones por defecto del gestor de ventanas
        glfwDefaultWindowHints();
        //se oculta la ventana mientras no esta configurada
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        //se permite reescalar la ventana
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        //se activan los bordes de la ventana
        glfwWindowHint(GLFW_DECORATED, GL_TRUE);

        glfwWindowHint(GLFW_STENCIL_BITS, 8);
        glfwWindowHint(GLFW_SAMPLES, 8);

        //se crea la ventana con las configuraciones anteriores
        window = GLFW.glfwCreateWindow(WIDTH, HEIGHT, Editor.EDITOR_NAME, NULL, NULL);
        if (window == NULL) { throw new RuntimeException("Error al crear la ventana con GLFW"); }

        //se registran las funciones que manejan eventos
        //eventos del teclado, raton
        InputHandler.registerEvents();
        //evento de reescalado de ventana
        glfwSetFramebufferSizeCallback(window, frameBufferSizeCallback = new FrameBufferSizeCallback());

        //obtiene el tamano de la pantalla para poder centrar la ventana
        ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        //centra la ventana
        glfwSetWindowPos(window,
                (GLFWvidmode.width(vidmode) - WIDTH) / 2,
                (GLFWvidmode.height(vidmode) - HEIGHT) / 2);

        //se selecciona la ventana
        glfwMakeContextCurrent(window);
        //se activa vsync para no tener mas de 60 fps,
        //porque es un editor no un juego, no son necesarios
        glfwSwapInterval(1);//1 v-sync, 0 no v-sync

        // hace la ventana visible
        glfwShowWindow(window);
    }

    public static void initOpenGL() {
        //inicia openGL
        GL.createCapabilities();
        //actualiza las variable WIDTH y HEIGHT al tamano de la ventana
        IntBuffer width = BufferUtils.createIntBuffer(1), height = BufferUtils.createIntBuffer(1);
        glfwGetFramebufferSize(window, width, height);
        WIDTH = width.get(0);
        HEIGHT = height.get(0);

        //abilita el uso de toda la ventana
        glViewport(0, 0, WIDTH, HEIGHT);
        glClear(GL_COLOR_BUFFER_BIT);
        //cambia el color de fondo
        int color = ConfigurationFile.INSTANCE.backgroundColor;
        glClearColor(((color >> 16)&0xFF)/255f, ((color >> 8)&0xFF)/255f, (color&0xFF)/255f, 1.0f);

        //activa anti-aliasing MSAA
        glEnable(GL13.GL_MULTISAMPLE);
    }

    public static void setGuiProjection() {
        glViewport(0, 0, WIDTH, HEIGHT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, WIDTH, HEIGHT, 0, -1, 1);
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
        return new Vect2i(WIDTH, HEIGHT);
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

    private static class FrameBufferSizeCallback extends GLFWFramebufferSizeCallback {

        @Override
        public void invoke(long window, int x, int y) {
            //evento de reescalado de ventana
            //DEBUG
            //Log.debug("x: " + x + ", y: " + y);

            // se evita errores al minimizar la ventana
            if (x != 0 || y != 0) {
                WIDTH = x;
                HEIGHT = y;
            }
        }
    }
}
