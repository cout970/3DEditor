package com.cout970.editor.gui.render;

import com.cout970.editor.util.Color;
import com.cout970.editor.util.Vect2d;
import com.cout970.editor.util.Vect2i;

import java.util.List;

/**
 * Created by cout970 on 25/12/2015.
 */
public interface IGuiRenderer {

    IGuiRenderer INSTANCE = new GuiRenderer();

    IFontRenderer getFontRenderer();

    void drawHoveringText(List<String> textLines, Vect2i pos);

    void drawHorizontalLine(int startX, int endX, int y, Color color);

    void drawVerticalLine(int x, int startY, int endY, Color color);

    void drawRectangle(Vect2i start, Vect2i end, Color color);

    void drawGradientRectangle(Vect2i start, Vect2i end, Color startColor, Color endColor);

    void drawCenteredString(String text, Vect2i pos, Color color);

    void drawString(String text, Vect2i pos, Color color);

    void drawTexturedRectangle(Vect2i pos, Vect2i texturePos, Vect2i size);

    void drawRectangleWithCustomSizedTexture(Vect2i pos, Vect2i size, Vect2d textureUV, Vect2d textureSize);

    void drawScaledCustomSizeRectangle(Vect2i pos, Vect2i size, Vect2d textureUV, Vect2i textureSize, Vect2d tileSize);
}
