package com.cout970.editor.render.texture.atlas;

import java.util.ArrayList;
import java.util.List;

public class SpriteSlot {

    private int posX, posY;
    private int sizeX, sizeY;
    private SpriteTextureBuffer texture;

    public SpriteSlot(int posX, int posY, int sizeX, int sizeY) {
        this.posX = posX;
        this.posY = posY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public List<SpriteSlot> subSlots() {
        List<SpriteSlot> list = new ArrayList<>(4);
        int i = sizeX >> 1;
        int j = sizeY >> 1;

        list.add(new SpriteSlot(posX, posY, i, j));
        list.add(new SpriteSlot(posX + i, posY, i, j));
        list.add(new SpriteSlot(posX, posY + j, i, j));
        list.add(new SpriteSlot(posX + i, posY + j, i, j));

        return list;
    }

    public SpriteTextureBuffer getSpriteTextureBuffer() {
        return texture;
    }

    public void setSpriteTextureBuffer(SpriteTextureBuffer texture) {
        this.texture = texture;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + posX;
        result = prime * result + posY;
        result = prime * result + sizeX;
        result = prime * result + sizeY;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SpriteSlot other = (SpriteSlot) obj;
        if (posX != other.posX)
            return false;
        if (posY != other.posY)
            return false;
        if (sizeX != other.sizeX)
            return false;
        return sizeY == other.sizeY;
    }

    @Override
    public String toString() {
        return "TextureSlot [posX=" + posX + ", posY=" + posY + ", sizeX=" + sizeX + ", sizeY=" + sizeY + "]";
    }

    public ITextureSprite toTextureSprite(TextureAtlas parent) {
        return new TextureSprite(texture.getSizeX(), texture.getSizeY(), parent, posX, posY, 1D / parent.getTextureSizeX());
    }
}
