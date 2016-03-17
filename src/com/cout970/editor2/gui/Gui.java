package com.cout970.editor2.gui;

import com.cout970.editor2.display.GLFWDisplay;
import com.cout970.editor2.display.Handler2D;
import com.cout970.editor2.display.InputHandler;
import com.cout970.editor2.gui.render.FontRenderer;
import com.cout970.editor2.gui.render.IFontRenderer;
import com.cout970.editor2.gui.render.IGuiRenderer;
import com.cout970.editor2.util.Vect2i;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cout970 on 12/02/2016.
 */
public class Gui implements IGui, InputHandler.IKeyboardCallback, InputHandler.IMouseWheelCallback, InputHandler.IMouseButtonCallback, InputHandler.ITextCallback {

    private List<IGuiComponent> components;
    protected static IGuiRenderer renderer;
    protected Handler2D handler;
    private Vect2i size;

    public Gui() {
        components = new LinkedList<>();
        renderer = IGuiRenderer.INSTANCE;
        handler = GLFWDisplay.handler2D;
        InputHandler.registerKeyboardCallback(this);
        InputHandler.registerMouseWheelCallback(this);
        InputHandler.registerMouseButtonCallback(this);
        InputHandler.registerTextCallback(this);
        onResize();
    }

    public void render() {
        List<IGuiComponent> reverse = new LinkedList<>(components);
        reverse.sort((o1, o2) -> -compare(o1, o2));
        for (IGuiComponent c : reverse) {
            c.renderBackground(this, InputHandler.getCursorPos().toVect2i(), 0);
        }
        for (IGuiComponent c : reverse) {
            c.renderForeground(this, InputHandler.getCursorPos().toVect2i());
        }
    }

    public void onResize() {
        size = GLFWDisplay.getFrameBufferSize();
        components.forEach(i -> i.onResize(this));
    }

    @Override
    public void onKeyPress(int key, int code, int action) {
        if (action == 1) {
            components.forEach(i -> i.onKeyPressed(this, key, code, action));
        }
    }

    @Override
    public void onMouseClick(Vect2i pos, InputHandler.MouseButton b, int action) {
        if (action == GLFW.GLFW_PRESS) {
            boolean done = false;
            for (IGuiComponent c : components) {
                if (c.isMouseOnTop(this, pos.copy(), b)) {
                    c.onMouseClick(this, pos.copy(), b);
                    done = true;
                    c.setLevel(0);
                    int level = 1;
                    for (IGuiComponent c1 : components) {
                        if (c != c1) {
                            c1.setLevel(level);
                            level++;
                        }
                    }
                    Collections.sort(components, this::compare);
                    break;
                }
            }
            if (!done) {
                components.forEach(i -> i.onMouseClick(this, pos.copy(), b));
            }
        }
    }

    @Override
    public void onWheelMoves(double amount) {
        components.forEach(i -> i.onWheelMoves(this, amount));
    }

    @Override
    public List<IGuiComponent> getComponents() {
        return new ArrayList<>(components);
    }

    @Override
    public void addComponent(IGuiComponent comp) {
        comp.setLevel(components.size());
        components.add(comp);
        onResize();
    }

    @Override
    public void removeComponent(IGuiComponent comp) {
        components.remove(comp);
        components.sort(this::compare);
    }

    @Override
    public IFontRenderer getFontRenderer() {
        return FontRenderer.INSTANCE;
    }

    @Override
    public Vect2i getGuiSize() {
        return size.copy();
    }

    @Override
    public Vect2i getGuiStartingPoint() {
        return Vect2i.nullVector();
    }

    @Override
    public IGuiRenderer getGuiRenderer() {
        return renderer;
    }

    @Override
    public boolean isAltKeyPressed() {
        return InputHandler.isKeyDown(GLFW.GLFW_KEY_LEFT_ALT);
    }

    @Override
    public boolean isShiftKeyPressed() {
        return InputHandler.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT);
    }

    @Override
    public boolean isCtrlKeyPressed() {
        return InputHandler.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL);
    }

    @Override
    public boolean isButtonDown(InputHandler.MouseButton button) {
        return InputHandler.isMouseButtonPress(button);
    }

    @Override
    public boolean isKeyPressed(int pressedKeyNum) {
        return InputHandler.isKeyDown(pressedKeyNum);
    }

    @Override
    public void onCharPress(int key) {
        components.forEach(i -> i.onCharPress(this, key));
    }

    public int compare(IGuiComponent a, IGuiComponent b) {
        return a.getLevel() - b.getLevel();
    }

    public boolean blockMouse() {
        if (InputHandler.getCursorPos().getY() < TopBar.BAR_HEIGHT)
            return true;
        for (IGuiComponent c : components) {
            if (c instanceof ISizedComponent) {
                if (IGui.isInside(InputHandler.getCursorPos().toVect2i(), ((ISizedComponent) c).getPos(), ((ISizedComponent) c).getSize())) {
                    return true;
                }
            }
        }
        return false;
    }
}