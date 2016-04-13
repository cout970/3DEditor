package com.cout970.editor.render.texture;

import java.io.File;

/**
 * Created by cout970 on 13/04/2016.
 */
public class ExternalResourceReference extends ResourceReference {

    private File archivo;

    public ExternalResourceReference(File archivo) {
        super("external", archivo.getAbsolutePath(), archivo.getName());
        this.archivo = archivo;
    }

    public String toString() {
        return archivo.getAbsolutePath();
    }

    @Override
    public File getFile() {
        return archivo;
    }

    @Override
    public String getCompletePath() {
        return archivo.getAbsolutePath();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof ExternalResourceReference)) { return false; }
        if (!super.equals(o)) { return false; }

        ExternalResourceReference that = (ExternalResourceReference) o;

        return archivo != null ? archivo.equals(that.archivo) : that.archivo == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (archivo != null ? archivo.hashCode() : 0);
        return result;
    }
}
