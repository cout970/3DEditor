package com.cout970.editor;

import com.cout970.editor.display.Display;
import com.cout970.editor.display.GuiHandler;
import com.cout970.editor.display.EditorHandler;
import com.cout970.editor.render.texture.TextureStorage;
import com.cout970.editor.tools.Project;

/**
 * Created by cout970 on 10/02/2016.
 */
public class Editor {

    public static final String EDITOR_NAME = "Editor";
    public static final String EDITOR_VERSION = "0.0.0";
    //proyecto sobre el que se trabaja
    private static Project project;
    //handlers
    public static GuiHandler guiHandler;
    public static EditorHandler editorHandler;

    public static void main(String[] args) {
        Display.initWindow();
        Display.initOpenGL();
        TextureStorage.initTextures();

        //se incian los objetos que renderizaran la interfaz y la escena del editor
        guiHandler = new GuiHandler();
        editorHandler = new EditorHandler();
        guiHandler.init();
        editorHandler.init();

        //se crea un projecto vacio
        project = new Project("", "cout970", "", "", "", "", "", new ModelTree(), 32);
        project.init();

        //se ejecuta el loop de renderizado
        Display.runLoop();

        //se cierra con exit para eliminar las ventanas de carga y
        //guardado de archivos que son parte de java y no de openGL
        System.exit(0);
    }

    public static Project getProject() {
        return project;
    }

    public static void setProject(Project p) {
        project = p;
    }

    public static GuiHandler getGuiHandler() {
        return guiHandler;
    }

    public static EditorHandler getEditorHandler() {
        return editorHandler;
    }
}