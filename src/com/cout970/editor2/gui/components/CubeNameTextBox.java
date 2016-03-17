package com.cout970.editor2.gui.components;

import com.cout970.editor2.gui.ISizedComponent;
import com.cout970.editor2.util.Vect2i;

/**
 * Created by cout970 on 13/02/2016.
 */
public class CubeNameTextBox extends TextBox {

    public CubeNameTextBox(ISizedComponent parent, Vect2i offset, int lenght) {
        super(parent, offset, lenght);
        type = CenterType.LEFT;
    }

    @Override
    public boolean isCharValid(int key) {
        return (key >= '0' && key <= '9') || (key >= 'A' && key <= 'Z') || (key >= 'a' && key <= 'z') || key == '_';
    }
}
