package com.cout970.editor.util;

import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class LoopTimer {

    private double lastSecond;
    private int fpsCount;
    private int lastFpsCount;
    private double delta;
    private double time;

    public LoopTimer() {
        lastSecond = GLFW.glfwGetTime();
    }

    public void loopTick() {
        delta = glfwGetTime() - time;
        time = glfwGetTime();

        fpsCount++;
        if (time - lastSecond >= 1) {
            lastFpsCount = fpsCount;
            fpsCount = 0;
            lastSecond = time;
        }
    }

    public int getFPS() {
        return lastFpsCount;
    }

    public double getDelta() {
        return delta;
    }
}
