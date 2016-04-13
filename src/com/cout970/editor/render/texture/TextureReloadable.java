package com.cout970.editor.render.texture;

/**
 * Created by cout970 on 13/04/2016.
 */
public class TextureReloadable extends TextureGL implements ITextureReloadable {

    private ResourceReference resource;

    public TextureReloadable(int texId, String name, int sizeX, int sizeY, ResourceReference res) {
        super(texId, name, sizeX, sizeY);
        resource = res;
    }

    public TextureReloadable(ITexture tex, ResourceReference ref) {
        this(tex.getTextureID(), tex.getTextureName(), tex.getTextureSizeX(), tex.getTextureSizeY(), ref);
    }

    @Override
    public ITextureReloadable reload() {
        ITexture tex =  TextureManager.INSTANCE.loadTexture(resource, getTextureName());
        return new TextureReloadable(tex, resource);
    }

    @Override
    public ResourceReference getResourceReference() {
        return resource;
    }
}
