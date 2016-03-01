package com.cout970.editor;

import com.cout970.editor.model.IModel;
import com.cout970.editor.model.TechneCube;
import com.cout970.editor.render.texture.TextureStorage;
import com.cout970.editor.util.Vect2d;
import com.cout970.editor.util.Vect3d;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cout970 on 11/02/2016.
 */
public class ModelTree {

    public static final ModelTree INSTANCE = new ModelTree();
    private IModel modelBase;
    private List<IModel> models = new LinkedList<>();
    private List<IModel> selectedModels = new LinkedList<>();

    private ModelTree() {
    }

    public List<IModel> getModelsToRender() {
        List<IModel> list = new ArrayList<>(models.size() + 1);
        models.stream().filter(IModel::isVisible).forEach(list::add);
        list.add(modelBase);
        return list;
    }

    public void addModel(IModel m) {
        models.add(m);
    }

    public List<IModel> getSelectedModels() {
        return selectedModels;
    }

    public void addModelToSelection(IModel m) {
        selectedModels.add(m);
    }

    public void removeModelFromSelection(IModel m) {
        selectedModels.remove(m);
    }

    public void clearSelection() {
        selectedModels.clear();
    }

    public void init() {
        modelBase = new TechneCube("base", new Vect3d(0, -1, 0), new Vect3d(16, 16, 16), TextureStorage.CUBE, new Vect2d(0, 0), 16);
    }

    public List<IModel> getAllVisibleModels() {
        List<IModel> list = new ArrayList<>(models.size() + 1);
        list.addAll(models);
        return list;
    }

    public int getNumberOfModels() {
        return models.size();
    }
}
