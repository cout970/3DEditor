package com.cout970.editor.gui.api;

import com.cout970.editor.display.InputHandler;
import com.cout970.editor.util.Vect2i;

/**
 * Created by cout970 on 13/02/2016.
 */
public interface ISizedComponent extends IGuiComponent {

    Vect2i getPos();

    Vect2i getSize();

    @Override
    default boolean isMouseOnTop(IGui gui, Vect2i mouse, InputHandler.MouseButton button) {
        return IGui.isInside(mouse, getPos(), getSize());
    }
}
