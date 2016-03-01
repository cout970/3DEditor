package com.cout970.editor.render.engine;

import org.lwjgl.opengl.GL11;

public class RenderEngineGL11 implements IRenderEngine {

    @Override
    public void startDrawing(int type) {
        GL11.glBegin(type);
    }

    @Override
    public void endDrawing() {
        GL11.glEnd();
    }

    @Override
    public void addVertex(double x, double y, double z) {
        GL11.glVertex3d(x, y, z);
    }

    @Override
    public void addTextureUV(double u, double v) {
        GL11.glTexCoord2d(u, v);
    }

    @Override
    public void translate(double x, double y, double z) {
        GL11.glTranslated(x, y, z);
    }

    @Override
    public void rotate(double angle, double x, double y, double z) {
        GL11.glRotated(angle, x, y, z);
    }

    @Override
    public void scale(double x, double y, double z) {
        GL11.glScaled(x, y, z);
    }

    @Override
    public void loadIdentity() {
        GL11.glLoadIdentity();
    }

    @Override
    public void pushMatrix() {
        GL11.glPushMatrix();
    }

    @Override
    public void popMatrix() {
        GL11.glPopMatrix();
    }

    @Override
    public void enable(int i) {
        GL11.glEnable(i);
    }

    @Override
    public void disable(int i) {
        GL11.glDisable(i);
    }

    @Override
    public void enableBlend() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void setColor(int rgb, float alpha) {
        GL11.glColor4f((rgb & 0xFF) / 255f, ((rgb >> 8) & 0xFF) / 255f, ((rgb >> 16) & 0xFF) / 255f, alpha);
    }

    @Override
    public void addNormal(double x, double y, double z) {
        GL11.glNormal3d(x, y, z);
    }

    @Override
    public void startCompile(DisplayList list) {
        if (list.vertexlist != -1) {
            GL11.glDeleteLists(list.vertexlist, 1);
        }
        list.vertexlist = GL11.glGenLists(1);
        GL11.glNewList(list.vertexlist, GL11.GL_COMPILE);
    }

    @Override
    public void endCompile() {
        GL11.glEndList();
    }

    @Override
    public void render(DisplayList list) {
        GL11.glCallList(list.vertexlist);
    }

    @Override
    public void setColor(float r, float g, float b, float alpha) {
        int rgb = (((int) (r * 255) & 0xFF) << 16) | (((int) (g * 255) & 0xFF) << 8) | ((int) (b * 255) & 0xFF);
        setColor(rgb, alpha);
    }

    @Override
    public void setColorOpaque(int rgb) {
        setColor(rgb, 1f);
    }

    @Override
    public void addLight(double f) {
    }

    @Override
    public void enableLight() {
    }

    @Override
    public void disableLight() {
    }
}
