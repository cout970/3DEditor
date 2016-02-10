package com.cout970.editor.render.texture;

import com.cout970.editor.render.texture.atlas.ITextureAtlas;

import java.nio.ByteBuffer;

public interface ITextureLoader {

	ITexture getTexture(ResourceFile path);
	
	ITexture loadTexture(ResourceFile resourceFile, String textureName);
	
	ITexture loadTexture(ByteBuffer imageBuffer, String textureName);
	
	ITextureAtlas generateTextureAtlas(ResourceFile folderPath, String name);
}
