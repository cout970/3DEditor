package com.cout970.editor2.util;

public class LoopTimer {

    private static final long SECOND = 1000000000L;
    private static final long TICK = 50000000L;
    private long lastSecond;
    private long lastTick;

    private int fpsCount;
    private int tickCount;

    private int lastFpsCount;
    private int lastTickCount;

    private boolean tickCicle;

    public LoopTimer() {
        lastSecond = System.nanoTime();
        lastTick = System.nanoTime();
    }

    public void loopTick() {
        long time = System.nanoTime();

        tickCicle = false;

        if (time - lastSecond >= SECOND) {

            lastFpsCount = fpsCount;
            fpsCount = 0;

            lastTickCount = tickCount;
            tickCount = 0;

            lastSecond = time;
        }
        if (time - lastTick >= TICK) {
            lastTick = time;
            tickCount++;
            tickCicle = true;
        }

        fpsCount++;
    }

    public int getFPS() {
        return lastFpsCount;
    }

    public int getTPS() {
        return lastTickCount;
    }

    public boolean isTickCicle() {
        return tickCicle;
    }

    public float tickDelay() {
        return Math.min(1, (System.nanoTime() - lastTick) / (float) TICK);
    }
}
