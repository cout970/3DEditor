package com.cout970.editor.gui.api;

import com.cout970.editor.display.InputHandler;
import com.cout970.editor.util.Vect2i;

import java.util.List;

/**
 * Created by cout970 on 26/03/2016.
 */
public interface ICompositeComponent {

    List<IGuiComponent> getComponents();

    default void renderForeground(IGui gui, Vect2i mouse){
        getComponents().forEach(iGuiComponent -> renderForeground(gui, mouse.copy()));
    }

    default void onMouseClick(IGui gui, Vect2i mouse, InputHandler.MouseButton button){
        getComponents().forEach(iGuiComponent -> onMouseClick(gui, mouse.copy(), button));
    }

    default boolean onKeyPressed(IGui gui, int key, int scancode, int action){
        getComponents().forEach(iGuiComponent -> onKeyPressed(gui, key, scancode, action));
        return false;
    }

    default void onCharPress(IGui gui, int key){
        getComponents().forEach(iGuiComponent -> onCharPress(gui, key));
    }

    default void onWheelMoves(IGui gui, double amount){
     getComponents().forEach(iGuiComponent -> onWheelMoves(gui, amount));
    }
}
