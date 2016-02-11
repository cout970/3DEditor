package com.cout970.editor;

import com.cout970.editor.gui.Component;
import com.cout970.editor.gui.DynamicPanel;
import com.cout970.editor.model.TechneCube;
import com.cout970.editor.render.engine.IRenderEngine;
import com.cout970.editor.render.texture.TextureStorage;
import com.cout970.editor.util.Color;
import com.cout970.editor.util.Vect2d;
import com.cout970.editor.util.Vect2i;
import com.cout970.editor.util.Vect3d;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.opengl.GL11.GL_QUADS;

/**
 * Created by cout970 on 10/02/2016.
 */
public class Handler2D implements InputHandler.IKeyboardCallback {

    private ConfigurationFile config = ConfigurationFile.INSTANCE;
    private IRenderEngine engine = IRenderEngine.INSTANCE;
    private Vect2i size = GLFWDisplay.getFrameBufferSize().copy();
    private Vect2i center = GLFWDisplay.getFrameBufferSize().copy().division(2);
    private ModelTree modelTree;
    private Component panel;

    public void init(){
        panel = new DynamicPanel(Vect2d.nullVector(), new Vect2d(20,100), new Color(0x666666));
    }

    public void update() {
        GLFWDisplay.set2D();
        TextureStorage.EMPTY.bind();
        if (InputHandler.isMouseButtonPress(InputHandler.MouseButton.MIDDLE) || InputHandler.isMouseButtonPress(InputHandler.MouseButton.RIGHT)) {
            drawCenter();
        }
        panel.updateAndRender();
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
    }

    public void setModelTree(ModelTree modelTree) {
        this.modelTree = modelTree;
    }

    @Override
    public void onKeyPress(int key, int action) {
        if (action == 1) {
            switch (key) {
                case GLFW_KEY_A:
                    modelTree.addModel(new TechneCube("base", new Vect3d(0, -1, 0), new Vect3d(16, 16, 16), TextureStorage.CUBE, new Vect2d(0, 0), 16));
            }
        }
    }

    public Vect2i getScreenSize() {
        return size.copy();
    }
}
