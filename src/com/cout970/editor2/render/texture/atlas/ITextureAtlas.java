package com.cout970.editor2.render.texture.atlas;

import com.cout970.editor2.render.texture.ITexture;

public interface ITextureAtlas extends ITexture {

    ITextureSprite getSprite(String name);
}
