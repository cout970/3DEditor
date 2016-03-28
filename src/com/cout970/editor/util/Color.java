package com.cout970.editor.util;

import org.lwjgl.opengl.GL11;

public class Color {

    protected int color;

    public Color(float red, float green, float blue) {
        this((int) (red * 0xFF), (int) (green * 0xFF), (int) (blue * 0xFF));
    }

    public Color(int red, int green, int blue) {
        color = ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | (blue & 0xFF);
    }

    public Color(int color) {
        this.color = color;
    }

    public int getRed() {
        return (color >> 16) & 0xFF;
    }

    public int getGreen() {
        return (color >> 8) & 0xFF;
    }

    public int getBlue() {
        return color & 0xFF;
    }


    public int toInt() {
        return color;
    }

    public int toIntWithAlpha() {
        return color | 0xFF000000;
    }

    public String toString() {
        return "[0x" + Integer.toHexString(getRed()) + Integer.toHexString(getGreen()) + Integer.toHexString(getBlue()) + ", red: " + getRed() + ", green: " + getGreen() + ", blue: " + getBlue() + "]";
    }

    public void glColor() {
        GL11.glColor3f(getRed() / 255f, getGreen() / 255f, getBlue() / 255f);
    }

    public float getR() {
        return getRed() / 255f;
    }

    public float getG() {
        return getGreen() / 255f;
    }

    public float getB() {
        return getBlue() / 255f;
    }
}
