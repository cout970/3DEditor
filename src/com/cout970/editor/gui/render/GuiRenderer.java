package com.cout970.editor.gui.render;

import com.cout970.editor.render.engine.IRenderEngine;
import com.cout970.editor.render.texture.TextureStorage;
import com.cout970.editor.util.Color;
import com.cout970.editor.util.Vect2d;
import com.cout970.editor.util.Vect2i;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_QUADS;

/**
 * Created by cout970 on 11/02/2016.
 */
public class GuiRenderer implements IGuiRenderer {


    @Override
    public IFontRenderer getFontRenderer() {
        return IFontRenderer.INSTANCE;
    }

    @Override
    public void drawHoveringText(List<String> textLines, Vect2i pos) {

    }

    @Override
    public void drawHorizontalLine(int startX, int endX, int y, Color color) {

    }

    @Override
    public void drawVerticalLine(int x, int startY, int endY, Color color) {

    }

    @Override
    public void drawRectangle(Vect2i start, Vect2i end, Color x) {
        IRenderEngine engine = IRenderEngine.INSTANCE;
        TextureStorage.EMPTY.bind();
        engine.startDrawing(GL_QUADS);
        engine.setColorOpaque(x.toInt());
        engine.addVertex(start.getX(), start.getY(), 0.0D);
        engine.addVertex(start.getX(), end.getY(), 0.0D);
        engine.addVertex(end.getX(), end.getY(), 0.0D);
        engine.addVertex(end.getX(), start.getY(), 0.0D);
        engine.endDrawing();
    }

    @Override
    public void drawGradientRectangle(Vect2i start, Vect2i end, Color startColor, Color endColor) {
        IRenderEngine engine = IRenderEngine.INSTANCE;
        TextureStorage.EMPTY.bind();
        engine.startDrawing(GL_QUADS);
        engine.setColorOpaque(startColor.toInt());
        engine.addVertex(start.getX(), start.getY(), 0.0D);
        engine.addVertex(start.getX(), end.getY(), 0.0D);
        engine.setColorOpaque(endColor.toInt());
        engine.addVertex(end.getX(), end.getY(), 0.0D);
        engine.addVertex(end.getX(), start.getY(), 0.0D);
        engine.endDrawing();
    }

    @Override
    public void drawCenteredString(String text, Vect2i pos, Color color) {

    }

    @Override
    public void drawString(String text, Vect2i pos, Color color) {

    }

    @Override
    public void drawTexturedRectangle(Vect2i pos, Vect2i texturePos, Vect2i size) {

    }

    @Override
    public void drawRectangleWithCustomSizedTexture(Vect2i pos, Vect2i size, Vect2d textureUV, Vect2d textureSize) {

    }

    @Override
    public void drawScaledCustomSizeRectangle(Vect2i pos, Vect2i size, Vect2d textureUV, Vect2i textureSize, Vect2d tileSize) {

    }
}
