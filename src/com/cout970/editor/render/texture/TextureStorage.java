package com.cout970.editor.render.texture;

import com.cout970.editor.Editor;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static org.lwjgl.BufferUtils.createByteBuffer;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;

public class TextureStorage {

    public static final TextureStorage INSTANCE = new TextureStorage();
    public static ITexture EMPTY;
    public static ITexture MISSING_TEXTURE;
    public static ITexture CUBE;
    public static ITexture CENTER;
    public static ITexture BUTTONS;
    public static ITexture FONT;
    public static ITexture MODEL_TEXTURE;
    public static ITexture ROTATION_POINT;

    private TextureStorage() {
    }

    public static void initTextures() {
        //abilita el uso de las texturas
        glEnable(GL_TEXTURE_2D);
        INSTANCE.reloadTextures(Editor.EDITOR_NAME.toLowerCase());
    }

    public void reloadTextures(String domain) {
        EMPTY = new TextureGL(0, "empty", 16, 16);

        MISSING_TEXTURE = TextureManager.INSTANCE.loadTexture(new ResourceReference(domain, "textures/missing.png"),
                "missing_texture");
        CUBE = TextureManager.INSTANCE.loadTexture(new ResourceReference(domain, "textures/cube.png"),
                "cube");
        CENTER = TextureManager.INSTANCE.loadTexture(new ResourceReference(domain, "textures/center.png"),
                "center");
        BUTTONS = TextureManager.INSTANCE.loadTexture(new ResourceReference(domain, "textures/buttons.png"),
                "button");
        FONT = TextureManager.INSTANCE.loadTexture(new ResourceReference(domain, "fonts/oldfont.png"),
                "font");
        ROTATION_POINT = TextureManager.INSTANCE.loadTexture(new ResourceReference(domain, "textures/rotation_point.png"),
                "rotation_point");
        MODEL_TEXTURE = MISSING_TEXTURE;
    }

    public static void loadModelTexture(File file) {

        try {
            ByteBuffer buffer;
            FileInputStream fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();

            buffer = createByteBuffer((int) fc.size() + 1);
            int i;
            do {
                i = fc.read(buffer);
            } while (i != -1);

            fis.close();
            fc.close();
            buffer.rewind();
            MODEL_TEXTURE = TextureManager.INSTANCE.loadTexture(buffer, file.getName());
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }
}
