package com.cout970.editor.gui.components;

import com.cout970.editor.display.InputHandler;
import com.cout970.editor.gui.IGui;
import com.cout970.editor.render.IGuiRenderer;
import com.cout970.editor.render.texture.ITexture;
import com.cout970.editor.util.Color;
import com.cout970.editor.util.Vect2i;
import org.lwjgl.opengl.GL11;

import java.util.Collections;
import java.util.List;

/**
 * Created by cout970 on 24/01/2016.
 */
public abstract class AbstractStateButton extends AbstractButton {

    protected String text;
    protected String tooltip;
    protected ITexture texture;
    protected ButtonState state;
    protected boolean pressed;

    public AbstractStateButton(Vect2i pos, Vect2i size, ITexture texture, String text, ButtonListener listener) {
        super(pos, size, listener);
        this.text = text;
        this.texture = texture;
    }

    public AbstractStateButton(Vect2i pos, Vect2i size, ITexture texture, String text, String tooltip, ButtonListener listener) {
        this(pos, size, texture, text, listener);
        this.tooltip = tooltip;
    }

    public AbstractStateButton(Vect2i pos, Vect2i size, ITexture texture, ButtonListener listener) {
        this(pos, size, texture, null, listener);
    }

    @Override
    protected void renderButton(IGui gui, IGuiRenderer rend, Vect2i mouse) {
        GL11.glColor4f(1, 1, 1, 1);
        if (!gui.isButtonDown(InputHandler.MouseButton.LEFT) && !gui.isButtonDown(InputHandler.MouseButton.RIGHT)) {
            pressed = false;
            state = onButtonRelease();
        }
        state = onButtonHovered(IGui.isInside(mouse, getPos(gui), size));
        texture.bind();
        Vect2i uv = getUVFromState(state);
        rend.drawTexturedRectangle(getPos(gui), uv, size);
        String text = getText();
        if (text != null) {
            rend.drawCenteredString(text, getPos(gui).add(size.toVect2d().multiply(0.5).toVect2i()), getTextColor());
        }
    }

    @Override
    public void onMouseClick(IGui gui, Vect2i mouse, InputHandler.MouseButton button) {
        if (IGui.isInside(mouse, getPos(gui), size)) {
            pressed = button != InputHandler.MouseButton.MIDDLE;
            state = onButtonPress();
        }
        super.onMouseClick(gui, mouse, button);
    }

    @Override
    public void renderForeground(IGui gui, Vect2i mouse) {
        if (IGui.isInside(mouse, getPos(gui), size)) {
            List<String> toolTip = getButtonTooltip(gui, mouse);
            if (!toolTip.isEmpty()) {
                gui.getGuiRenderer().drawHoveringText(toolTip, mouse.sub(gui.getGuiStartingPoint()));
            }
        }
    }

    protected String getText() {
        return text;
    }

    protected List<String> getButtonTooltip(IGui gui, Vect2i mouse) {
        return tooltip == null ? Collections.EMPTY_LIST : Collections.singletonList(tooltip);
    }

    public Color getTextColor() {
        return new Color(0xFFFFFF);
    }

    protected abstract Vect2i getUVFromState(ButtonState state);

    protected abstract ButtonState onButtonPress();

    protected abstract ButtonState onButtonRelease();

    protected abstract ButtonState onButtonHovered(boolean hovered);


    public ButtonState getState() {
        return state;
    }

    public enum ButtonState {
        NORMAL(null),
        HOVER(NORMAL),
        DOWN(HOVER),
        DISABLED(null),
        DISABLED_HOVER(DISABLED),
        DISABLED_DOWN(DISABLED_HOVER),
        ACTIVE(null),
        ACTIVE_HOVER(ACTIVE),
        ACTIVE_DOWN(ACTIVE_HOVER),
        HIGHLIGHT(null);

        private ButtonState parent;

        ButtonState(ButtonState parent) {
            this.parent = parent;
        }

        public ButtonState getParent() {
            return parent;
        }
    }
}
