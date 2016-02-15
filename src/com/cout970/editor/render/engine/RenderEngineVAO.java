package com.cout970.editor.render.engine;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

public class RenderEngineVAO implements IRenderEngine {

	private static int bufferSize = 0x200000;
	private static ByteBuffer byteBuffer = BufferUtils.createByteBuffer(bufferSize * 4);
	private static IntBuffer intBuffer = byteBuffer.asIntBuffer();
	private static FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
	private static ShortBuffer shortBuffer = byteBuffer.asShortBuffer();

	private int[] tempBuffer;
	private int tempBufferIndex, tempBufferSize;
	private int vertices;

	private boolean drawing, useTexture, useColor, useNormal, useLight;
	private int drawMode, color, normal, light;
	private double textureU, textureV;
	public static int totalVertices;

	@Override
	public void startDrawing(int type) {
		if (drawing) {
			throw new RuntimeException("The Render Engine it's already drawing other thing!");
		}
		drawing = true;
		reset();
		drawMode = type;
		useColor = false;
		useTexture = false;
		useNormal = false;
		useLight = false;
	}

	private void reset() {
		vertices = 0;
		byteBuffer.clear();
		tempBufferIndex = 0;
	}

	@Override
	public void endDrawing() {
		if (!drawing) {
			throw new RuntimeException(
					"Someone try to call endDrawing before calling the method startDrawing");
		}
		drawing = false;
		int offset = 0;
		while (offset < vertices) {
			int vtc = Math.min(vertices - offset, bufferSize >> 5);
			intBuffer.clear();
			intBuffer.put(tempBuffer, offset * 8, vtc * 8);
			byteBuffer.position(0);
			byteBuffer.limit(vtc * 32);
			offset += vtc;

			if (useTexture) {
				floatBuffer.position(3);
				GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 32, floatBuffer);
				GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
			}

			if (useColor) {
				byteBuffer.position(20);
				GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, 32, byteBuffer);
				GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
			}

			if (useNormal) {
				byteBuffer.position(24);
				GL11.glNormalPointer(GL11.GL_BYTE, 32, byteBuffer);
				GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
			}
			
			if(useLight){
				GL13.glClientActiveTexture(GL13.GL_TEXTURE1);
				shortBuffer.position(14);
				GL11.glTexCoordPointer(2, GL11.GL_SHORT, 32, shortBuffer);
                GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
                GL13.glClientActiveTexture(GL13.GL_TEXTURE0);
			}

			floatBuffer.position(0);
			GL11.glVertexPointer(3, GL11.GL_FLOAT, 32, floatBuffer);
			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glDrawArrays(drawMode, 0, vtc);
			GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);

			if (useTexture) {
				GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
			}

			if (useColor) {
				GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
			}

			if (useNormal) {
				GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
			}
			
			if(useLight){
				GL13.glClientActiveTexture(GL13.GL_TEXTURE1);
                GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
                GL13.glClientActiveTexture(GL13.GL_TEXTURE0);
			}
			
		}
		if (tempBufferSize > 0x20000 && tempBufferIndex < (tempBufferSize << 3)) {
			tempBufferSize = 0x10000;
			tempBuffer = new int[tempBufferSize];
		}
		reset();
	}

	@Override
	public void addVertex(double x, double y, double z) {
		if (tempBufferIndex >= tempBufferSize - 32) {
			if (tempBufferSize == 0) {
				tempBufferSize = 0x10000;
				tempBuffer = new int[tempBufferSize];
			} else {
				tempBufferSize *= 2;
				tempBuffer = Arrays.copyOf(tempBuffer, tempBufferSize);
			}
		}

		tempBuffer[tempBufferIndex + 0] = Float.floatToRawIntBits((float) (x));
		tempBuffer[tempBufferIndex + 1] = Float.floatToRawIntBits((float) (y));
		tempBuffer[tempBufferIndex + 2] = Float.floatToRawIntBits((float) (z));

		if (useTexture) {
			tempBuffer[tempBufferIndex + 3] = Float.floatToRawIntBits((float) textureU);
			tempBuffer[tempBufferIndex + 4] = Float.floatToRawIntBits((float) textureV);
		}

		if (useColor) {
			tempBuffer[tempBufferIndex + 5] = color;
		}

		if (useNormal) {
			tempBuffer[tempBufferIndex + 6] = normal;
		}
		
		if(useLight){
			tempBuffer[tempBufferIndex + 7] = light;
		}

		vertices++;
		totalVertices++;
		tempBufferIndex += 8;
	}

	@Override
	public void addTextureUV(double u, double v) {
		useTexture = true;
		textureU = u;
		textureV = v;
	}

	@Override
	public void translate(double x, double y, double z) {
		GL11.glTranslated(x, y, z);
	}

	@Override
	public void rotate(double angle, double x, double y, double z) {
		GL11.glRotatef((float) angle, (float) x, (float) y, (float) z);
	}

	@Override
	public void scale(double x, double y, double z) {
		GL11.glScalef((float) x, (float) y, (float) z);
	}

	@Override
	public void setColor(int rgb, float alpha) {
		useColor = true;
		int auxRgb = (rgb & 0xFF) << 16 | (rgb & 0xFF00) | (rgb & 0xFF0000) >>> 16;
		color = ((int) (alpha * 0xFF) << 24) | auxRgb;
	}

	@Override
	public void loadIdentity() {
		GL11.glLoadIdentity();
	}

	@Override
	public void pushMatrix() {
		GL11.glPushMatrix();
	}

	@Override
	public void popMatrix() {
		GL11.glPopMatrix();
	}

	@Override
	public void enable(int i) {
		GL11.glEnable(i);
	}

	@Override
	public void disable(int i) {
		GL11.glDisable(i);
	}

	@Override
	public void enableBlend() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void addNormal(double x, double y, double z) {
		useNormal = true;
		int b1 = (int) (x * 127.0D) & 255;
		int b2 = (int) (y * 127.0D) & 255;
		int b3 = (int) (z * 127.0D) & 255;
		normal = b1 | (b2 << 8) | (b3 << 16);
	}

	@Override
	public void startCompile(DisplayList list) {
		if(list.vertexlist != -1){
			GL11.glDeleteLists(list.vertexlist, 1);
		}
		list.vertexlist = GL11.glGenLists(1);
		GL11.glNewList(list.vertexlist, GL11.GL_COMPILE);
	}

	@Override
	public void endCompile() {
		GL11.glEndList();
	}

	@Override
	public void render(DisplayList list) {
		GL11.glCallList(list.vertexlist);
	}

	@Override
	public void setColor(float r, float g, float b, float alpha) {
		int rgb = (((int)(r*255) & 0xFF) << 16) | (((int)(g*255) & 0xFF) << 8) | ((int)(b*255) & 0xFF);
		setColor(rgb, alpha);
	}

	@Override
	public void setColorOpaque(int rgb) {
		setColor(rgb, 1f);
	}

	@Override
	public void addLight(double f) {
		useLight = true;
		light = 0;
	}

	@Override
	public void enableLight() {
//		GL13.glClientActiveTexture(GL13.GL_TEXTURE1);
//		GL11.glEnable(GL11.GL_TEXTURE_2D);
//		ITexture tex = TextureStorage.LIGHT_MAP;
//		TextureManager.INSTANCE.bindForced(tex);
//		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
//		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
//		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
//		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
//		GL11.glColor4f(1, 1, 1, 1);
//        GL13.glClientActiveTexture(GL13.GL_TEXTURE0);
	}

	@Override
	public void disableLight() {
//		GL13.glClientActiveTexture(GL13.GL_TEXTURE1);
//		GL11.glDisable(GL11.GL_TEXTURE_2D);
//		GL13.glClientActiveTexture(GL13.GL_TEXTURE0);
	}
}
