package com.cout970.editor.gui.render;

import com.cout970.editor.render.engine.IRenderEngine;
import com.cout970.editor.render.texture.TextureStorage;
import com.cout970.editor.util.Vect2d;
import com.cout970.editor.util.Vect2i;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by cout970 on 11/02/2016.
 */
public class FontRenderer implements IFontRenderer {

    public static final FontRenderer INSTANCE = new FontRenderer();

    private FontRenderer() {
    }

    @Override
    public int drawStringWithShadow(String text, float x, float y, int color) {
        print(x+1, y+1, 0, text);
        print(x, y, color, text);
        return 0;
    }

    @Override
    public int drawString(String text, int x, int y, int color) {
        print(x, y, color, text);
        return 0;
    }

    @Override
    public int getStringWidth(String text) {
        return text.length()*8;
    }

    @Override
    public int getCharWidth(char character) {
        return 8;
    }

    @Override
    public String trimStringToWidth(String text, int width) {
        throw new UnsupportedOperationException();
    }


    private void print(float x, float y, int color, String text) {
        IRenderEngine eng = IRenderEngine.INSTANCE;
        TextureStorage.FONT.bind();
        glColor3f((color & 0xFF) / 255f, ((color >> 8) & 0xFF) / 255f, ((color >> 16) & 0xFF) / 255f);
        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);
            GuiRenderer.INSTANCE.drawRectangleWithCustomSizedTexture(new Vect2i(x + i * 8, y), new Vect2i(8, 8), new Vect2d((character & 15) * 8, (character >> 4) * 8), new Vect2d(128, 128));
        }
        glColor4f(1f, 1f, 1f, 1f);
    }
}
