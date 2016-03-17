package com.cout970.editor2.render.texture.atlas;

import java.nio.ByteBuffer;

public class SpriteTextureBuffer {

    private ByteBuffer image;
    private int sizeX, sizeY;
    private boolean alpha;
    private String name;

    public SpriteTextureBuffer(ByteBuffer texture, int sizeX, int sizeY, boolean alpha, String name) {
        this.image = texture;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.alpha = alpha;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ByteBuffer getTexture() {
        return image;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public boolean hasAlpha() {
        return alpha;
    }

    @Override
    public String toString() {
        return "SpriteTextureBuffer [buffer=" + image + ", sizeX=" + sizeX + ", sizeY=" + sizeY + ", alpha=" + alpha + "]";
    }
}
