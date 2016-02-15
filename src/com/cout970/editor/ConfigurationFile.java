package com.cout970.editor;

/**
 * Created by cout970 on 10/02/2016.
 */
public class ConfigurationFile {

    public static final ConfigurationFile INSTANCE = new ConfigurationFile();

    public boolean showAxisGridY = true;
    public boolean showAxisGridX = false;
    public boolean showAxisGridZ = false;
    public double translationSpeedY = 0.3125D;
    public double translationSpeedX = 0.3125D;
    public double rotationSpeedX = 20D;
    public double rotationSpeedY = 20D;
    public int camaraController = 0;

    private ConfigurationFile() {
    }
}
