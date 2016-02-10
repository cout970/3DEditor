package com.cout970.editor.render.texture;

public class TextureStorage {

	public static final TextureStorage INSTANCE = new TextureStorage();
	public static ITexture MISSING_TEXTURE;
	public static ITexture LIGHT_MAP;

	private TextureStorage() {}

	public void reloadTextures(String domain) {
		MISSING_TEXTURE = TextureManager.INSTANCE.loadTexture(new ResourceFile(domain, "textures/std/missing.png"),
				"missing_texture");
		LIGHT_MAP = TextureManager.INSTANCE.loadTexture(new ResourceFile(domain, "textures/std/light.png"),
				"light_map");
	}
}
