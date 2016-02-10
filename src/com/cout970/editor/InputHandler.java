package com.cout970.editor;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;

/**
 * Created by cout970 on 10/02/2016.
 */
public class InputHandler {

    private static double cursorDifX;
    private static double cursorDifY;

    private static boolean mouseButton0;
    private static boolean mouseButton1;
    private static boolean mouseButton2;

    private static double cursorPosXold;
    private static double cursorPosYold;


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
        switch (button){
            case LEFT:
                return mouseButton0;
            case RIGHT:
                return mouseButton1;
            case MIDDLE:
                return mouseButton2;
        }
        return false;
    }

    public static class GLFWMouseCallback extends GLFWMouseButtonCallback {

        @Override
        public void invoke(long window, int button, int action, int mods) {
//            Log.debug("button: "+button+", action: "+action);
            if (button == 0){
                mouseButton0 = action == 1;
            }else if(button == 1){
                mouseButton1 = action == 1;
            }else if(button == 2){
                mouseButton2 = action == 1;
            }
        }
    }

    public static class GLFWKeyboardCallback extends GLFWKeyCallback {

        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
//            Log.debug("key: "+key+", keycode: "+scancode+", action: "+action);
            if (action == 1) {
                if (scancode == 1) {
                    GLFWDisplay.terminate();
                }
            }
        }
    }

    public enum MouseButton {
        LEFT, RIGHT, MIDDLE;
    }
}
