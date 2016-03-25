package com.cout970.editor.render;

import com.cout970.editor.render.texture.TextureStorage;
import com.cout970.editor.util.Color;
import com.cout970.editor.util.Vect2d;
import com.cout970.editor.util.Vect2i;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

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

        TextureStorage.EMPTY.bind();
        glBegin(GL_QUADS);
        x.glColor();
        glVertex3d(start.getX(), start.getY(), 0.0D);
        glVertex3d(start.getX(), end.getY(), 0.0D);
        glVertex3d(end.getX(), end.getY(), 0.0D);
        glVertex3d(end.getX(), start.getY(), 0.0D);
        glEnd();
    }

    @Override
    public void drawGradientRectangle(Vect2i start, Vect2i end, Color startColor, Color endColor) {

        TextureStorage.EMPTY.bind();
        glBegin(GL_QUADS);
        startColor.glColor();
        glVertex3d(start.getX(), start.getY(), 0.0D);
        glVertex3d(start.getX(), end.getY(), 0.0D);
        endColor.glColor();
        glVertex3d(end.getX(), end.getY(), 0.0D);
        glVertex3d(end.getX(), start.getY(), 0.0D);
        glEnd();
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

        glBegin(GL_QUADS);
        glTexCoord2d(((texturePos.getX()) * f), (double) ((texturePos.getY() + size.getY()) * f1));
        glVertex3d((pos.getX()), (pos.getY() + size.getY()), zLevel);

        glTexCoord2d(((texturePos.getX() + size.getX()) * f), (double) ((texturePos.getY() + size.getY()) * f1));
        glVertex3d((pos.getX() + size.getX()), (pos.getY() + size.getY()), zLevel);

        glTexCoord2d(((texturePos.getX() + size.getX()) * f), (double) ((texturePos.getY()) * f1));
        glVertex3d((pos.getX() + size.getX()), (pos.getY()), zLevel);

        glTexCoord2d(((texturePos.getX()) * f), (double) ((texturePos.getY()) * f1));
        glVertex3d((pos.getX()), (pos.getY()), zLevel);
        glEnd();
    }

    @Override
    public void drawRectangleWithCustomSizedTexture(Vect2i pos, Vect2i size, Vect2d textureUV, Vect2d textureSize) {
        float f = (float) (1.0F / textureSize.getX());
        float f1 = (float) (1.0F / textureSize.getY());

        glBegin(GL_QUADS);
        glTexCoord2d((textureUV.getX() * f), ((textureUV.getY() + size.getY()) * f1));
        glVertex3d(pos.getX(), pos.getY() + size.getY(), 0.0D);

        glTexCoord2d(((textureUV.getX() + size.getX()) * f), ((textureUV.getY() + size.getY()) * f1));
        glVertex3d(pos.getX() + size.getX(), pos.getY() + size.getY(), 0.0D);

        glTexCoord2d(((textureUV.getX() + size.getX()) * f), (textureUV.getY() * f1));
        glVertex3d(pos.getX() + size.getX(), pos.getY(), 0.0D);

        glTexCoord2d((textureUV.getX() * f), (textureUV.getY() * f1));
        glVertex3d(pos.getX(), pos.getY(), 0.0D);
        glEnd();
    }

    @Override
    public void drawScaledCustomSizeRectangle(Vect2i pos, Vect2i size, Vect2d textureUV, Vect2i textureSize, Vect2d tileSize) {

    }

    @Override
    public void drawRectangleWithTextureUV(Vect2i pos, Vect2i size, Vect2d first, Vect2d end) {
        glBegin(GL_QUADS);

        glTexCoord2d(first.getX(), end.getY());
        glVertex3d(pos.getX(), pos.getY() + size.getY(), 0.0D);

        glTexCoord2d(end.getX(), end.getY());
        glVertex3d(pos.getX() + size.getX(), pos.getY() + size.getY(), 0.0D);

        glTexCoord2d(end.getX(), first.getY());
        glVertex3d(pos.getX() + size.getX(), pos.getY(), 0.0D);

        glTexCoord2d(first.getX(), first.getY());
        glVertex3d(pos.getX(), pos.getY(), 0.0D);

        glEnd();
    }

    @Override
    public void enableBlend() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void disableBlend() {
        glDisable(GL_BLEND);
    }
}
