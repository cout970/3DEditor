package com.cout970.editor.gui.components;

import com.cout970.editor.gui.api.IGui;
import com.cout970.editor.gui.api.ISizedComponent;
import com.cout970.editor.render.texture.ITexture;
import com.cout970.editor.util.Vect2i;

import java.util.function.Function;

/**
 * Created by cout970 on 27/03/2016.
 */
public class InnerButton extends SimpleButton {

    private Vect2i offset;
    private ISizedComponent parent;

    public InnerButton(ISizedComponent parent, Vect2i offset, Vect2i size, ITexture texture, String text, String tooltip, ButtonListener listener, Function<ButtonState, Vect2i> uvMapper) {
        super(new Vect2i(0,0), size, texture, text, tooltip, listener, uvMapper);
        this.offset = offset.copy();
        this.parent = parent;
    }

    @Override
    public void renderBackground(IGui gui, Vect2i mouse, float partialTicks) {
        super.renderBackground(gui, mouse, partialTicks);
        setPos(offset.copy().add(parent.getPos()));
    }
}
