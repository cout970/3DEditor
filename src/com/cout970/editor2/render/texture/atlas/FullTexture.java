package com.cout970.editor2.render.texture.atlas;

import com.cout970.editor2.render.texture.ITexture;

public class FullTexture implements ITextureSprite {

    public ITexture texture;

    public FullTexture(ITexture tex) {
        texture = tex;
    }

    @Override
    public ITexture getParent() {
        return texture;
    }

    @Override
    public int getTextureSizeX() {
        return texture.getTextureSizeX();
    }

    @Override
    public int getTextureSizeY() {
        return texture.getTextureSizeY();
    }

    @Override
    public double getTextureU(float offset) {
        return offset;
    }

    @Override
    public double getTextureV(float offset) {
        return offset;
    }

}
