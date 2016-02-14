package com.cout970.editor.gui.windows;

import com.cout970.editor.gui.IGui;
import com.cout970.editor.gui.components.CubeNameTextBox;
import com.cout970.editor.gui.components.NumberEdit;
import com.cout970.editor.gui.components.RotationBar;
import com.cout970.editor.gui.components.TextBox;
import com.cout970.editor.model.TechneCube;
import com.cout970.editor.util.Color;
import com.cout970.editor.util.Vect2i;

/**
 * Created by cout970 on 12/02/2016.
 */
public class CubeEditWindow extends InternalWindow {

    private TechneCube cubeModel;

    private TextBox cubeName;

    private NumberEdit cubeSizeX;
    private NumberEdit cubeSizeY;
    private NumberEdit cubeSizeZ;

    private NumberEdit cubePosX;
    private NumberEdit cubePosY;
    private NumberEdit cubePosZ;

    private NumberEdit cubeRotPointX;
    private NumberEdit cubeRotPointY;
    private NumberEdit cubeRotPointZ;

    private NumberEdit cubeRotationX;
    private NumberEdit cubeRotationY;
    private NumberEdit cubeRotationZ;

    private RotationBar cubeRotationBarX;
    private RotationBar cubeRotationBarY;
    private RotationBar cubeRotationBarZ;

    public CubeEditWindow() {
        super(new Vect2i(238, 205));
        //name
        subParts.add(cubeName = new CubeNameTextBox(this, new Vect2i(2, 26), 234));
        //size
        subParts.add(cubeSizeX = new NumberEdit(this, new Vect2i(2, 56), false));
        subParts.add(cubeSizeY = new NumberEdit(this, new Vect2i(2 + 78, 56), false));
        subParts.add(cubeSizeZ = new NumberEdit(this, new Vect2i(2 + 78 + 78, 56), false));
        //pos
        subParts.add(cubePosX = new NumberEdit(this, new Vect2i(2, 86), true));
        subParts.add(cubePosY = new NumberEdit(this, new Vect2i(2 + 78, 86), true));
        subParts.add(cubePosZ = new NumberEdit(this, new Vect2i(2 + 78 + 78, 86), true));
        //rot pos
        subParts.add(cubeRotPointX = new NumberEdit(this, new Vect2i(2, 116), true));
        subParts.add(cubeRotPointY = new NumberEdit(this, new Vect2i(2 + 78, 116), true));
        subParts.add(cubeRotPointZ = new NumberEdit(this, new Vect2i(2 + 78 + 78, 116), true));
        //rotation
        subParts.add(cubeRotationX = new NumberEdit(this, new Vect2i(2, 146), true));
        subParts.add(cubeRotationY = new NumberEdit(this, new Vect2i(2, 166), true));
        subParts.add(cubeRotationZ = new NumberEdit(this, new Vect2i(2, 186), true));
        //rotation bar
        subParts.add(cubeRotationBarX = new RotationBar(cubeRotationX));
        subParts.add(cubeRotationBarY = new RotationBar(cubeRotationY));
        subParts.add(cubeRotationBarZ = new RotationBar(cubeRotationZ));
    }

    @Override
    public void renderBackground(IGui gui, Vect2i mouse, float partialTicks) {
        super.renderBackground(gui, mouse, partialTicks);
        if (!hide) {
            gui.getGuiRenderer().drawString("Cube Name", getPos().add(2, 16), new Color(0));
            gui.getGuiRenderer().drawString("Cube Size", getPos().add(2, 46), new Color(0));
            gui.getGuiRenderer().drawString("Cube Position", getPos().add(2, 76), new Color(0));
            gui.getGuiRenderer().drawString("Rotation Point", getPos().add(2, 106), new Color(0));
            gui.getGuiRenderer().drawString("Rotation", getPos().add(2, 136), new Color(0));

        }
    }
}
