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
        return FontRenderer.INSTANCE;
    }

    @Override
    public void drawHoveringText(List<String> textLines, Vect2i pos) {

    }

    @Override
    public void drawHorizontalLine(int startX, int endX, int y, Color color) {
        if (endX < startX) {
            int i = startX;
            startX = endX;
            endX = i;
        }

        drawRectangle(new Vect2i(startX, y), new Vect2i(endX + 1, y + 1), color);
    }

    @Override
    public void drawVerticalLine(int x, int startY, int endY, Color color) {
        if (endY < startY) {
            int i = startY;
            startY = endY;
            endY = i;
        }

        drawRectangle(new Vect2i(x, startY + 1), new Vect2i(x + 1, endY), color);
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
        int desp = getFontRenderer().getStringWidth(text) / 2;
        drawString(text, pos.copy().sub(desp, 0), color);
    }

    @Override
    public void drawString(String text, Vect2i pos, Color color) {
        getFontRenderer().drawString(text, pos.getX(), pos.getY(), color.toInt());
    }

    @Override
    public void drawTexturedRectangle(Vect2i pos, Vect2i texturePos, Vect2i size) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        int zLevel = 0;
        IRenderEngine engine = IRenderEngine.INSTANCE;
        engine.startDrawing(GL_QUADS);
        engine.addTextureUV(((texturePos.getX()) * f), (double) ((texturePos.getY() + size.getY()) * f1));
        engine.addVertex((pos.getX()), (pos.getY() + size.getY()), zLevel);

        engine.addTextureUV(((texturePos.getX() + size.getX()) * f), (double) ((texturePos.getY() + size.getY()) * f1));
        engine.addVertex((pos.getX() + size.getX()), (pos.getY() + size.getY()), zLevel);

        engine.addTextureUV(((texturePos.getX() + size.getX()) * f), (double) ((texturePos.getY()) * f1));
        engine.addVertex((pos.getX() + size.getX()), (pos.getY()), zLevel);

        engine.addTextureUV(((texturePos.getX()) * f), (double) ((texturePos.getY()) * f1));
        engine.addVertex((pos.getX()), (pos.getY()), zLevel);
        engine.endDrawing();
    }

    @Override
    public void drawRectangleWithCustomSizedTexture(Vect2i pos, Vect2i size, Vect2d textureUV, Vect2d textureSize) {
        float f = (float) (1.0F / textureSize.getX());
        float f1 = (float) (1.0F / textureSize.getY());
        IRenderEngine engine = IRenderEngine.INSTANCE;
        engine.startDrawing(GL_QUADS);
        engine.addTextureUV((textureUV.getX() * f), ((textureUV.getY() + size.getY()) * f1));
        engine.addVertex(pos.getX(), pos.getY() + size.getY(), 0.0D);

        engine.addTextureUV(((textureUV.getX() + size.getX()) * f), ((textureUV.getY() + size.getY()) * f1));
        engine.addVertex(pos.getX() + size.getX(), pos.getY() + size.getY(), 0.0D);

        engine.addTextureUV(((textureUV.getX() + size.getX()) * f), (textureUV.getY() * f1));
        engine.addVertex(pos.getX() + size.getX(), pos.getY(), 0.0D);

        engine.addTextureUV((textureUV.getX() * f), (textureUV.getY() * f1));
        engine.addVertex(pos.getX(), pos.getY(), 0.0D);
        engine.endDrawing();
    }

    @Override
    public void drawScaledCustomSizeRectangle(Vect2i pos, Vect2i size, Vect2d textureUV, Vect2i textureSize, Vect2d tileSize) {

    }
}
