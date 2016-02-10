package com.cout970.editor.render.texture;

public interface ITexture {

	String getTextureName();
	
	int getTextureSizeX();
	int getTextureSizeY();
	
	int getTextureID();
		
	void bind();
}
