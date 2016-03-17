package com.cout970.editor2.render.engine;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;

public class RenderEngineVBO implements IRenderEngine {

    private DisplayList tempList;
    private int defaultCapacity = 0x20000;
    private FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(defaultCapacity);
    private ByteBuffer colorBuffer = BufferUtils.createByteBuffer(defaultCapacity);
    private FloatBuffer textureBuffer = BufferUtils.createFloatBuffer(defaultCapacity);
    private FloatBuffer normalsBuffer = BufferUtils.createFloatBuffer(defaultCapacity);

    private boolean drawing, useTexture, useColor, useNormal;
    private int drawMode, vertex;

    @Override
    public void startDrawing(int type) {
        if (drawing) {
            throw new RuntimeException("The Render Engine it's already drawing other thing!");
        }
        drawing = true;
        reset();
        drawMode = type;
        useColor = false;
        useTexture = false;
        useNormal = false;
        if (tempList == null)
            tempList = new DisplayList();
    }

    private void reset() {
        vertex = 0;
        vertexBuffer.clear();
        colorBuffer.clear();
        textureBuffer.clear();
        normalsBuffer.clear();
    }

    @Override
    public void endDrawing() {
        if (!drawing) {
            throw new RuntimeException(
                    "Someone try to call endDrawing before calling the method startDrawing");
        }
        drawing = false;

        if (useColor) {
            tempList.colorList = GL15.glGenBuffers();
            colorBuffer.flip();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, tempList.colorList);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, colorBuffer, GL15.GL_STATIC_DRAW);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        }

        if (useTexture) {
            tempList.textureList = GL15.glGenBuffers();
            textureBuffer.flip();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, tempList.textureList);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textureBuffer, GL15.GL_STATIC_DRAW);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        }

        if (useNormal) {
            tempList.normalList = GL15.glGenBuffers();
            normalsBuffer.flip();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, tempList.normalList);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normalsBuffer, GL15.GL_STATIC_DRAW);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        }

        tempList.vertexlist = GL15.glGenBuffers();
        vertexBuffer.flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, tempList.vertexlist);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        tempList.vertex = vertex;
        tempList.drawMode = drawMode;
        reset();
    }

    @Override
    public void addVertex(double x, double y, double z) {
        vertexBuffer.put(new float[]{(float) x, (float) y, (float) z});
        vertex++;
    }

    @Override
    public void addTextureUV(double u, double v) {
        useTexture = true;
        textureBuffer.put(new float[]{(float) u, (float) v});
    }

    @Override
    public void translate(double x, double y, double z) {
        GL11.glTranslated(x, y, z);
    }

    @Override
    public void rotate(double angle, double x, double y, double z) {
        GL11.glRotatef((float) angle, (float) x, (float) y, (float) z);
    }

    @Override
    public void scale(double x, double y, double z) {
        GL11.glScalef((float) x, (float) y, (float) z);
    }

    @Override
    public void setColor(int rgb, float alpha) {
        useColor = true;
        colorBuffer.put(
                new byte[]{(byte) ((rgb >> 16) & 0xFF), (byte) ((rgb >> 8) & 0xFF), (byte) (rgb & 0xFF), (byte) (alpha * 0xFF)});
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
    public void addNormal(double x, double y, double z) {
        useNormal = true;
        normalsBuffer.put(new float[]{(float) x, (float) y, (float) z});
    }

    @Override
    public void startCompile(DisplayList list) {
        if (tempList != null) {
            glDeleteBuffers(tempList.vertexlist);
            if (useColor)
                glDeleteBuffers(tempList.colorList);
            if (useTexture)
                glDeleteBuffers(tempList.textureList);
            if (useNormal)
                glDeleteBuffers(tempList.normalList);
        }
        tempList = list;
    }

    @Override
    public void endCompile() {
        tempList = null;
    }

    @Override
    public void render(DisplayList list) {

        if (list.colorList != -1) {
            GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, list.colorList);
            GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, 0, 0L);
        }

        if (list.textureList != -1) {
            GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, list.textureList);
            GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0L);
        }

        if (list.normalList != -1) {
            GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, list.normalList);
            GL11.glNormalPointer(GL11.GL_FLOAT, 0, 0L);
        }

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, list.vertexlist);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0L);

        GL11.glDrawArrays(list.drawMode, 0, list.vertex);

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);

        if (list.colorList != -1) {
            GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        }

        if (list.textureList != -1) {
            GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        }

        if (list.normalList != -1) {
            GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
        }
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
