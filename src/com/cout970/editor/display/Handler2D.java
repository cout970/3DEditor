package com.cout970.editor.display;

import com.cout970.editor.ConfigurationFile;
import com.cout970.editor.ModelTree;
import com.cout970.editor.gui.Gui;
import com.cout970.editor.gui.TopBar;
import com.cout970.editor.render.engine.IRenderEngine;
import com.cout970.editor.render.texture.TextureStorage;
import com.cout970.editor.util.Vect2i;

import static org.lwjgl.opengl.GL11.GL_QUADS;

/**
 * Created by cout970 on 10/02/2016.
 */
public class Handler2D implements InputHandler.IKeyboardCallback {

    private ConfigurationFile config = ConfigurationFile.INSTANCE;
    private IRenderEngine engine = IRenderEngine.INSTANCE;
    private Vect2i size = GLFWDisplay.getFrameBufferSize().copy();
    private Vect2i center = GLFWDisplay.getFrameBufferSize().copy().division(2);
    private Gui gui;
    private TopBar topBar;

    public void init() {
        gui = new Gui();
        gui.addComponent(topBar = new TopBar());
        topBar.init();
        gui.addComponent(topBar.getCubeEditor());
        gui.addComponent(topBar.getModelTree());
        gui.addComponent(topBar.getTextureEditor());
        gui.addComponent(topBar.getGroupEditor());
        ModelTree.INSTANCE.init();
    }

    public void update() {
        GLFWDisplay.set2D();
        TextureStorage.EMPTY.bind();
        if (InputHandler.isMouseButtonPress(InputHandler.MouseButton.MIDDLE) || InputHandler.isMouseButtonPress(InputHandler.MouseButton.RIGHT)) {
            drawCenter();
        }
        gui.render();
    }

    private void drawCenter() {
        float size = 50;
        TextureStorage.CENTER.bind();
        engine.startDrawing(GL_QUADS);
        engine.setColorOpaque(0xFFFFFF);
        engine.addTextureUV(0, 0);
        engine.addVertex(center.getX() - size / 2, center.getY() - size / 2, 0);
        engine.addTextureUV(0, 1);
        engine.addVertex(center.getX() - size / 2, center.getY() + size / 2, 0);
        engine.addTextureUV(1, 1);
        engine.addVertex(center.getX() + size / 2, center.getY() + size / 2, 0);
        engine.addTextureUV(1, 0);
        engine.addVertex(center.getX() + size / 2, center.getY() - size / 2, 0);
        engine.endDrawing();
    }

    public void onResize() {
        size = GLFWDisplay.getFrameBufferSize().copy();
        center = GLFWDisplay.getFrameBufferSize().copy().division(2);
        gui.onResize();
    }

    @Override
    public void onKeyPress(int key, int code, int action) {
    }

    public Vect2i getScreenSize() {
        return size.copy();
    }

    public Gui getGui() {
        return gui;
    }
}
