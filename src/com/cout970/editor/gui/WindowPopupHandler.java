package com.cout970.editor.gui;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created by cout970 on 23/03/2016.
 */
public class WindowPopupHandler {

    public static final WindowPopupHandler INSTANCE = new WindowPopupHandler();

    private WindowPopupHandler() {}

    public boolean showSaveCurrentProjectPopup() {
        int option = JOptionPane.showConfirmDialog(null, "Do you want to save the current project?");//TODO add I18n
        if (option == JOptionPane.CANCEL_OPTION) {
            return false;
        }
        if (option == JOptionPane.YES_OPTION) {
            GuiController.INSTANCE.buttonSaveProject();
        }
        return true;
    }

    public void showNewProjectConfigPopup() {
        //TODO
    }

    public File showLoadProjectPopup() {
        JFileChooser chooser = new JFileChooser(new File("./"));
        int option = chooser.showOpenDialog(new JFrame());
        if (option == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    public File showSaveProjectFileSelector() {
        JFileChooser chooser = new JFileChooser(new File("./"));
        chooser.setFileFilter(new Filter(0) {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".tcn");
            }

            @Override
            public String getDescription() {
                return "Techne model format .tcn";
            }
        });
        chooser.setFileFilter(new Filter(1) {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".obj");
            }

            @Override
            public String getDescription() {
                return "OBJ model format .obj";
            }
        });
        chooser.setFileFilter(new Filter(2) {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".json");
            }

            @Override
            public String getDescription() {
                return "Minecraft json model format .json";
            }
        });
        int option = chooser.showSaveDialog(new JFrame());
        if (option == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            switch (((Filter) chooser.getFileFilter()).getId()) {
                case 1://obj
                    if (!f.getName().endsWith(".obj")) {
                        return new File(f.getAbsolutePath() + ".obj");
                    }
                    return f;
                case 2://json
                    if (!f.getName().endsWith(".json")) {
                        return new File(f.getAbsolutePath() + ".json");
                    }
                    return f;
                default://tcn
                    if (!f.getName().endsWith(".tcn")) {
                        return new File(f.getAbsolutePath() + ".tcn");
                    }
                    return f;
            }
        }
        return null;
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
                return "Images PNG .png";
            }
        });
        int option = chooser.showOpenDialog(new JFrame());
        if (option == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            if (f != null && f.exists() && f.getName().endsWith(".png")) {
                return f;
            }
        }
        return null;
    }

    private abstract class Filter extends FileFilter {

        private int id;

        public Filter(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
