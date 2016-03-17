package com.cout970.editor2.render.texture;

public interface ITexture {

    String getTextureName();

    int getTextureSizeX();

    int getTextureSizeY();

    int getTextureID();

    void bind();
}
