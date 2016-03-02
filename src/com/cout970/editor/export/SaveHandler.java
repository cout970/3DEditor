package com.cout970.editor.export;

import com.cout970.editor.tools.Project;

import java.io.File;

/**
 * Created by cout970 on 01/03/2016.
 */
public class SaveHandler implements ISaveHandler {

    public static final SaveHandler INSTANCE = new SaveHandler();
    private TechneSaveHandler techneSaveHandler;

    public SaveHandler() {
        techneSaveHandler = new TechneSaveHandler();
    }

    @Override
    public void save(File file, Project models) {
        techneSaveHandler.save(file, models);
    }

    @Override
    public Project load(File file) {
        return techneSaveHandler.load(file);
    }
}
