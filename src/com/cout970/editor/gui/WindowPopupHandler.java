package com.cout970.editor.gui;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created by cout970 on 23/03/2016.
 */
public class WindowPopupHandler {

    public static final WindowPopupHandler INSTANCE = new WindowPopupHandler();

    private WindowPopupHandler(){}

    public void showSaveCurrentProjectPopup() {
    //TODO
    }

    public void showNewProjectConfigPopup() {
    //TODO
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

    public File showLoadTexturePopup() {
        JFileChooser chooser = new JFileChooser(new File("./"));

        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".png");
            }

            @Override
            public String getDescription() {
                return ".png Files";
            }
        });
        chooser.showOpenDialog(new JFrame());
        File f = chooser.getSelectedFile();
        if (f == null || !f.exists() || !f.getName().endsWith(".png")){
            return null;
        }
        return f;
    }
}
