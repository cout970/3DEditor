package com.cout970.editor.gui.event;

import com.cout970.editor.display.InputHandler;
import com.cout970.editor.util.Vect2i;

/**
 * Created by cout970 on 05/04/2016.
 */
public class MouseClickEvent implements IGuiEvent {

    private InputHandler.MouseButton button;
    private Vect2i mousePos;

    public MouseClickEvent(InputHandler.MouseButton button, Vect2i mousePos) {
        this.button = button;
        this.mousePos = mousePos;
    }

    public InputHandler.MouseButton getButton() {
        return button;
    }

    public Vect2i getMousePos() {
        return mousePos.copy();
    }
}
