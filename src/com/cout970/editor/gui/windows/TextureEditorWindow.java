package com.cout970.editor.gui.windows;

import com.cout970.editor.Editor;
import com.cout970.editor.display.InputHandler;
import com.cout970.editor.gui.api.IGui;
import com.cout970.editor.gui.TopBar;
import com.cout970.editor.gui.components.AbstractButton;
import com.cout970.editor.gui.components.AbstractStateButton;
import com.cout970.editor.gui.components.InnerButton;
import com.cout970.editor.gui.components.SimpleButton;
import com.cout970.editor.model.TechneCube;
import com.cout970.editor.render.texture.TextureStorage;
import com.cout970.editor.util.Color;
import com.cout970.editor.util.Vect2d;
import com.cout970.editor.util.Vect2i;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by cout970 on 22/02/2016.
 */
public class TextureEditorWindow extends ResizableWindows {

    private static int HEADER_SIZE = 18;
    private int margin = 4;
    private SimpleButton buttonGrid;
    private double zoom = 0;
    private Vect2d texturePos;

    private boolean selected;
    private Vect2i oldMouse;
    private Vect2d oldTexturePos;
    private double scale;
    private double pixel;
    private boolean showGrid = true;

    public TextureEditorWindow(IGui gui) {
        super(new Vect2i(240, 248), gui);
        this.pos.set(0, 235 + TopBar.BAR_HEIGHT);
        backgroundColor = new Color(0xF0F0F0);
        buttonGrid = new InnerButton(this, new Vect2i(margin, 14), new Vect2i(16, 16), TextureStorage.BUTTONS, null, null, this::buttonListener, this::uvMapper);
        minSize = 240;
        texturePos = Vect2d.nullVector();
        subParts.add(buttonGrid);

    }

    public boolean buttonListener(AbstractButton button, Vect2i mouse, InputHandler.MouseButton mouseButton) {
        showGrid = !showGrid;
        return false;
    }

    public Vect2i uvMapper(AbstractStateButton.ButtonState t) {
        if (t == AbstractStateButton.ButtonState.ACTIVE){
            return new Vect2i(112, 24);
        }
        return new Vect2i(96, 24);
    }

    public Vect2i getBoxPos() {
        return getPos().add(margin, 10 + margin + HEADER_SIZE);
    }

    public Vect2i getBoxSize() {
        return getSize().sub(margin * 2, 10 + margin * 2 + HEADER_SIZE);
    }

    @Override
    public void setInternalSize(Vect2i s) {
        setSize(s.copy().add(0, 10 + HEADER_SIZE));
    }

    @Override
    public Vect2i getInternalSize() {
        return getSize().sub(0, 10 + HEADER_SIZE);
    }

    @Override
    public void onMouseClick(IGui gui, Vect2i mouse, InputHandler.MouseButton button) {
        super.onMouseClick(gui, mouse.copy(), button);
        if (!hide) {
            if (button == InputHandler.MouseButton.LEFT) {
                if (IGui.isInside(mouse, getBoxPos(), getBoxSize())) {
                    selected = true;
                    oldMouse = mouse.copy();
                    oldTexturePos = texturePos.copy();
                    return;
                }
            }

        }
        selected = false;
    }

    @Override
    public void onWheelMoves(IGui gui, double amount) {
        if (hide) { return; }
        if (IGui.isInside(InputHandler.getCursorPos().toVect2i(), getBoxPos(), getBoxSize())) {
            zoom += amount / 20d;
            if (zoom > 1) { zoom = 1; }
            if (zoom < 0) { zoom = 0; }
        }
    }

    @Override
    public void renderBackground(IGui gui, Vect2i mouse, float partialTicks) {
        super.renderBackground(gui, mouse, partialTicks);
        if (!hide) {

            //the model texture
            scale = 1 + zoom * 8;
            pixel = getBoxSize().getX() / Editor.getProject().getTextureSize();

            if (!gui.isButtonDown(InputHandler.MouseButton.LEFT)) {
                selected = false;
            }
            if (selected) {
                texturePos = mouse.copy().sub(oldMouse).toVect2d().multiply(-pixel / (scale * getBoxSize().getX())).add(oldTexturePos);
            }

            texturePos.set(Math.max(texturePos.getX(), 0), Math.max(texturePos.getY(), 0));
            texturePos.set(Math.min(texturePos.getX(), 1 - 1 / scale), Math.min(texturePos.getY(), 1 - 1 / scale));

            glColor4f(1f, 1f, 1f, 1f);
            if (showGrid) {
                for (int i = 0; i < Editor.getProject().getTextureSize(); i++) {
                    Vect2d size = getBoxSize().toVect2d();
                    int disp = (int) (i * pixel * scale - texturePos.getY() * pixel * Editor.getProject().getTextureSize() * scale);
                    drawRectangle(getBoxPos().add(0, disp),
                            getBoxPos().add(getBoxSize().getX(), disp + 1), new Color(0));
                }

                for (int i = 0; i < Editor.getProject().getTextureSize(); i++) {
                    Vect2d size = getBoxSize().toVect2d();
                    int disp = (int) (i * pixel * scale - texturePos.getX() * pixel * Editor.getProject().getTextureSize() * scale);
                    drawRectangle(getBoxPos().add(disp, 0),
                            getBoxPos().add(disp + 1, getBoxSize().getY()), new Color(0));
                }
            }
            glColor4f(1f, 1f, 1f, 1f);

            TextureStorage.MODEL_TEXTURE.bind();
            glEnable(GL_ALPHA_TEST);
            glAlphaFunc(GL_GREATER, 0.9f);

            Vect2d start = Vect2d.nullVector();
            Vect2d end = new Vect2d(1 / scale, 1 / scale);

            start.add(texturePos);
            end.add(texturePos);

            gui.getGuiRenderer().drawRectangleWithTextureUV(getBoxPos(), getBoxSize(), start, end);

            glDisable(GL_ALPHA_TEST);
            Editor.getProject().getModelTree().getAllVisibleModels().stream().filter(iModel -> iModel instanceof TechneCube).map(iModel1 -> (TechneCube) iModel1).forEach(cube -> {

                double offsetX = cube.getTextureOffset().getX() * pixel;
                double offsetY = cube.getTextureOffset().getY() * pixel;

                double width = cube.getSize().getX() * pixel;
                double height = cube.getSize().getY() * pixel;
                double length = cube.getSize().getZ() * pixel;

                drawOverlay(
                        new Vect2d(offsetX + length + width, offsetY + length),
                        new Vect2d(offsetX + length + width + length, offsetY + length + height), new Color(1f, 0f, 0f));

                drawOverlay(
                        new Vect2d(offsetX, offsetY + length),
                        new Vect2d(offsetX + length, offsetY + length + height),
                        new Color(0.5f, 0f, 0f));

                drawOverlay(
                        new Vect2d(offsetX + length, offsetY),
                        new Vect2d(offsetX + length + width, offsetY + length),
                        new Color(0f, 0.8f, 0f));

                drawOverlay(
                        new Vect2d(offsetX + length + width + width, offsetY),
                        new Vect2d(offsetX + length + width, offsetY + length),
                        new Color(0f, 0.9f, 0f));

                drawOverlay(
                        new Vect2d(offsetX + length, offsetY + length),
                        new Vect2d(offsetX + length + width, offsetY + length + height),
                        new Color(0f, 0f, 0.8f));

                drawOverlay(
                        new Vect2d(offsetX + length + width + length, offsetY + length),
                        new Vect2d(offsetX + length + width + length + width, offsetY + length + height),
                        new Color(0f, 0f, 1f));
            });
        }
    }

    private void drawOverlay(Vect2d start, Vect2d end, Color color) {
        Vect2d base = getBoxPos().toVect2d();
        start.sub(texturePos.getX() * pixel * Editor.getProject().getTextureSize(), texturePos.getY() * pixel * Editor.getProject().getTextureSize()).multiply(scale).add(base);
        end.sub(texturePos.getX() * pixel * Editor.getProject().getTextureSize(), texturePos.getY() * pixel * Editor.getProject().getTextureSize()).multiply(scale).add(base);
        drawRectangle(start.toVect2i(), end.toVect2i(), color);
    }

    private void drawRectangle(Vect2i start, Vect2i end, Color color) {
        if (start.getX() > getBoxPos().getX() + getBoxSize().getX()) { return; }
        if (start.getY() > getBoxPos().getY() + getBoxSize().getY()) { return; }
        if (end.getX() < getBoxPos().getX()) { return; }
        if (end.getY() < getBoxPos().getY()) { return; }
        glColor4f(color.getR(), color.getG(), color.getB(), 0.5f);
        gui.getGuiRenderer().drawRectangle(
                new Vect2i(Math.max(getBoxPos().getX(), start.getX()), Math.max(getBoxPos().getY(), start.getY())),
                new Vect2i(Math.min(getBoxPos().add(getBoxSize()).getX(), end.getX()), Math.min(getBoxPos().add(getBoxSize()).getY(), end.getY())));
    }
}
