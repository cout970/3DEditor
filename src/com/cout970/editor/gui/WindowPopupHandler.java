package com.cout970.editor.gui;

import javax.swing.*;
import java.io.File;

/**
 * Created by cout970 on 23/03/2016.
 */
public class WindowPopupHandler {

    public static final WindowPopupHandler INSTANCE = new WindowPopupHandler();

    private WindowPopupHandler(){}

    public void showSaveCurrentProjectPopup() {

    }

    public void showNewProjectConfigPopup() {

    }

    public File showLoadProjectPopup() {
        JFileChooser chooser = new JFileChooser(new File("./"));
        chooser.showOpenDialog(new JFrame());
        return chooser.getSelectedFile();
    }

    public File showSaveProjectFileSelector() {
        JFileChooser chooser = new JFileChooser(new File("./"));
        chooser.showSaveDialog(new JFrame());
        return chooser.getSelectedFile();
    }

    public void showErrorPopup(String s) {
        JOptionPane.showMessageDialog(null, s);
    }
}
