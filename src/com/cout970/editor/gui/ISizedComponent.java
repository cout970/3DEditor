package com.cout970.editor.gui;

import com.cout970.editor.util.Vect2i;

/**
 * Created by cout970 on 13/02/2016.
 */
public interface ISizedComponent extends IGuiComponent {

    Vect2i getPos();
    Vect2i getSize();
}
