package com.cout970.editor;

import com.cout970.editor.model.IModel;
import com.cout970.editor.model.TechneCube;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cout970 on 11/02/2016.
 */
public class ModelTree {

    private List<IModel> models = new LinkedList<>();
    private List<IModel> selectedModels = new LinkedList<>();

    public ModelTree() {
    }

    public ModelTree(List<TechneCube> models) {
        this.models.addAll(models);
    }

    public List<IModel> getModelsToRender() {
        List<IModel> list = new ArrayList<>(models.size() + 1);
        models.stream().filter(IModel::isVisible).forEach(list::add);
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

    public List<IModel> getAllVisibleModels() {
        List<IModel> list = new ArrayList<>(models.size() + 1);
        list.addAll(models);
        return list;
    }

    public int getNumberOfModels() {
        return models.size();
    }

    public List<IModel> getAllModels() {
        List<IModel> list = new ArrayList<>(models.size());
        list.addAll(models);
        return list;
    }

    public void removeModels(List<IModel> list) {
        models.removeAll(list);
    }
}
