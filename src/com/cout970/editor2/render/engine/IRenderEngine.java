package com.cout970.editor2.render.engine;

public interface IRenderEngine {

    IRenderEngine INSTANCE = new RenderEngineVAO();

    void startDrawing(int type);

    void endDrawing();

    void addVertex(double x, double y, double z);

    void addTextureUV(double u, double v);

    void addNormal(double x, double y, double z);

    void addLight(double f);

    void translate(double x, double y, double z);

    void rotate(double angle, double x, double y, double z);

    void scale(double x, double y, double z);

    void setColor(int rgb, float alpha);

    void setColor(float r, float g, float b, float alpha);

    void setColorOpaque(int rgb);

    void loadIdentity();

    void pushMatrix();

    void popMatrix();

    void enable(int i);

    void disable(int i);

    void enableBlend();

    void enableLight();

    void disableLight();

    void startCompile(DisplayList list);

    void endCompile();

    void render(DisplayList list);

}
