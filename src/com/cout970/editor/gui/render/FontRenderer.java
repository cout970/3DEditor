package com.cout970.editor.gui.render;

/**
 * Created by cout970 on 11/02/2016.
 */
public class FontRenderer implements IFontRenderer {


    @Override
    public int drawStringWithShadow(String text, float x, float y, int color) {
        return 0;
    }

    @Override
    public int drawString(String text, int x, int y, int color) {
        return 0;
    }

    @Override
    public int getStringWidth(String text) {
        return 0;
    }

    @Override
    public int getCharWidth(char character) {
        return 0;
    }

    @Override
    public String trimStringToWidth(String text, int width) {
        return null;
    }
}
