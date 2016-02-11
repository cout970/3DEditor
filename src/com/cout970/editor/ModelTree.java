package com.cout970.editor;

import com.cout970.editor.model.IModel;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by cout970 on 11/02/2016.
 */
public class ModelTree {

    private List<IModel> models = new LinkedList<>();

    public List<IModel> getAllModels() {
        return models;
    }

    public void addModel(IModel m) {
        models.add(m);
    }
}
