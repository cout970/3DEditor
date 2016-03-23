package com.cout970.editor.gui.windows;

import com.cout970.editor.Editor;
import com.cout970.editor.gui.IGui;
import com.cout970.editor.gui.components.*;
import com.cout970.editor.model.IModel;
import com.cout970.editor.model.TechneCube;
import com.cout970.editor.util.Color;
import com.cout970.editor.util.Vect2i;
import com.cout970.editor.util.Vect3d;

/**
 * Created by cout970 on 12/02/2016.
 */
public class CubeEditWindow extends InternalWindow {

    private TechneCube cubeModel;
    private static final double pixel = 0.0625d;

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

    public CubeEditWindow(IGui gui) {
        super(new Vect2i(238, 205), gui);
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

            if (Editor.getProject().getModelTree().getSelectedModels().size() == 1 && cubeModel != Editor.getProject().getModelTree().getSelectedModels().get(0)) {
                loadModel(Editor.getProject().getModelTree().getSelectedModels().get(0));
            } else if (Editor.getProject().getModelTree().getSelectedModels().size() != 1 || cubeModel != Editor.getProject().getModelTree().getSelectedModels().get(0)) {
                cubeModel = null;
            }

            if (cubeModel == null) {
                subParts.stream().filter(i -> i instanceof ILockable).map(i -> (ILockable) i).forEach(i -> i.setLocked(true));
            } else {
                subParts.stream().filter(i -> i instanceof ILockable).map(i -> (ILockable) i).forEach(i -> i.setLocked(false));
                updateModel(cubeModel);
            }
        }
    }

    private void updateModel(TechneCube m) {
        if (cubeName.hasChanged()) {
            cubeName.resetChanges();
            m.setName(cubeName.getBuffer());
        }
        if (cubePosX.hasChanged() || cubePosY.hasChanged() || cubePosZ.hasChanged()) {
            cubePosX.resetChanges();
            cubePosY.resetChanges();
            cubePosZ.resetChanges();
            m.setPos(new Vect3d(cubePosX.getValue() * pixel, cubePosY.getValue() * pixel, cubePosZ.getValue() * pixel));
        }
        if (cubeSizeX.hasChanged() || cubeSizeY.hasChanged() || cubeSizeZ.hasChanged()) {
            cubeSizeX.resetChanges();
            cubeSizeY.resetChanges();
            cubeSizeZ.resetChanges();
            m.setSize(new Vect3d(cubeSizeX.getValue(), cubeSizeY.getValue(), cubeSizeZ.getValue()));
        }
        if (cubeRotationX.hasChanged() || cubeRotationY.hasChanged() || cubeRotationZ.hasChanged()) {
            cubeRotationX.resetChanges();
            cubeRotationY.resetChanges();
            cubeRotationZ.resetChanges();
            m.setRotation(new Vect3d(Math.toRadians(cubeRotationX.getValue()), Math.toRadians(cubeRotationY.getValue()), Math.toRadians(cubeRotationZ.getValue())));
        }
        if (cubeRotPointX.hasChanged() || cubeRotPointY.hasChanged() || cubeRotPointZ.hasChanged()) {
            cubeRotPointX.resetChanges();
            cubeRotPointY.resetChanges();
            cubeRotPointZ.resetChanges();
            m.setRotationPoint(new Vect3d(cubeRotPointX.getValue() * pixel, cubeRotPointY.getValue() * pixel, cubeRotPointZ.getValue() * pixel));
        }
    }

    private void loadModel(IModel iModel) {
        if (iModel instanceof TechneCube) {
            TechneCube m = (TechneCube) iModel;
            cubeModel = m;
            cubeName.setBuffer(m.getName());
            cubeName.resetChanges();

            cubePosX.setValue(m.getPos().getX() / pixel);
            cubePosY.setValue(m.getPos().getY() / pixel);
            cubePosZ.setValue(m.getPos().getZ() / pixel);
            cubePosX.resetChanges();
            cubePosY.resetChanges();
            cubePosZ.resetChanges();

            cubeSizeX.setValue(m.getSize().getX());
            cubeSizeY.setValue(m.getSize().getY());
            cubeSizeZ.setValue(m.getSize().getZ());
            cubeSizeX.resetChanges();
            cubeSizeY.resetChanges();
            cubeSizeZ.resetChanges();

            cubeRotationX.setValue(Math.toDegrees(m.getRotation().getX()));
            cubeRotationY.setValue(Math.toDegrees(m.getRotation().getY()));
            cubeRotationZ.setValue(Math.toDegrees(m.getRotation().getZ()));
            cubeRotationX.resetChanges();
            cubeRotationY.resetChanges();
            cubeRotationZ.resetChanges();

            cubeRotPointX.setValue(m.getRotationPoint().getX() / pixel);
            cubeRotPointY.setValue(m.getRotationPoint().getY() / pixel);
            cubeRotPointZ.setValue(m.getRotationPoint().getZ() / pixel);
            cubeRotPointX.resetChanges();
            cubeRotPointY.resetChanges();
            cubeRotPointZ.resetChanges();
        }
    }
}
