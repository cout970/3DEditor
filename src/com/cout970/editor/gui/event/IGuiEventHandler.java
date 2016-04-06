package com.cout970.editor.gui.event;

import com.cout970.editor.gui.api.IGui;

/**
 * Created by cout970 on 05/04/2016.
 */
public interface IGuiEventHandler {

    boolean handleEvent(IGui gui, IGuiEvent event);

}
