package com.cout970.editor.gui.components;

import com.cout970.editor.InputHandler;
import com.cout970.editor.gui.IGui;
import com.cout970.editor.gui.ISizedComponent;
import com.cout970.editor.util.Color;
import com.cout970.editor.util.Vect2i;
import org.lwjgl.glfw.GLFW;

/**
 * Created by cout970 on 12/02/2016.
 */
public class TextBox implements ISizedComponent, ILockable {

    protected String buffer;
    protected int pointer;
    protected ISizedComponent parent;
    protected Vect2i offset;
    protected Vect2i size;
    protected int maxLenght;
    protected int selectionStart;
    protected int selectionEnd;
    protected int selectionAux;
    protected boolean focused;
    protected CenterType type;
    protected boolean changes;
    protected boolean locked;

    public TextBox(ISizedComponent parent, Vect2i offset, int lenght) {
        this.parent = parent;
        this.offset = offset;
        this.size = new Vect2i(lenght, 16);
        maxLenght = (lenght - 1) / 8;
        pointer = 0;
        type = CenterType.CENTERED;
        buffer = "";
    }

    @Override
    public void onResize(IGui gui) {
    }

    @Override
    public void renderBackground(IGui gui, Vect2i mouse, float partialTicks) {
        Vect2i margin = new Vect2i(1, 1);
        gui.getGuiRenderer().drawRectangle(parent.getPos().add(offset), parent.getPos().add(offset).add(size), new Color(0));
        gui.getGuiRenderer().drawRectangle(parent.getPos().add(offset).add(margin), parent.getPos().add(offset).add(size).sub(margin), new Color(0x999999));
        if (!locked) {
            Vect2i pos0;
            if (type == CenterType.LEFT) {
                pos0 = parent.getPos().add(offset).copy();
            } else if (type == CenterType.RIGHT) {
                pos0 = parent.getPos().add(offset).copy().add(size.getX() - buffer.length() * 8 - 4, 0);
            } else {
                pos0 = parent.getPos().add(offset).copy().add(size.getX() / 2 - buffer.length() * 4 - 4, 0);
            }
            for (int i = 0; i < buffer.length(); i++) {
                char c = buffer.charAt(i);
                Vect2i pos = new Vect2i(pos0.getX() + i * 8 + 2, pos0.getY() + 4);
                if (i >= selectionStart && i < selectionEnd) {
                    gui.getGuiRenderer().drawRectangle(pos.copy().sub(0, 2), pos.copy().add(8, 10), new Color(0x214283));
                }
                gui.getFontRenderer().drawString("" + c, pos.getX(), pos.getY(), 0);
            }
            if (focused) {
                if (((int) (GLFW.glfwGetTime() * 1000)) % 1000 > 500) {
                    gui.getGuiRenderer().drawRectangle(new Vect2i(pos0.getX() + pointer * 8 + 2, pos0.getY() + 2),
                            new Vect2i(pos0.getX() + pointer * 8 + 4, pos0.getY() + 8 + 6), new Color(0));
                }
            }
        } else {
            gui.getGuiRenderer().drawRectangle(parent.getPos().add(offset).add(margin), parent.getPos().add(offset).add(size).sub(margin), new Color(0x777777));
        }
    }

    @Override
    public void renderForeground(IGui gui, Vect2i mouse) {

    }

    @Override
    public void onMouseClick(IGui gui, Vect2i mouse, InputHandler.MouseButton button) {
//        Log.debug("click: "+mouse+", button: "+button);
        if (!focused && button == InputHandler.MouseButton.LEFT && IGui.isInside(mouse, getPos(), getSize())) {
            focused = true;
            onFocusGained();
        } else if (focused && !IGui.isInside(mouse, getPos(), getSize())) {
            focused = false;
            onFocusLose();
        } else if (focused && button == InputHandler.MouseButton.LEFT && IGui.isInside(mouse, getPos(), getSize())) {
            Vect2i point = mouse.copy().sub(getPos());
            int sel = point.getX() / 8;
            if (sel > buffer.length()) sel = buffer.length();
            pointer = sel;
            if (selectionEnd - selectionStart > 0) {
                selectionStart = 0;
                selectionEnd = 0;
            }
        }
    }

    public void onFocusGained() {
        selectionStart = 0;
        selectionEnd = buffer.length();
        pointer = selectionEnd;
    }

    public void onFocusLose() {
        selectionStart = 0;
        selectionEnd = 0;
        changes = true;
    }

    @Override
    public boolean onKeyPressed(IGui gui, int key, int scancode, int action) {
        if (action == GLFW.GLFW_PRESS && focused) {
            if (gui.isShiftKeyPressed() && (key == GLFW.GLFW_KEY_LEFT || key == GLFW.GLFW_KEY_RIGHT)) {
                if (selectionEnd - selectionStart == 0) {
                    selectionStart = pointer;
                    selectionEnd = pointer;
                    selectionAux = pointer;
                }
                if (key == GLFW.GLFW_KEY_LEFT) {
                    if (selectionEnd == selectionAux) {
                        if (selectionStart > 0)
                            selectionStart--;
                    } else {
                        if (selectionEnd > selectionAux)
                            selectionEnd--;
                    }
                } else {
                    if (selectionStart == selectionAux) {
                        if (selectionEnd < buffer.length())
                            selectionEnd++;
                    } else if (selectionEnd == selectionAux) {
                        if (selectionStart < selectionAux)
                            selectionStart++;
                    } else {
                        if (selectionEnd < buffer.length())
                            selectionEnd++;
                    }
                }
                if (selectionEnd - selectionStart == 0) {
                    selectionStart = 0;
                    selectionEnd = 0;
                }
            } else if (selectionEnd - selectionStart > 0) {
                if (key == GLFW.GLFW_KEY_BACKSPACE || key == GLFW.GLFW_KEY_DELETE) {
                    buffer = buffer.substring(0, selectionStart) + buffer.substring(selectionEnd, buffer.length());
                    pointer = selectionStart;
                    selectionStart = 0;
                    selectionEnd = 0;
                } else if (key == GLFW.GLFW_KEY_LEFT) {
                    selectionEnd = selectionStart;
                    pointer = selectionStart;
                } else if (key == GLFW.GLFW_KEY_RIGHT) {
                    selectionStart = selectionEnd;
                    pointer = selectionStart;
                }
            } else if (key == GLFW.GLFW_KEY_BACKSPACE) {
                if (pointer > 0 && buffer.length() > 0) {
                    buffer = buffer.substring(0, pointer - 1) + buffer.substring(pointer, buffer.length());
                    pointer--;
                }
            } else if (key == GLFW.GLFW_KEY_DELETE) {
                if (buffer.length() > 0 && pointer < buffer.length()) {
//                    Log.debug("part1: '"+(buffer.substring(0, pointer-1))+"' part2: '"+(buffer.substring(pointer, buffer.length()))+"' pointer: "+pointer);
                    buffer = buffer.substring(0, pointer) + buffer.substring(pointer + 1, buffer.length());
                }
            } else if (key == GLFW.GLFW_KEY_LEFT) {
                if (pointer > 0) {
                    pointer--;
                }
            } else if (key == GLFW.GLFW_KEY_RIGHT) {
                if (pointer < buffer.length()) {
                    pointer++;
                }
            } else if (key == GLFW.GLFW_KEY_ENTER) {
                focused = false;
                onFocusLose();
            }
        }

        return false;
    }

    @Override
    public void onCharPress(IGui gui, int key) {
        char c = (char) key;
        if (focused && isCharValid(key)) {
            if (selectionEnd - selectionStart > 0) {
                buffer = buffer.substring(0, selectionStart) + c + buffer.substring(selectionEnd, buffer.length());
                pointer = selectionStart + 1;
                selectionStart = 0;
                selectionEnd = 0;
            } else if (buffer.length() < maxLenght) {
                buffer = buffer.substring(0, pointer) + c + buffer.substring(pointer, buffer.length());
                pointer++;
            }
        }
    }

    public boolean isCharValid(int key) {
        return key >= 32 && key <= 126;
    }

    @Override
    public void onWheelMoves(IGui gui, double amount) {
    }

    public CenterType getCenterType() {
        return type;
    }

    public void setCenterType(CenterType type) {
        this.type = type;
    }

    public String getBuffer() {
        return buffer;
    }

    public ISizedComponent getParent() {
        return parent;
    }

    public int getMaxLenght() {
        return maxLenght;
    }

    public void setBuffer(String buffer) {
        this.buffer = buffer;
        pointer = buffer.length();
    }

    @Override
    public Vect2i getPos() {
        return parent.getPos().add(offset);
    }

    @Override
    public Vect2i getSize() {
        return size.copy();
    }

    public boolean hasChanged() {
        return changes;
    }

    public void resetChanges() {
        changes = false;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Override
    public void setLocked(boolean b) {
        this.locked = b;
    }

    public enum CenterType {
        LEFT, CENTERED, RIGHT
    }
}
