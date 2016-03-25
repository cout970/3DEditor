package com.cout970.editor.gui.windows;

import com.cout970.editor.Editor;
import com.cout970.editor.gui.IGui;
import com.cout970.editor.gui.TopBar;
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

    public TextureEditorWindow(IGui gui) {
        super(new Vect2i(240, 248), gui);
        this.pos.set(0, 235 + TopBar.BAR_HEIGHT);
        backgroundColor = new Color(0xF0F0F0);
    }

    @Override
    public void renderBackground(IGui gui, Vect2i mouse, float partialTicks) {
        super.renderBackground(gui, mouse, partialTicks);
        if (!hide) {
            glColor4f(1f, 1f, 1f, 1f);
            TextureStorage.MODEL_TEXTURE.bind();
            int margin = 4;
            Vect2d base = getPos().add(margin, 10 + margin).toVect2d();
            int canvasSize = getSize().getX() - margin * 2;
            glEnable(GL_ALPHA_TEST);
            glAlphaFunc(GL_GREATER, 0.9f);
            gui.getGuiRenderer().drawRectangleWithTextureUV(base.toVect2i(),
                    new Vect2i(canvasSize, canvasSize), Vect2d.nullVector(), new Vect2d(1, 1));
            glDisable(GL_ALPHA_TEST);
            Editor.getProject().getModelTree().getAllVisibleModels().stream().filter(iModel -> iModel instanceof TechneCube).map(iModel1 -> (TechneCube) iModel1).forEach(cube -> {

                double pixel = canvasSize / (double) cube.getTextureSize();

                double offsetX = cube.getTextureOffset().getX() * pixel;
                double offsetY = cube.getTextureOffset().getY() * pixel;

                double width = cube.getSize().getX() * pixel;
                double height = cube.getSize().getY() * pixel;
                double length = cube.getSize().getZ() * pixel;

                gui.getGuiRenderer().drawRectangle(
                        new Vect2i(base.copy().add(offsetX + length + width, offsetY + length).toVect2i()),
                        new Vect2i(base.copy().add(offsetX + length + width + length, offsetY + length + height).toVect2i()), new Color(1f, 0f, 0f));

                gui.getGuiRenderer().drawRectangle(
                        new Vect2i(base.copy().add(offsetX, offsetY + length).toVect2i()),
                        new Vect2i(base.copy().add(offsetX + length, offsetY + length + height).toVect2i()),
                        new Color(0.5f, 0f, 0f));

                gui.getGuiRenderer().drawRectangle(
                        new Vect2i(base.copy().add(offsetX + length, offsetY).toVect2i()),
                        new Vect2i(base.copy().add(offsetX + length + width, offsetY + length).toVect2i()),
                        new Color(0f, 0.8f, 0f));

                gui.getGuiRenderer().drawRectangle(
                        new Vect2i(base.copy().add(offsetX + length + width, offsetY + length).toVect2i()),
                        new Vect2i(base.copy().add(offsetX + length + width + width, offsetY).toVect2i()),
                        new Color(0f, 0.9f, 0f));

                gui.getGuiRenderer().drawRectangle(
                        new Vect2i(base.copy().add(offsetX + length, offsetY + length).toVect2i()),
                        new Vect2i(base.copy().add(offsetX + length + width, offsetY + length + height).toVect2i()),
                        new Color(0f, 0f, 0.8f));

                gui.getGuiRenderer().drawRectangle(
                        new Vect2i(base.copy().add(offsetX + length + width + length, offsetY + length).toVect2i()),
                        new Vect2i(base.copy().add(offsetX + length + width + length + width, offsetY + length + height).toVect2i()),
                        new Color(0f, 0f, 1f));
            });
        }
    }
}
