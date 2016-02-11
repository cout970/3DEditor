package com.cout970.editor.render.texture;

public class TextureStorage {

	public static final TextureStorage INSTANCE = new TextureStorage();
	public static ITexture EMPTY;
	public static ITexture MISSING_TEXTURE;
	public static ITexture CUBE;
	public static ITexture CENTER;
	private TextureStorage() {}

	public void reloadTextures(String domain) {
		EMPTY = TextureManager.INSTANCE.loadTexture(new ResourceReference(domain, "textures/empty.png"),
				"empty");
		MISSING_TEXTURE = TextureManager.INSTANCE.loadTexture(new ResourceReference(domain, "textures/missing.png"),
				"missing_texture");
		CUBE = TextureManager.INSTANCE.loadTexture(new ResourceReference(domain, "textures/cube.png"),
				"cube");
		CENTER = TextureManager.INSTANCE.loadTexture(new ResourceReference(domain, "textures/center.png"),
				"center");
	}
}
