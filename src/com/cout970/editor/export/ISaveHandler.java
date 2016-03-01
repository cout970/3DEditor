package com.cout970.editor.export;

import com.cout970.editor.ModelTree;

import java.io.File;

/**
 * Created by cout970 on 01/03/2016.
 */
public interface ISaveHandler {

    void save(File file, ModelTree models);

    ModelTree load(File file);
}
