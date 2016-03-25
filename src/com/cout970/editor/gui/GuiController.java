package com.cout970.editor.gui;

import com.cout970.editor.Editor;
import com.cout970.editor.export.SaveHandler;
import com.cout970.editor.model.IModel;
import com.cout970.editor.model.TechneCube;
import com.cout970.editor.render.texture.TextureStorage;
import com.cout970.editor.tools.Project;
import com.cout970.editor.util.Vect2d;
import com.cout970.editor.util.Vect3d;

import java.io.File;

/**
 * Created by cout970 on 23/03/2016.
 */
public class GuiController {

    public static final GuiController INSTANCE = new GuiController();
    private File lastSaveFile;

    public void buttonNewProject(){
        WindowPopupHandler.INSTANCE.showSaveCurrentProjectPopup();
        WindowPopupHandler.INSTANCE.showNewProjectConfigPopup();
    }

    public void buttonLoadProject() {
        WindowPopupHandler.INSTANCE.showSaveCurrentProjectPopup();
        lastSaveFile = WindowPopupHandler.INSTANCE.showLoadProjectPopup();
        Project p = SaveHandler.INSTANCE.load(lastSaveFile);
        Editor.setProject(p);
    }

    public void buttonSaveProject() {
        if (lastSaveFile == null){
            lastSaveFile = WindowPopupHandler.INSTANCE.showSaveProjectFileSelector();
        }
        if (lastSaveFile == null){
            WindowPopupHandler.INSTANCE.showErrorPopup("Invalid save location");
        }else {
            SaveHandler.INSTANCE.save(lastSaveFile, Editor.getProject());
        }
    }

    public void buttonAddCube() {
        Editor.getProject().getModelTree().clearSelection();
        IModel m = new TechneCube("Shape" + Editor.getProject().getModelTree().getNumberOfModels(), new Vect3d(0, 0, 0), new Vect3d(1, 1, 1), new Vect2d(0, 0), 16);
        Editor.getProject().getModelTree().addModel(m);
        Editor.getProject().getModelTree().addModelToSelection(m);
    }

    public void buttonSaveAsProject() {

    }

    public void buttonLoadTexture() {
        File texture = WindowPopupHandler.INSTANCE.showLoadTexturePopup();
        if (texture != null){
            TextureStorage.loadModelTexture(texture);

        }
    }
}
