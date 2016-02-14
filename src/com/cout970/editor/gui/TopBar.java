package com.cout970.editor.gui;

import com.cout970.editor.InputHandler;
import com.cout970.editor.gui.components.AbstractButton;
import com.cout970.editor.gui.components.AbstractStateButton;
import com.cout970.editor.gui.components.SimpleButton;
import com.cout970.editor.gui.render.IGuiRenderer;
import com.cout970.editor.render.texture.TextureStorage;
import com.cout970.editor.util.Color;
import com.cout970.editor.util.Vect2i;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cout970 on 12/02/2016.
 */
public class TopBar implements IGuiComponent {

    public static final int BAR_HEIGHT = 25;
    private List<AbstractButton> buttons = new ArrayList<>();

    public void init(){
        buttons.add(new SimpleButton(Vect2i.nullVector(), new Vect2i(80, BAR_HEIGHT-2), TextureStorage.BUTTONS, "File", this::onPress, this::uvMapFile).setId(0));
        buttons.add(new SimpleButton(new Vect2i(80, 0), new Vect2i(80, BAR_HEIGHT-2), TextureStorage.BUTTONS, "Editor", this::onPress, this::uvMapFile).setId(1));
        buttons.add(new SimpleButton(new Vect2i(80*2, 0), new Vect2i(80, BAR_HEIGHT-2), TextureStorage.BUTTONS, "Help", this::onPress, this::uvMapFile).setId(2));
        buttons.add(new SimpleButton(new Vect2i(80*3, 0), new Vect2i(BAR_HEIGHT-2, BAR_HEIGHT-2), TextureStorage.BUTTONS, this::onPress, this::uvMapFile).setId(3));
    }

    public boolean onPress(AbstractButton button, Vect2i mouse, InputHandler.MouseButton mouseButton){
        return false;
    }

    public Vect2i uvMapFile(AbstractStateButton.ButtonState state){
        if (state == AbstractStateButton.ButtonState.NORMAL) return new Vect2i(16, 0);
        if (state == AbstractStateButton.ButtonState.HOVER) return new Vect2i(16, BAR_HEIGHT-2);
        if (state == AbstractStateButton.ButtonState.DOWN) return new Vect2i(16, (BAR_HEIGHT-2)*2);
        return Vect2i.nullVector();
    }

    @Override
    public void onResize(IGui gui) {}

    @Override
    public void renderBackground(IGui gui, Vect2i mouse, float partialTicks) {
        IGuiRenderer rend = gui.getGuiRenderer();
        rend.drawRectangle(Vect2i.nullVector(), new Vect2i(gui.getGuiSize().getX(), BAR_HEIGHT), new Color(0x7f7f7f));
        rend.drawRectangle(Vect2i.nullVector().add(0, BAR_HEIGHT-2), new Vect2i(gui.getGuiSize().getX(), BAR_HEIGHT), new Color(0x4C4C4C));
        buttons.forEach(i -> i.renderBackground(gui, mouse.copy(), partialTicks));
    }

    @Override
    public void renderForeground(IGui gui, Vect2i mouse) {
        buttons.forEach(i -> i.renderForeground(gui, mouse.copy()));
    }

    @Override
    public void onMouseClick(IGui gui, Vect2i mouse, InputHandler.MouseButton button) {
        buttons.forEach(i -> i.onMouseClick(gui, mouse.copy(), button));
    }

    @Override
    public boolean onKeyPressed(IGui gui, int key, int scancode, int action){
        buttons.forEach(i -> i.onKeyPressed(gui, key, scancode, action));
        return false;
    }

    @Override
    public void onCharPress(IGui gui, int key) {}

    @Override
    public void onWheelMoves(IGui gui, double amount) {
        buttons.forEach(i -> i.onWheelMoves(gui, amount));
    }
}
