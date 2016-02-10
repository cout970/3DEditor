package com.cout970.editor.render.engine;

public class DisplayList {

	protected int vertexlist;
	protected int colorList;
	protected int textureList;
	protected int normalList;
	protected int vertex;
	protected int drawMode;
	
	public DisplayList(){
		vertexlist = -1;
		colorList = -1;
		textureList = -1;
		normalList = -1;
		vertex = -1;
		drawMode = -1;
	}

	@Override
	public String toString() {
		return "DisplayList [list=" + vertexlist + ", colorList=" + colorList + ", textureList=" + textureList + ", normalList=" + normalList + "]";
	}
}
