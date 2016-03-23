package com.cout970.editor.display;

import com.cout970.editor.ConfigurationFile;
import com.cout970.editor.gui.Gui;
import com.cout970.editor.gui.TopBar;
import com.cout970.editor.render.texture.TextureStorage;
import com.cout970.editor.util.Vect2i;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by cout970 on 10/02/2016.
 */
public class GuiHandler implements InputHandler.IKeyboardCallback {

    private ConfigurationFile config = ConfigurationFile.INSTANCE;
    private Vect2i size = Display.getFrameBufferSize().copy();
    private Vect2i center = Display.getFrameBufferSize().copy().division(2);
    private Gui gui;

    public void init() {
        gui = new Gui();
        TopBar topBar;
        gui.addComponent(topBar = new TopBar());
        topBar.init(gui);
        InputHandler.registerKeyboardCallback(this);
        gui.addComponent(topBar.getCubeEditor());
//        gui.addComponent(topBar.getModelTree());
//        gui.addComponent(topBar.getTextureEditor());
//        gui.addComponent(topBar.getGroupEditor());
    }

    public void update() {
        Display.setGuiProjection();
        TextureStorage.EMPTY.bind();
        if (InputHandler.isMouseButtonPress(InputHandler.MouseButton.MIDDLE) || InputHandler.isMouseButtonPress(InputHandler.MouseButton.RIGHT)) {
            drawCenter();
        }
        size = Display.getFrameBufferSize().copy();
        center = Display.getFrameBufferSize().copy().division(2);
        gui.render();
    }

    private void drawCenter() {
        float size = 50;
        TextureStorage.CENTER.bind();
        glBegin(GL_QUADS);
        glColor4f(1, 1, 1, 1);
        glTexCoord2d(0,0);
        glVertex3d(center.getX() - size / 2, center.getY() - size / 2, 0);
        glTexCoord2d(0,1);
        glVertex3d(center.getX() - size / 2, center.getY() + size / 2, 0);
        glTexCoord2d(1,1);
        glVertex3d(center.getX() + size / 2, center.getY() + size / 2, 0);
        glTexCoord2d(1,0);
        glVertex3d(center.getX() + size / 2, center.getY() - size / 2, 0);
        glEnd();
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
