package com.cout970.editor.gui.windows;

import com.cout970.editor.display.InputHandler;
import com.cout970.editor.gui.IGui;
import com.cout970.editor.gui.IGuiComponent;
import com.cout970.editor.gui.ISizedComponent;
import com.cout970.editor.gui.TopBar;
import com.cout970.editor.gui.render.IGuiRenderer;
import com.cout970.editor.util.Color;
import com.cout970.editor.util.Vect2i;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by cout970 on 12/02/2016.
 */
public class InternalWindow implements ISizedComponent {

    protected IGui gui;
    protected Vect2i size;
    protected Vect2i pos;
    private boolean moving;
    private Vect2i clickPoint;
    protected boolean hide;
    protected int level;
    protected List<IGuiComponent> subParts;

    public InternalWindow(Vect2i size) {
        this.size = size;
        subParts = new LinkedList<>();
        setPos(Vect2i.nullVector());
    }

    public Vect2i getSize() {
        return size.copy();
    }

    public void setSize(Vect2i size) {
        this.size = size;
    }

    public Vect2i getPos() {
        return pos.copy();
    }

    public void setPos(Vect2i pos) {
        this.pos = pos;
        if (pos.getX() < 0) {
            pos.setX(0);
        }
        if (pos.getY() < TopBar.BAR_HEIGHT) {
            pos.setY(TopBar.BAR_HEIGHT);
        }
        if (gui == null) return;
        if (pos.getX() + size.getX() > gui.getGuiSize().getX()) {
            pos.setX(gui.getGuiSize().getX() - size.getX());
        }
        if (pos.getY() + size.getY() > gui.getGuiSize().getY()) {
            pos.setY(gui.getGuiSize().getY() - size.getY());
        }
    }

    @Override
    public void onResize(IGui gui) {
        this.gui = gui;
    }

    @Override
    public void renderBackground(IGui gui, Vect2i mouse, float partialTicks) {

        if (moving) {
            if (!gui.isButtonDown(InputHandler.MouseButton.LEFT)) {
                moving = false;
            } else {
                setPos(mouse.copy().sub(clickPoint));
            }
        }
        if (!hide) {
            IGuiRenderer rend = gui.getGuiRenderer();
            Vect2i margin = new Vect2i(2, 2);
            rend.drawRectangle(getPos(), getPos().add(getSize()), new Color(0x666666));
            rend.drawRectangle(getPos().add(margin), getPos().add(getSize()).sub(margin), new Color(0xFFFFFF));
            rend.drawRectangle(getPos().add(margin), getPos().add(getSize().sub(margin).getX() - 10, 12), new Color(0x7f7f7f));
            rend.drawRectangle(getPos().add(getSize().sub(margin).getX() - 10, 0), getPos().add(getSize().sub(margin).getX(), 12), new Color(0x5d5d5d));
            subParts.forEach(i -> i.renderBackground(gui, mouse.copy(), partialTicks));
        }
    }

    @Override
    public void renderForeground(IGui gui, Vect2i mouse) {
        if (hide) return;
        subParts.forEach(i -> i.renderForeground(gui, mouse.copy()));
    }

    @Override
    public void onMouseClick(IGui gui, Vect2i mouse, InputHandler.MouseButton button) {
        if (hide) return;
        if (button == InputHandler.MouseButton.LEFT) {
            if (IGui.isInside(mouse, getPos(), new Vect2i(getSize().getX() - 10, 12))) {
                moving = true;
                clickPoint = mouse.copy().sub(getPos());
            } else if (IGui.isInside(mouse, getPos().add(getSize().getX() - 12, 0), new Vect2i(12, 12))) {
                hide = true;
            }
        }
        subParts.forEach(i -> i.onMouseClick(gui, mouse.copy(), button));
    }

    @Override
    public boolean onKeyPressed(IGui gui, int key, int scancode, int action) {
        if (hide) return false;
        subParts.forEach(i -> i.onKeyPressed(gui, key, scancode, action));
        return false;
    }

    @Override
    public void onWheelMoves(IGui gui, double amount) {
        if (hide) return;
        subParts.forEach(i -> i.onWheelMoves(gui, amount));
    }

    @Override
    public void onCharPress(IGui gui, int key) {
        if (hide) return;
        subParts.forEach(i -> i.onCharPress(gui, key));
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean isMouseOnTop(IGui gui, Vect2i mouse, InputHandler.MouseButton button) {
        return !hide && IGui.isInside(mouse, getPos(), getSize());
    }
}
