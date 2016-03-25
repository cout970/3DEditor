package com.cout970.editor.gui.components;


import com.cout970.editor.display.InputHandler;
import com.cout970.editor.gui.IGui;
import com.cout970.editor.gui.IGuiComponent;
import com.cout970.editor.render.IGuiRenderer;
import com.cout970.editor.util.Vect2i;

/**
 * Created by cout970 on 24/01/2016.
 */
public abstract class AbstractButton implements IGuiComponent {

    protected Vect2i pos;
    protected Vect2i size;
    protected int id;
    protected ButtonListener listener;

    public AbstractButton(Vect2i pos, Vect2i size, ButtonListener listener) {
        this.pos = pos;
        this.size = size;
        this.listener = listener;
    }

    @Override
    public void renderBackground(IGui gui, Vect2i mouse, float partialTicks) {
        renderButton(gui, gui.getGuiRenderer(), mouse);
    }

    @Override
    public void onMouseClick(IGui gui, Vect2i mouse, InputHandler.MouseButton button) {
        if (IGui.isInside(mouse, getPos(gui), size)) {
            listener.onPress(this, mouse, button);
        }
    }

    @Override
    public boolean onKeyPressed(IGui gui, int key, int scancode, int action) {
        return false;
    }

    protected Vect2i getPos(IGui gui) {
        return pos.copy();
    }

    protected abstract void renderButton(IGui gui, IGuiRenderer guiRenderer, Vect2i mouse);

    public int getId() {
        return id;
    }

    public AbstractButton setId(int id) {
        this.id = id;
        return this;
    }

    public interface ButtonListener {
        boolean onPress(AbstractButton button, Vect2i mouse, InputHandler.MouseButton mouseButton);
    }

    @Override
    public void onWheelMoves(IGui gui, double amount) {

    }

    @Override
    public void onCharPress(IGui gui, int key) {
    }


    public void setPos(Vect2i pos) {
        this.pos = pos;
    }
}
