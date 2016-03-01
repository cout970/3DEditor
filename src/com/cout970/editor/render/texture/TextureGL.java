package com.cout970.editor.render.texture;

public class TextureGL implements ITexture {

    private String name;
    private int sizeX, sizeY;
    private int textureID;

    public TextureGL(int texId, String name, int sizeX, int sizeY) {
        this.name = name;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.textureID = texId;
    }

    @Override
    public String getTextureName() {
        return name;
    }

    @Override
    public int getTextureSizeX() {
        return sizeX;
    }

    @Override
    public int getTextureSizeY() {
        return sizeY;
    }

    @Override
    public void bind() {
        TextureManager.INSTANCE.bind(this);
    }

    public int getTextureID() {
        return textureID;
    }
}
