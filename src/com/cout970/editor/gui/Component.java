package com.cout970.editor.gui;

import com.cout970.editor.GLFWDisplay;
import com.cout970.editor.Handler2D;
import com.cout970.editor.InputHandler;
import com.cout970.editor.gui.render.IGuiRenderer;
import com.cout970.editor.util.Vect2d;
import com.cout970.editor.util.Vect2i;

/**
 * Created by cout970 on 11/02/2016.
 */
public abstract class Component implements InputHandler.IKeyboardCallback, InputHandler.IMouseWheelCallback, InputHandler.IMouseButtonCallback{

    protected static IGuiRenderer renderer;
    protected Vect2d pos;
    protected Vect2d size;
    protected Handler2D handler;

    public Component(Vect2d pos, Vect2d size) {
        this.pos = pos;
        this.size = size;
        renderer = IGuiRenderer.INSTANCE;
        handler = GLFWDisplay.handler2D;
        InputHandler.registerKeyboardCallback(this);
        InputHandler.registerMouseWheelCallback(this);
        InputHandler.registerMouseButtonCallback(this);
    }

    public abstract void updateAndRender();

    public Vect2d getPos() {
        return pos;
    }

    public Vect2i getSize() {
        return sizeToScreen(size);
    }

    protected Vect2i sizeToScreen(Vect2d size){
        return handler.getScreenSize().toVect2d().multiply(size.copy().multiply(1 / 100d)).toVect2i();
    }

    public static boolean isInside(Vect2i mouse, Vect2i pos, Vect2i size) {
        return isInside(mouse.getX(), mouse.getY(), pos.getX(), pos.getY(), size.getX(), size.getY());
    }

    public static boolean isInside(int mx, int my, int x, int y, int w, int h) {
        if (mx > x && mx < x + w) {
            if (my > y && my < y + h) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onKeyPress(int key, int action){}

    @Override
    public void onMouseClick(Vect2i pos, InputHandler.MouseButton b){}

    @Override
    public void onWheelMoves(double amount) {}
}
