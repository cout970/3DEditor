package com.cout970.editor2.render.texture.atlas;

import com.cout970.editor2.render.texture.TextureGL;

import java.util.HashMap;

public class TextureAtlas extends TextureGL implements ITextureAtlas {

    private HashMap<String, ITextureSprite> sprites;

    public TextureAtlas(int texId, String name, int sizeX, int sizeY, AtlasBuilder atlas) {
        super(texId, name, sizeX, sizeY);
        sprites = new HashMap<>();
        for (SpriteSlot slot : atlas.getTextureSlots()) {
            sprites.put(slot.getSpriteTextureBuffer().getName(), slot.toTextureSprite(this));
        }
    }

    @Override
    public ITextureSprite getSprite(String name) {
        return sprites.get(name);
    }

    @Override
    public String toString() {
        return "TextureAtlas [getTextureName()=" + getTextureName() + ", getTextureSizeX()=" + getTextureSizeX() + ", getTextureSizeY()=" + getTextureSizeY() + ", getTextureID()=" + getTextureID() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((sprites == null) ? 0 : sprites.hashCode());
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
        TextureAtlas other = (TextureAtlas) obj;
        if (sprites == null) {
            if (other.sprites != null)
                return false;
        } else if (!sprites.equals(other.sprites))
            return false;
        return true;
    }
}
