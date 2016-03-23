package com.cout970.editor.gui.components;

import com.cout970.editor.gui.ISizedComponent;
import com.cout970.editor.util.Vect2i;

/**
 * Created by cout970 on 13/02/2016.
 */
public class IntegerTextBox extends TextBox {

    public IntegerTextBox(ISizedComponent parent, Vect2i offset) {
        super(parent, offset, 60);
        type = CenterType.RIGHT;
    }

    @Override
    public boolean isCharValid(int key) {
        return (key >= '0' && key <= '9') || key == '-';
    }
}
