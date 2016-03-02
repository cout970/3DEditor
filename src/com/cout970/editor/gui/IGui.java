package com.cout970.editor.gui;

import com.cout970.editor.display.InputHandler;
import com.cout970.editor.gui.render.IFontRenderer;
import com.cout970.editor.gui.render.IGuiRenderer;
import com.cout970.editor.util.Vect2i;

import java.util.List;

/**
 * Created by cout970 on 24/12/2015.
 */
public interface IGui {

    void render();

    List<IGuiComponent> getComponents();

    void addComponent(IGuiComponent comp);

    void removeComponent(IGuiComponent comp);

    IFontRenderer getFontRenderer();

    Vect2i getGuiSize();

    Vect2i getGuiStartingPoint();

    IGuiRenderer getGuiRenderer();

    boolean isAltKeyPressed();

    boolean isShiftKeyPressed();

    boolean isCtrlKeyPressed();

    boolean isButtonDown(InputHandler.MouseButton button);

    boolean isKeyPressed(int pressedKeyNum);

    static boolean isInside(int mx, int my, int x, int y, int w, int h) {
        if (mx > x && mx < x + w) {
            if (my > y && my < y + h) {
                return true;
            }
        }
        return false;
    }

    static boolean isInside(Vect2i mouse, Vect2i pos, Vect2i size) {
        return isInside(mouse.getX(), mouse.getY(), pos.getX(), pos.getY(), size.getX(), size.getY());
    }
}
