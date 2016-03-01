package com.cout970.editor;

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

    private static List<IMouseWheelCallback> mouseWheelCallbacks = new ArrayList<>();
    private static List<IKeyboardCallback> keyboardCallbacks = new ArrayList<>();
    private static List<IMouseButtonCallback> mouseButtonCallbacks = new ArrayList<>();
    private static List<ITextCallback> textCallbacks = new ArrayList<>();

    private static double cursorDifX;
    private static double cursorDifY;

    private static double cursorPosX;
    private static double cursorPosY;

    private static boolean mouseButton0;
    private static boolean mouseButton1;
    private static boolean mouseButton2;

    private static double cursorPosXold;
    private static double cursorPosYold;

    private static boolean mayusLock;

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

    public static void updateCursor(boolean blocked) {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

        GLFW.glfwGetCursorPos(GLFWDisplay.getWindow(), x, y);
        x.rewind();
        y.rewind();
        cursorDifX = cursorPosXold - x.get();
        cursorDifY = cursorPosYold - y.get();
        x.rewind();
        y.rewind();
        cursorPosXold = x.get();
        cursorPosYold = y.get();
        if (!blocked) {
            glfwSetCursorPos(GLFWDisplay.getWindow(), GLFWDisplay.getWindowWidth() / 2, GLFWDisplay.getWindowHeight() / 2);
        }
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
        return glfwGetKey(GLFWDisplay.getWindow(), pressedKeyNum) == GLFW_PRESS;
    }


    public static class MousePosCallback extends GLFWCursorPosCallback {

        @Override
        public void invoke(long window, double x, double y) {
            cursorPosX = x;
            cursorPosY = y;
        }
    }

    public static class MouseWheelCallback extends GLFWScrollCallback {

        @Override
        public void invoke(long window, double unknown, double amount) {
//            Log.debug("unknown: "+unknown+", amount: "+amount);
            mouseWheelCallbacks.stream().forEach(i -> i.onWheelMoves(amount));
        }
    }


    public static class MouseButtonCallback extends org.lwjgl.glfw.GLFWMouseButtonCallback {

        @Override
        public void invoke(long window, int button, int action, int mods) {
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
//            Log.debug("keycode: "+keycode+", scancode: "+scancode+", action: "+action+", mods: "+mods);
            if (action == GLFW_PRESS && keycode == GLFW_KEY_ESCAPE) {
                GLFWDisplay.terminate();
            }
            keyboardCallbacks.stream().forEach(i -> i.onKeyPress(keycode, scancode, action));
        }
    }

    public static class CharCallback extends GLFWCharCallback {

        @Override
        public void invoke(long window, int key) {
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
