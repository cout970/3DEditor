package com.cout970.editor2.gui.components;

import com.cout970.editor2.display.InputHandler;
import com.cout970.editor2.gui.IGui;
import com.cout970.editor2.gui.ISizedComponent;
import com.cout970.editor2.gui.render.IGuiRenderer;
import com.cout970.editor2.render.texture.TextureStorage;
import com.cout970.editor2.util.Vect2i;

/**
 * Created by cout970 on 13/02/2016.
 */
public class NumberEdit implements ISizedComponent, ILockable {

    private static Vect2i size = new Vect2i(78, 18);
    private ISizedComponent parent;
    private Vect2i offset;
    private double value;
    private TextBox number;
    private AbstractButton up;
    private AbstractButton down;
    private boolean floats;
    private Vect2i lastTickMouse;
    private boolean changes;
    protected int level;

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    public NumberEdit(ISizedComponent parent, Vect2i offset, boolean floats) {
        this.parent = parent;
        this.offset = offset;
        this.floats = floats;
        lastTickMouse = Vect2i.nullVector();
        if (floats) {
            number = new FloatTextBox(this, Vect2i.nullVector()) {
                public void onFocusLose() {
                    super.onFocusLose();
                    String s = getBuffer();
                    try {
                        setValue(Float.parseFloat(s));
                    } catch (NumberFormatException e) {
                        setBuffer(String.valueOf(getValueClamped()).replace(',', '.'));
                    }
                }
            };
        } else {
            number = new IntegerTextBox(this, Vect2i.nullVector()) {
                public void onFocusLose() {
                    super.onFocusLose();
                    String s = getBuffer();
                    try {
                        setValue(Integer.parseInt(s));
                    } catch (NumberFormatException e) {
                        setBuffer(String.valueOf((int) getValue()));
                    }
                }
            };
        }
        up = new SimpleButton(new Vect2i(60, 0), new Vect2i(18, 8), TextureStorage.BUTTONS, this::onPress, this::upUvMapper) {
            protected Vect2i getPos(IGui gui) {
                return pos.copy().add(parent.getPos()).add(offset);
            }
        }.setId(0);

        down = new SimpleButton(new Vect2i(60, 8), new Vect2i(18, 8), TextureStorage.BUTTONS, this::onPress, this::downUvMapper) {
            protected Vect2i getPos(IGui gui) {
                return pos.copy().add(parent.getPos()).add(offset);
            }
        }.setId(1);
        setValue(0);
    }

    public double getValueClamped() {
        return Double.parseDouble(String.format("%.3f", getValue()).replace(',', '.'));
    }

    public boolean onPress(AbstractButton button, Vect2i mouse, InputHandler.MouseButton mouseButton) {
        if (button.getId() == 0) {
            setValue(getValue() + 1);
        } else if (button.getId() == 1) {
            setValue(getValue() - 1);
        }
        return false;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        if (!floats) {
            this.value = (int) value;
            number.setBuffer(String.format("%d", (int) value));
        } else {
            this.value = value;
            number.setBuffer(String.valueOf(getValueClamped()).replace(',', '.'));
        }
        changes = true;
    }

    public Vect2i upUvMapper(AbstractStateButton.ButtonState buttonState) {
        if (buttonState == AbstractStateButton.ButtonState.NORMAL) {
            return new Vect2i(96, 0);
        } else if (buttonState == AbstractStateButton.ButtonState.HOVER) {
            return new Vect2i(96, 16);
        } else {
            return new Vect2i(96, 8);
        }
    }

    public Vect2i downUvMapper(AbstractStateButton.ButtonState buttonState) {
        if (buttonState == AbstractStateButton.ButtonState.NORMAL) {
            return new Vect2i(114, 0);
        } else if (buttonState == AbstractStateButton.ButtonState.HOVER) {
            return new Vect2i(114, 16);
        } else {
            return new Vect2i(114, 8);
        }
    }

    @Override
    public void onResize(IGui gui) {
    }

    @Override
    public void renderBackground(IGui gui, Vect2i mouse, float partialTicks) {
        IGuiRenderer rend = gui.getGuiRenderer();
        number.renderBackground(gui, mouse.copy(), partialTicks);
        up.renderBackground(gui, mouse.copy(), partialTicks);
        down.renderBackground(gui, mouse.copy(), partialTicks);
        lastTickMouse = mouse.copy();
    }

    @Override
    public void renderForeground(IGui gui, Vect2i mouse) {
        number.renderForeground(gui, mouse);
        up.renderForeground(gui, mouse);
        down.renderForeground(gui, mouse);
    }

    @Override
    public void onMouseClick(IGui gui, Vect2i mouse, InputHandler.MouseButton button) {
        number.onMouseClick(gui, mouse, button);
        up.onMouseClick(gui, mouse, button);
        down.onMouseClick(gui, mouse, button);
    }

    @Override
    public boolean onKeyPressed(IGui gui, int key, int scancode, int action) {
        number.onKeyPressed(gui, key, scancode, action);
        up.onKeyPressed(gui, key, scancode, action);
        down.onKeyPressed(gui, key, scancode, action);
        return false;
    }

    @Override
    public void onCharPress(IGui gui, int key) {
        number.onCharPress(gui, key);
        up.onCharPress(gui, key);
        down.onCharPress(gui, key);
    }

    @Override
    public void onWheelMoves(IGui gui, double amount) {
        number.onWheelMoves(gui, amount);
        up.onWheelMoves(gui, amount);
        down.onWheelMoves(gui, amount);
        if (IGui.isInside(lastTickMouse, getPos(), getSize())) {
            setValue(getValue() + amount);
        }
    }

    @Override
    public Vect2i getPos() {
        return parent.getPos().add(offset);
    }

    @Override
    public Vect2i getSize() {
        return size.copy();
    }

    @Override
    public boolean isMouseOnTop(IGui gui, Vect2i mouse, InputHandler.MouseButton button) {
        return IGui.isInside(mouse, getPos(), getSize());
    }

    public boolean hasChanged() {
        return changes;
    }

    public void resetChanges() {
        changes = false;
    }

    @Override
    public boolean isLocked() {
        return number.isLocked();
    }

    @Override
    public void setLocked(boolean b) {
        number.setLocked(b);
    }


}
