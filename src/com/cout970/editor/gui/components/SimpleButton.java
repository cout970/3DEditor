package com.cout970.editor.gui.components;


import com.cout970.editor.render.texture.ITexture;
import com.cout970.editor.util.Vect2i;

import java.util.function.Function;

/**
 * Created by cout970 on 30/01/2016.
 */
public class SimpleButton extends AbstractStateButton {

    protected Function<ButtonState, Vect2i> uvMapper;
    protected int level;

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    public SimpleButton(Vect2i pos, Vect2i size, ITexture texture, String text, ButtonListener listener, Function<ButtonState, Vect2i> uvMapper) {
        super(pos, size, texture, text, listener);
        this.uvMapper = uvMapper;
    }

    public SimpleButton(Vect2i pos, Vect2i size, ITexture texture, String text, String tooltip, ButtonListener listener, Function<ButtonState, Vect2i> uvMapper) {
        super(pos, size, texture, text, tooltip, listener);
        this.uvMapper = uvMapper;
    }

    public SimpleButton(Vect2i pos, Vect2i size, ITexture texture, ButtonListener listener, Function<ButtonState, Vect2i> uvMapper) {
        super(pos, size, texture, listener);
        this.uvMapper = uvMapper;
    }

    public SimpleButton(Vect2i pos, Vect2i size, ITexture texture, ButtonListener listener, Vect2i offset) {
        this(pos, size, texture, null, null, listener, offset);
    }

    public SimpleButton(Vect2i pos, Vect2i size, ITexture texture, String text, String tooltip, ButtonListener listener, Vect2i offset) {
        super(pos, size, texture, text, tooltip, listener);
        Vect2i offset2 = offset.copy();
        Vect2i offset3 = offset.copy().add(0, size.getY());
        Vect2i offset4 = offset.copy().add(0, size.getY() * 2);
        this.uvMapper = buttonState -> {
            switch (buttonState) {
                case NORMAL:
                    return offset2;
                case HOVER:
                    return offset3;
                case DOWN:
                    return offset4;
            }
            return offset2;
        };
    }

    @Override
    protected Vect2i getUVFromState(ButtonState state) {
        return uvMapper.apply(state);
    }

    @Override
    protected ButtonState onButtonPress() {
        return ButtonState.DOWN;
    }

    @Override
    protected ButtonState onButtonRelease() {
        return ButtonState.NORMAL;
    }

    @Override
    protected ButtonState onButtonHovered(boolean hovered) {
        return pressed ? ButtonState.DOWN : hovered ? ButtonState.HOVER : ButtonState.NORMAL;
    }
}
