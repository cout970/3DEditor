package com.cout970.editor;

import com.cout970.editor.display.GLFWDisplay;
import com.cout970.editor.export.SaveHandler;
import com.cout970.editor.tools.Project;

import java.io.File;

/**
 * Created by cout970 on 10/02/2016.
 */
public class Editor {

    public static final String EDITOR_NAME = "Editor";
    public static final String EDITOR_VERSION = "0.0.0";
    private static Project project;

    public static void main(String[] args) {
        GLFWDisplay.init();
        project = SaveHandler.INSTANCE.load(new File("./modelCrusherLeft.tcn"));
        //TODO test and finish the save function
//        SaveHandler.INSTANCE.save(new File("./test.tcn"), project);
//        project = SaveHandler.INSTANCE.load(new File("./test.tcn"));
        GLFWDisplay.run();
    }

    public static Project getProject() {
        return project;
    }
}
