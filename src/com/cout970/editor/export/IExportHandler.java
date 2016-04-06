package com.cout970.editor.export;

import com.cout970.editor.tools.Project;

import java.io.File;

/**
 * Created by cout970 on 06/04/2016.
 */
public interface IExportHandler {

    boolean export(Project project, File output);

}
