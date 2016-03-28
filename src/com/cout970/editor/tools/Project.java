package com.cout970.editor.tools;

import com.cout970.editor.ModelTree;

/**
 * Created by cout970 on 02/03/2016.
 */
public class Project {

    private String autor;
    private String creationVersion;
    private String dataCreation;
    private String name;
    private String projectName;
    private String previewImagePath;
    private String projectType;
    private ModelTree models;
    private double textureSize;

    public Project(String creationVersion, String autor, String dataCreation, String name, String projectName, String previewImagePath, String projectType, ModelTree models, int textureSize) {
        this.creationVersion = creationVersion;
        this.autor = autor;
        this.dataCreation = dataCreation;
        this.name = name;
        this.projectName = projectName;
        this.previewImagePath = previewImagePath;
        this.projectType = projectType;
        this.models = models;
        this.textureSize = textureSize;
    }

    public String getCreationVersion() {
        return creationVersion;
    }

    public String getAutor() {
        return autor;
    }

    public String getDataCreation() {
        return dataCreation;
    }

    public String getName() {
        return name;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getPreviewImagePath() {
        return previewImagePath;
    }

    public String getProjectType() {
        return projectType;
    }

    public ModelTree getModelTree() {
        return models;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setPreviewImagePath(String previewImagePath) {
        this.previewImagePath = previewImagePath;
    }

    public void setModels(ModelTree models) {
        this.models = models;
    }

    public void init() {
        if (models == null){
            models = new ModelTree();
        }
    }

    public double getTextureSize() {
        return textureSize;
    }

    public void setTextureSize(int textureSize) {
        this.textureSize = textureSize;
    }
}
