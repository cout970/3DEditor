package com.cout970.editor.gui.event;

/**
 * Created by cout970 on 05/04/2016.
 */
public class TextInputEvent {

    private int key;

    public TextInputEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
