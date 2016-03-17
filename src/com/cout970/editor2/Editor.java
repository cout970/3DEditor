package com.cout970.editor2;

import com.cout970.editor2.display.GLFWDisplay;
import com.cout970.editor2.tools.Project;

/**
 * Created by cout970 on 10/02/2016.
 */
public class Editor {

    public static final String EDITOR_NAME = "Editor";
    public static final String EDITOR_VERSION = "0.0.0";
    private static Project project;

    public static void main(String[] args) {
        GLFWDisplay.init();
//        project = SaveHandler.INSTANCE.load(new File("./modelCrusherLeft.tcn"));
//        //TODO test and finish the save function
//        SaveHandler.INSTANCE.save(new File("./test.tcn"), project);
//        project = SaveHandler.INSTANCE.load(new File("./test.tcn"));
        project = new Project("", "cout970", "", "", "", "", "", new ModelTree());
        project.init();
        GLFWDisplay.run();
    }

    public static Project getProject() {
        return project;
    }
}