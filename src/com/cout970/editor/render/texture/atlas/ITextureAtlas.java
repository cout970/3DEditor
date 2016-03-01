package com.cout970.editor.render.texture.atlas;

import com.cout970.editor.render.texture.ITexture;

public interface ITextureAtlas extends ITexture {

    ITextureSprite getSprite(String name);
}
