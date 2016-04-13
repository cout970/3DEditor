package com.cout970.editor.gui;

import com.cout970.editor.Editor;
import com.cout970.editor.ModelTree;
import com.cout970.editor.export.SaveHandler;
import com.cout970.editor.model.IModel;
import com.cout970.editor.model.TechneCube;
import com.cout970.editor.render.texture.ExternalResourceReference;
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

    public void buttonNewProject() {
        if(WindowPopupHandler.INSTANCE.showSaveCurrentProjectPopup()) {
            WindowPopupHandler.INSTANCE.showNewProjectConfigPopup();
            Project project = new Project("", "cout970", "", "", "", "", "", new ModelTree(), TextureStorage.MODEL_TEXTURE.getTextureSizeX());
            project.init();
            Editor.setProject(project);
        }
    }

    public void buttonLoadProject() {
        if(WindowPopupHandler.INSTANCE.showSaveCurrentProjectPopup()) {
            lastSaveFile = WindowPopupHandler.INSTANCE.showLoadProjectPopup();
            if (lastSaveFile != null) {
                Project p = SaveHandler.INSTANCE.load(lastSaveFile);
                if (p != null) {
                    Editor.setProject(p);
                } else {
                    WindowPopupHandler.INSTANCE.showErrorPopup("Error trying to load a project in: " + lastSaveFile.getAbsolutePath());
                }
            }
        }
    }

    public void buttonSaveProject() {
        if (lastSaveFile == null || !lastSaveFile.getName().endsWith(".tcn")) {
            lastSaveFile = WindowPopupHandler.INSTANCE.showSaveProjectFileSelector();
        }
        if (lastSaveFile != null) {
            SaveHandler.INSTANCE.save(lastSaveFile, Editor.getProject());
        }
    }

    public void buttonSaveAsProject() {
        lastSaveFile = WindowPopupHandler.INSTANCE.showSaveProjectFileSelector();

        if (lastSaveFile != null) {
            if(lastSaveFile.getName().endsWith(".obj")){
                SaveHandler.INSTANCE.export("obj", lastSaveFile, Editor.getProject());
            }else if(lastSaveFile.getName().endsWith(".json")){
                SaveHandler.INSTANCE.export("json", lastSaveFile, Editor.getProject());
            }else {
                SaveHandler.INSTANCE.save(lastSaveFile, Editor.getProject());
            }
        }
    }

    public void buttonAddCube() {
        Editor.getProject().getModelTree().clearSelection();
        IModel m = new TechneCube("Shape" + Editor.getProject().getModelTree().getNumberOfModels(), new Vect3d(0, 0, 0), new Vect3d(1, 1, 1), new Vect2d(0, 0));
        Editor.getProject().getModelTree().addModel(m);
        Editor.getProject().getModelTree().addModelToSelection(m);
    }


    public void buttonLoadTexture() {
        File texture = WindowPopupHandler.INSTANCE.showLoadTexturePopup();
        if (texture != null) {
            TextureStorage.loadModelTexture(new ExternalResourceReference(texture));
            Editor.getProject().setTextureSize(TextureStorage.MODEL_TEXTURE.getTextureSizeX());
        }
    }
}
