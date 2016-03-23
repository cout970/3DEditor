package com.cout970.editor.render.texture.atlas;

import com.cout970.editor.render.texture.ITexture;

public interface ITextureSprite {

    ITexture getParent();

    int getTextureSizeX();

    int getTextureSizeY();

    double getTextureU(float offset);//values from 0 to 1

    double getTextureV(float offset);//values from 0 to 1
}
