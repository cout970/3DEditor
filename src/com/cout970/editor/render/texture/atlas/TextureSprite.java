package com.cout970.editor.render.texture.atlas;

import com.cout970.editor.render.texture.ITexture;

public class TextureSprite implements ITextureSprite{

	private ITextureAtlas parent;
	private int sizeX, sizeY;
	private int posX, posY;
	private double scale;
	
	public TextureSprite(int sizeX, int sizeY, ITextureAtlas parent, int posX, int posY, double scale) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.parent = parent;
		this.posX = posX;
		this.posY = posY;
		this.scale = scale;
	}

	@Override
	public ITexture getParent() {
		return parent;
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
	public double getTextureU(float offset) {
		return ((posX+sizeX*offset)*scale);
	}

	@Override
	public double getTextureV(float offset) {
		return ((posY+sizeY*offset)*scale);
	}

	@Override
	public String toString() {
		return "TextureSprite [parent=" + parent + ", sizeX=" + sizeX + ", sizeY=" + sizeY + ", posX=" + posX + ", posY=" + posY + ", scale=" + scale + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result + posX;
		result = prime * result + posY;
		long temp;
		temp = Double.doubleToLongBits(scale);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		TextureSprite other = (TextureSprite) obj;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		if (posX != other.posX)
			return false;
		if (posY != other.posY)
			return false;
		if (Double.doubleToLongBits(scale) != Double.doubleToLongBits(other.scale))
			return false;
		if (sizeX != other.sizeX)
			return false;
		if (sizeY != other.sizeY)
			return false;
		return true;
	}
	
	
}
