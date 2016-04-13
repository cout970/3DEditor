package com.cout970.editor.render.texture;

/**
 * Created by cout970 on 13/04/2016.
 */
public interface ITextureReloadable extends ITexture {

    ITextureReloadable reload();

    ResourceReference getResourceReference();
}
