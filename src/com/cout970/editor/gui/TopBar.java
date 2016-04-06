package com.cout970.editor.gui;

import com.cout970.editor.display.Display;
import com.cout970.editor.display.InputHandler;
import com.cout970.editor.gui.components.AbstractButton;
import com.cout970.editor.gui.components.AbstractStateButton;
import com.cout970.editor.gui.components.SimpleButton;
import com.cout970.editor.gui.api.IGui;
import com.cout970.editor.gui.api.IGuiComponent;
import com.cout970.editor.gui.api.ISizedComponent;
import com.cout970.editor.gui.windows.CubeEditWindow;
import com.cout970.editor.gui.windows.GroupEditWindow;
import com.cout970.editor.gui.windows.ModelTreeWindow;
import com.cout970.editor.gui.windows.TextureEditorWindow;
import com.cout970.editor.render.IGuiRenderer;
import com.cout970.editor.render.texture.TextureStorage;
import com.cout970.editor.util.Color;
import com.cout970.editor.util.Vect2i;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cout970 on 12/02/2016.
 */
public class TopBar implements ISizedComponent {

    public static final int BAR_HEIGHT = 34;
    private List<AbstractButton> buttons = new ArrayList<>();
    private CubeEditWindow cubeEditor;
    private ModelTreeWindow modelTree;
    private TextureEditorWindow textureEditor;
    private GroupEditWindow groupEditor;
    protected int level;

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    public void init(IGui gui) {
        int size = BAR_HEIGHT - 2;
        //new
        buttons.add(new SimpleButton(Vect2i.nullVector(), new Vect2i(size, size), TextureStorage.BUTTONS, null, "New", this::onPress, this::uvMapNewFile).setId(0));
        //load
        buttons.add(new SimpleButton(new Vect2i(32, 0), new Vect2i(size, size), TextureStorage.BUTTONS, null, "Load", this::onPress, this::uvMapLoadProject).setId(1));
        //save
        buttons.add(new SimpleButton(new Vect2i(32 * 2, 0), new Vect2i(size, size), TextureStorage.BUTTONS, null, "Save", this::onPress, this::uvMapSaveProject).setId(2));
        //save as
        buttons.add(new SimpleButton(new Vect2i(32 * 3, 0), new Vect2i(size, size), TextureStorage.BUTTONS, null, "Save as", this::onPress, this::uvMapSaveProjectAs).setId(3));
        //load texture
        buttons.add(new SimpleButton(new Vect2i(32 * 4, 0), new Vect2i(size, size), TextureStorage.BUTTONS, null, "Load Texture", this::onPress, this::uvMapLoadTexture).setId(4));
        //add cube
        buttons.add(new SimpleButton(new Vect2i(32 * 5, 0), new Vect2i(size, size), TextureStorage.BUTTONS, null, "Add cube", this::onPress, this::uvMapAddCube).setId(5));
        //add cube
        buttons.add(new SimpleButton(new Vect2i(32 * 6, 0), new Vect2i(size, size), TextureStorage.BUTTONS, null, "Open/Close cube config", this::onPress, this::uvMapCubeConfig).setId(6));
        //add cube
        buttons.add(new SimpleButton(new Vect2i(32 * 7, 0), new Vect2i(size, size), TextureStorage.BUTTONS, null, "Open/Close texture view", this::onPress, this::uvMapTextureView).setId(7));
        //TODO
//        buttons.add(new SimpleButton(new Vect2i(80 * 3 + size * 1, 0), new Vect2i(size, size), TextureStorage.BUTTONS, this::onPress, this::uvMapAddTriangle).setId(4));//triangle
//        buttons.add(new SimpleButton(new Vect2i(80 * 3 + size * 2, 0), new Vect2i(size, size), TextureStorage.BUTTONS, this::onPress, this::uvMapAddQuad).setId(5));//quad
//        buttons.add(new SimpleButton(new Vect2i(80 * 3 + size * 3, 0), new Vect2i(size, size), TextureStorage.BUTTONS, this::onPress, this::uvMapCubeEditor).setId(6));//cube editor
        cubeEditor = new CubeEditWindow(gui);
        modelTree = new ModelTreeWindow(gui);
        textureEditor = new TextureEditorWindow(gui);
        groupEditor = new GroupEditWindow(gui);
    }

    public boolean onPress(AbstractButton button, Vect2i mouse, InputHandler.MouseButton mouseButton) {
        if (button.getId() == 0) {//new project
            GuiController.INSTANCE.buttonNewProject();
        } else if (button.getId() == 1) {//load
            GuiController.INSTANCE.buttonLoadProject();
        } else if (button.getId() == 2) {//save
            GuiController.INSTANCE.buttonSaveProject();
        } else if (button.getId() == 3) {//save as
            GuiController.INSTANCE.buttonSaveAsProject();
        } else if (button.getId() == 4) {//load texture
            GuiController.INSTANCE.buttonLoadTexture();
        } else if (button.getId() == 5) {//add cube
            GuiController.INSTANCE.buttonAddCube();
        } else if (button.getId() == 6) {
            cubeEditor.setHide(!cubeEditor.isHide());
        }else if (button.getId() == 7) {
            textureEditor.setHide(!textureEditor.isHide());
        }
        return false;
    }

    public Vect2i uvMapNewFile(AbstractStateButton.ButtonState state) {
        if (state == AbstractStateButton.ButtonState.NORMAL) { return new Vect2i(0, 69); }
        if (state == AbstractStateButton.ButtonState.HOVER) { return new Vect2i(32, 69); }
        if (state == AbstractStateButton.ButtonState.DOWN) { return new Vect2i(64, 69); }
        return Vect2i.nullVector();
    }

    public Vect2i uvMapLoadProject(AbstractStateButton.ButtonState state) {
        if (state == AbstractStateButton.ButtonState.NORMAL) { return new Vect2i(0, 69 + 32); }
        if (state == AbstractStateButton.ButtonState.HOVER) { return new Vect2i(32, 69 + 32); }
        if (state == AbstractStateButton.ButtonState.DOWN) { return new Vect2i(64, 69 + 32); }
        return Vect2i.nullVector();
    }

    public Vect2i uvMapSaveProject(AbstractStateButton.ButtonState state) {
        if (state == AbstractStateButton.ButtonState.NORMAL) { return new Vect2i(0, 69 + 32 * 2); }
        if (state == AbstractStateButton.ButtonState.HOVER) { return new Vect2i(32, 69 + 32 * 2); }
        if (state == AbstractStateButton.ButtonState.DOWN) { return new Vect2i(64, 69 + 32 * 2); }
        return Vect2i.nullVector();
    }

    public Vect2i uvMapSaveProjectAs(AbstractStateButton.ButtonState state) {
        if (state == AbstractStateButton.ButtonState.NORMAL) { return new Vect2i(0, 69 + 32 * 3); }
        if (state == AbstractStateButton.ButtonState.HOVER) { return new Vect2i(32, 69 + 32 * 3); }
        if (state == AbstractStateButton.ButtonState.DOWN) { return new Vect2i(64, 69 + 32 * 3); }
        return Vect2i.nullVector();
    }

    public Vect2i uvMapLoadTexture(AbstractStateButton.ButtonState state) {
        if (state == AbstractStateButton.ButtonState.NORMAL) { return new Vect2i(0, 69 + 32 * 4); }
        if (state == AbstractStateButton.ButtonState.HOVER) { return new Vect2i(32, 69 + 32 * 4); }
        if (state == AbstractStateButton.ButtonState.DOWN) { return new Vect2i(64, 69 + 32 * 4); }
        return Vect2i.nullVector();
    }

    public Vect2i uvMapAddCube(AbstractStateButton.ButtonState state) {
        if (state == AbstractStateButton.ButtonState.NORMAL) { return new Vect2i(96, 69); }
        if (state == AbstractStateButton.ButtonState.HOVER) { return new Vect2i(96 + 32, 69); }
        if (state == AbstractStateButton.ButtonState.DOWN) { return new Vect2i(96 + 64, 69); }
        return Vect2i.nullVector();
    }

    public Vect2i uvMapCubeConfig(AbstractStateButton.ButtonState state) {
        if (state == AbstractStateButton.ButtonState.NORMAL) { return new Vect2i(96, 69 + 32); }
        if (state == AbstractStateButton.ButtonState.HOVER) { return new Vect2i(96 + 32, 69 + 32); }
        if (state == AbstractStateButton.ButtonState.DOWN) { return new Vect2i(96 + 64, 69 + 32); }
        return Vect2i.nullVector();
    }

    public Vect2i uvMapTextureView(AbstractStateButton.ButtonState state) {
        if (state == AbstractStateButton.ButtonState.NORMAL) { return new Vect2i(96, 69 + 32 * 2); }
        if (state == AbstractStateButton.ButtonState.HOVER) { return new Vect2i(96 + 32, 69 + 32 * 2); }
        if (state == AbstractStateButton.ButtonState.DOWN) { return new Vect2i(96 + 64, 69 + 32 * 2); }
        return Vect2i.nullVector();
    }

    @Override
    public void renderBackground(IGui gui, Vect2i mouse, float partialTicks) {
        IGuiRenderer rend = gui.getGuiRenderer();
        rend.drawRectangle(Vect2i.nullVector(), new Vect2i(gui.getGuiSize().getX(), BAR_HEIGHT), new Color(0x7f7f7f));
        rend.drawRectangle(Vect2i.nullVector().add(0, BAR_HEIGHT - 2), new Vect2i(gui.getGuiSize().getX(), BAR_HEIGHT), new Color(0x4C4C4C));
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
    public boolean onKeyPressed(IGui gui, int key, int scancode, int action) {
        buttons.forEach(i -> i.onKeyPressed(gui, key, scancode, action));
        return false;
    }

    @Override
    public void onCharPress(IGui gui, int key) {
    }

    @Override
    public void onWheelMoves(IGui gui, double amount) {
        buttons.forEach(i -> i.onWheelMoves(gui, amount));
    }

    public IGuiComponent getCubeEditor() {
        return cubeEditor;
    }

    public IGuiComponent getModelTree() {
        return modelTree;
    }

    public IGuiComponent getTextureEditor() {
        return textureEditor;
    }

    public IGuiComponent getGroupEditor() {
        return groupEditor;
    }

    @Override
    public Vect2i getPos() {
        return Vect2i.nullVector();
    }

    @Override
    public Vect2i getSize() {
        return new Vect2i(Display.getFrameBufferSize().getX(), BAR_HEIGHT);
    }

    @Override
    public boolean isMouseOnTop(IGui gui, Vect2i mouse, InputHandler.MouseButton button) {
        return IGui.isInside(mouse, getPos(), getSize());
    }
}
