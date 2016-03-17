package com.cout970.editor2.render.texture;

import com.cout970.editor2.render.texture.atlas.ITextureAtlas;

import java.nio.ByteBuffer;

public interface ITextureLoader {

    ITexture getTexture(ResourceReference path);

    ITexture loadTexture(ResourceReference resourceFile, String textureName);

    ITexture loadTexture(ByteBuffer imageBuffer, String textureName);

    ITextureAtlas generateTextureAtlas(ResourceReference folderPath, String name);
}
