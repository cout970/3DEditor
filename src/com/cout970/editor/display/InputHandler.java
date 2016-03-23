package com.cout970.editor.display;

import com.cout970.editor.util.Vect2d;
import com.cout970.editor.util.Vect2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by cout970 on 10/02/2016.
 */
public class InputHandler {

    //metodos para manejar eventos
    private static InputHandler.KeyboardCallback keyCallback;
    private static InputHandler.MouseButtonCallback mouseButtonCallback;
    private static InputHandler.MouseWheelCallback mouseWheelCallback;
    private static InputHandler.CharCallback charCallback;

    //obetos a actualizar cuando se recibe un evento
    private static List<IMouseWheelCallback> mouseWheelCallbacks = new ArrayList<>();
    private static List<IKeyboardCallback> keyboardCallbacks = new ArrayList<>();
    private static List<IMouseButtonCallback> mouseButtonCallbacks = new ArrayList<>();
    private static List<ITextCallback> textCallbacks = new ArrayList<>();

    //movimiento del raton
    private static double cursorDifX;
    private static double cursorDifY;
    //posicion del raton
    private static double cursorPosX;
    private static double cursorPosY;
    //se guarda si el boton del raton esta pulsado o no
    private static boolean mouseButton0;
    private static boolean mouseButton1;
    private static boolean mouseButton2;

    /**
     * re registran los objetos a actualizar cuando se recibe un evento
     */
    public static void registerKeyboardCallback(IKeyboardCallback callback) {
        keyboardCallbacks.add(callback);
    }

    public static void registerMouseWheelCallback(IMouseWheelCallback callback) {
        mouseWheelCallbacks.add(callback);
    }

    public static void registerMouseButtonCallback(IMouseButtonCallback callback) {
        mouseButtonCallbacks.add(callback);
    }

    public static void registerTextCallback(ITextCallback callback) {
        textCallbacks.add(callback);
    }

    /**
     * Se actualiza la posicion del raton
     */
    public static void updateCursor() {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

        GLFW.glfwGetCursorPos(Display.getWindow(), x, y);
        x.rewind();
        y.rewind();
        cursorDifX = cursorPosX - x.get();
        cursorDifY = cursorPosY - y.get();
        x.rewind();
        y.rewind();
        cursorPosX = x.get();
        cursorPosY = y.get();
    }

    public static double getCursorDiffX() {
        return cursorDifX;
    }

    public static double getCursorDiffY() {
        return cursorDifY;
    }

    public static boolean isMouseButtonPress(MouseButton button) {
        switch (button) {
            case LEFT:
                return mouseButton0;
            case RIGHT:
                return mouseButton1;
            case MIDDLE:
                return mouseButton2;
        }
        return false;
    }

    public static Vect2d getCursorPos() {
        return new Vect2d(cursorPosX, cursorPosY);
    }

    public static boolean isKeyDown(int pressedKeyNum) {
        return glfwGetKey(Display.getWindow(), pressedKeyNum) == GLFW_PRESS;
    }

    //se registran las funciones que manejan eventos
    public static void registerEvents() {
        glfwSetKeyCallback(Display.getWindow(), keyCallback = new InputHandler.KeyboardCallback());
        glfwSetMouseButtonCallback(Display.getWindow(), mouseButtonCallback = new InputHandler.MouseButtonCallback());
        glfwSetScrollCallback(Display.getWindow(), mouseWheelCallback = new InputHandler.MouseWheelCallback());
        glfwSetCharCallback(Display.getWindow(), charCallback = new InputHandler.CharCallback());
    }

    //se liberan las funciones que manejan eventos, para evitar errores al reiniciar el editor
    public static void releaseEvents(){
        keyCallback.release();
        mouseButtonCallback.release();
        mouseWheelCallback.release();
        charCallback.release();
    }

    public static class MouseWheelCallback extends GLFWScrollCallback {

        @Override
        public void invoke(long window, double unknown, double amount) {
            //DEBUG
//            Log.debug("unknown: "+unknown+", amount: "+amount);
            mouseWheelCallbacks.stream().forEach(i -> i.onWheelMoves(amount));
        }
    }

    public static class MouseButtonCallback extends org.lwjgl.glfw.GLFWMouseButtonCallback {

        @Override
        public void invoke(long window, int button, int action, int mods) {
            //DEBUG
//            Log.debug("button: "+button+", action: "+action);
            if (button == 0) {
                mouseButton0 = action == 1;
            } else if (button == 1) {
                mouseButton1 = action == 1;
            } else if (button == 2) {
                mouseButton2 = action == 1;
            }
            mouseButtonCallbacks.stream().forEach(i -> i.onMouseClick(getCursorPos().toVect2i(), MouseButton.values()[button], action));
        }
    }

    public static class KeyboardCallback extends GLFWKeyCallback {

        @Override
        public void invoke(long window, int keycode, int scancode, int action, int mods) {
            //DEBUG
//            Log.debug("keycode: "+keycode+", scancode: "+scancode+", action: "+action+", mods: "+mods);
            if (action == GLFW_PRESS && keycode == GLFW_KEY_ESCAPE) {
                Display.terminate();
            }
            keyboardCallbacks.stream().forEach(i -> i.onKeyPress(keycode, scancode, action));
        }
    }

    public static class CharCallback extends GLFWCharCallback {

        @Override
        public void invoke(long window, int key) {
            //DEBUG
//            Log.debug("char: " + key);
            textCallbacks.forEach(i -> i.onCharPress(key));
        }
    }

    public interface IMouseWheelCallback {
        void onWheelMoves(double amount);
    }

    public interface IKeyboardCallback {
        void onKeyPress(int key, int code, int action);
    }

    public interface IMouseButtonCallback {
        void onMouseClick(Vect2i pos, InputHandler.MouseButton button, int action);
    }

    public interface ITextCallback {
        void onCharPress(int key);
    }

    public enum MouseButton {
        LEFT, RIGHT, MIDDLE;

        public static MouseButton fromID(int id) {
            switch (id) {
                case 0:
                    return LEFT;
                case 1:
                    return RIGHT;
                case 2:
                    return MIDDLE;
            }
            return null;
        }

        public int getID() {
            switch (this) {
                case LEFT:
                    return 0;
                case RIGHT:
                    return 1;
                case MIDDLE:
                    return 2;
            }
            return 0;
        }
    }
}
