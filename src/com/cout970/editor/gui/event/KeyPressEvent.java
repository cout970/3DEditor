package com.cout970.editor.gui.event;

/**
 * Created by cout970 on 05/04/2016.
 */
public class KeyPressEvent implements IGuiEvent {

    private int key;
    private int scancode;
    private int action;

    public KeyPressEvent(int key, int scancode, int action) {
        this.key = key;
        this.scancode = scancode;
        this.action = action;
    }

    public int getKey() {
        return key;
    }

    public int getScancode() {
        return scancode;
    }

    public int getAction() {
        return action;
    }
}
