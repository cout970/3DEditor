package com.cout970.editor2.gui;

import com.cout970.editor2.Editor;
import com.cout970.editor2.display.GLFWDisplay;
import com.cout970.editor2.display.InputHandler;
import com.cout970.editor2.gui.components.AbstractButton;
import com.cout970.editor2.gui.components.AbstractStateButton;
import com.cout970.editor2.gui.components.SimpleButton;
import com.cout970.editor2.gui.render.IGuiRenderer;
import com.cout970.editor2.gui.windows.CubeEditWindow;
import com.cout970.editor2.gui.windows.GroupEditWindow;
import com.cout970.editor2.gui.windows.ModelTreeWindow;
import com.cout970.editor2.gui.windows.TextureEditorWindow;
import com.cout970.editor2.model.IModel;
import com.cout970.editor2.model.TechneCube;
import com.cout970.editor2.render.texture.TextureStorage;
import com.cout970.editor2.util.Color;
import com.cout970.editor2.util.Vect2d;
import com.cout970.editor2.util.Vect2i;
import com.cout970.editor2.util.Vect3d;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cout970 on 12/02/2016.
 */
public class TopBar implements ISizedComponent {

    public static final int BAR_HEIGHT = 25;
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

    public void init() {
        int size = BAR_HEIGHT - 2;
        buttons.add(new SimpleButton(Vect2i.nullVector(), new Vect2i(80, size), TextureStorage.BUTTONS, "File", this::onPress, this::uvMapFile).setId(0));//file
        buttons.add(new SimpleButton(new Vect2i(80, 0), new Vect2i(80, size), TextureStorage.BUTTONS, "Editor", this::onPress, this::uvMapFile).setId(1));//editor
        buttons.add(new SimpleButton(new Vect2i(80 * 2, 0), new Vect2i(80, size), TextureStorage.BUTTONS, "Help", this::onPress, this::uvMapFile).setId(2));//help
        buttons.add(new SimpleButton(new Vect2i(80 * 3, 0), new Vect2i(size, size), TextureStorage.BUTTONS, this::onPress, this::uvMapAddCube).setId(3));//cube
        buttons.add(new SimpleButton(new Vect2i(80 * 3 + size * 1, 0), new Vect2i(size, size), TextureStorage.BUTTONS, this::onPress, this::uvMapAddTriangle).setId(4));//triangle
        buttons.add(new SimpleButton(new Vect2i(80 * 3 + size * 2, 0), new Vect2i(size, size), TextureStorage.BUTTONS, this::onPress, this::uvMapAddQuad).setId(5));//quad
        buttons.add(new SimpleButton(new Vect2i(80 * 3 + size * 3, 0), new Vect2i(size, size), TextureStorage.BUTTONS, this::onPress, this::uvMapCubeEditor).setId(6));//cube editor
        cubeEditor = new CubeEditWindow();
        modelTree = new ModelTreeWindow();
        textureEditor = new TextureEditorWindow();
        groupEditor = new GroupEditWindow();
    }

    public boolean onPress(AbstractButton button, Vect2i mouse, InputHandler.MouseButton mouseButton) {
        if (button.getId() == 3) {
            Editor.getProject().getModelTree().clearSelection();
            IModel m = new TechneCube("Shape" + Editor.getProject().getModelTree().getNumberOfModels(), new Vect3d(0, 0, 0), new Vect3d(1, 1, 1), TextureStorage.MODEL_TEXTURE, new Vect2d(0, 0), 16);
            Editor.getProject().getModelTree().addModel(m);
            Editor.getProject().getModelTree().addModelToSelection(m);
        } else if (button.getId() == 6) {
            cubeEditor.setHide(!cubeEditor.isHide());
        }
        return false;
    }

    public Vect2i uvMapFile(AbstractStateButton.ButtonState state) {
        if (state == AbstractStateButton.ButtonState.NORMAL) return new Vect2i(16, 0);
        if (state == AbstractStateButton.ButtonState.HOVER) return new Vect2i(16, BAR_HEIGHT - 2);
        if (state == AbstractStateButton.ButtonState.DOWN) return new Vect2i(16, (BAR_HEIGHT - 2) * 2);
        return Vect2i.nullVector();
    }

    public Vect2i uvMapAddCube(AbstractStateButton.ButtonState state) {
        if (state == AbstractStateButton.ButtonState.NORMAL) return new Vect2i(16, 69);
        if (state == AbstractStateButton.ButtonState.HOVER) return new Vect2i(16, 69 + BAR_HEIGHT - 2);
        if (state == AbstractStateButton.ButtonState.DOWN) return new Vect2i(16, 69 + (BAR_HEIGHT - 2) * 2);
        return Vect2i.nullVector();
    }

    public Vect2i uvMapAddTriangle(AbstractStateButton.ButtonState state) {
        if (state == AbstractStateButton.ButtonState.NORMAL) return new Vect2i(38, 69);
        if (state == AbstractStateButton.ButtonState.HOVER) return new Vect2i(38, 69 + BAR_HEIGHT - 2);
        if (state == AbstractStateButton.ButtonState.DOWN) return new Vect2i(38, 69 + (BAR_HEIGHT - 2) * 2);
        return Vect2i.nullVector();
    }

    public Vect2i uvMapAddQuad(AbstractStateButton.ButtonState state) {
        if (state == AbstractStateButton.ButtonState.NORMAL) return new Vect2i(60, 69);
        if (state == AbstractStateButton.ButtonState.HOVER) return new Vect2i(60, 69 + BAR_HEIGHT - 2);
        if (state == AbstractStateButton.ButtonState.DOWN) return new Vect2i(60, 69 + (BAR_HEIGHT - 2) * 2);
        return Vect2i.nullVector();
    }

    public Vect2i uvMapCubeEditor(AbstractStateButton.ButtonState state) {
        if (state == AbstractStateButton.ButtonState.NORMAL) return new Vect2i(82, 69);
        if (state == AbstractStateButton.ButtonState.HOVER) return new Vect2i(82, 69 + BAR_HEIGHT - 2);
        if (state == AbstractStateButton.ButtonState.DOWN) return new Vect2i(82, 69 + (BAR_HEIGHT - 2) * 2);
        return Vect2i.nullVector();
    }

    @Override
    public void onResize(IGui gui) {
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
        return new Vect2i(GLFWDisplay.getFrameBufferSize().getX(), BAR_HEIGHT);
    }

    @Override
    public boolean isMouseOnTop(IGui gui, Vect2i mouse, InputHandler.MouseButton button) {
        return IGui.isInside(mouse, getPos(), getSize());
    }
}
