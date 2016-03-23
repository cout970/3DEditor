package com.cout970.editor.gui;

import com.cout970.editor.display.InputHandler;
import com.cout970.editor.util.Vect2i;

/**
 * Created by cout970 on 24/12/2015.
 */
public interface IGuiComponent {

    void renderBackground(IGui gui, Vect2i mouse, float partialTicks);

    void renderForeground(IGui gui, Vect2i mouse);

    void onMouseClick(IGui gui, Vect2i mouse, InputHandler.MouseButton button);

    boolean onKeyPressed(IGui gui, int key, int scancode, int action);

    void onCharPress(IGui gui, int key);

    void onWheelMoves(IGui gui, double amount);

    int getLevel();

    void setLevel(int level);

    default boolean isMouseOnTop(IGui gui, Vect2i mouse, InputHandler.MouseButton button) {
        return false;
    }
}
