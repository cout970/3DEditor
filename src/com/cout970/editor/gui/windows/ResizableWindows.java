package com.cout970.editor.gui.windows;

import com.cout970.editor.display.InputHandler;
import com.cout970.editor.gui.IGui;
import com.cout970.editor.util.Vect2i;

/**
 * Created by cout970 on 24/03/2016.
 */
public class ResizableWindows extends InternalWindow {

    protected int selection;
    protected Vect2i oldPos;
    protected Vect2i oldSize;
    protected int minSize = 50;

    public ResizableWindows(Vect2i size, IGui gui) {
        super(size, gui);
    }

    @Override
    public void onMouseClick(IGui gui, Vect2i mouse, InputHandler.MouseButton button) {
        if (!hide) {
            if (button == InputHandler.MouseButton.LEFT) {
                int border = 5;
                if (IGui.isInside(mouse, getPos(), new Vect2i(getSize().getX(), 3))) {
                    selection = 1;
                    oldPos = getPos();
                    oldSize = getSize();
                    return;
                } else if (IGui.isInside(mouse, getPos().add(0, getSize().getY() - border), new Vect2i(getSize().getX(), border))) {
                    selection = 2;
                    oldPos = getPos();
                    oldSize = getSize();
                    return;
                } else if (IGui.isInside(mouse, getPos(), new Vect2i(border, getSize().getY()))) {
                    selection = 3;
                    oldPos = getPos();
                    oldSize = getSize();
                    return;
                } else if (IGui.isInside(mouse, getPos().add(getSize().getX() - border, 0), new Vect2i(border, getSize().getY()))) {
                    selection = 4;
                    oldPos = getPos();
                    oldSize = getSize();
                    return;
                } else {
                    selection = 0;
                }
            } else {
                selection = 0;
            }
            super.onMouseClick(gui, mouse.copy(), button);
        }
    }

    @Override
    public void renderBackground(IGui gui, Vect2i mouse, float partialTicks) {
        if (!hide) {
            Vect2i start = gui.getGuiStartingPoint();
            Vect2i end = gui.getGuiSize();
            if (selection != 0) {
                Vect2i m = mouse.copy().sub(oldPos);

                if (selection == 1) {
                    int ySize = Math.max(minSize, oldSize.getY() - m.getY());
                    int yPos = oldPos.getY() + Math.min(oldSize.getY() - minSize, m.getY());
                    yPos = Math.max(yPos, start.getY());
                    ySize = Math.min(ySize, end.getY() - yPos);
                    ySize = Math.min(ySize, oldSize.getY() + oldPos.getY() - start.getY());

                    setSize(new Vect2i(ySize, ySize));
                    setPos(new Vect2i(oldPos.getX(), yPos));
                } else if (selection == 2) {

                    int ySize = Math.max(minSize, m.getY());
                    ySize = Math.min(ySize, end.getY() - oldPos.getY());
                    ySize = Math.min(ySize, end.getX() - oldPos.getX());
                    setSize(new Vect2i(ySize, ySize));
                } else if (selection == 3) {

                    int xSize = Math.max(minSize, oldSize.getX() - m.getX());
                    int xPos = oldPos.getX() + Math.min(oldSize.getX() - minSize, m.getX());
                    xPos = Math.max(xPos, start.getX());
                    xSize = Math.min(xSize, end.getY() - oldPos.getY());
                    xPos = Math.max(xPos, oldPos.getX() - (xSize - oldSize.getX()));

                    setSize(new Vect2i(xSize, xSize));
                    setPos(new Vect2i(xPos, oldPos.getY()));
                } else if (selection == 4) {

                    int xSize = Math.max(minSize, m.getX());
                    xSize = Math.min(xSize, end.getX() - oldPos.getX());
                    xSize = Math.min(xSize, end.getY() - oldPos.getY());
                    setSize(new Vect2i(xSize, xSize));
                }
                if (!InputHandler.isMouseButtonPress(InputHandler.MouseButton.LEFT)) {
                    selection = 0;
                }
            }
            int size = Math.min(getSize().getX(), end.getY() - getPos().getY());
            size = Math.min(size, end.getX() - getPos().getX());
            size = Math.max(minSize, size);
            setSize(new Vect2i(size, size+10));
        }
        super.renderBackground(gui, mouse, partialTicks);
    }
}
