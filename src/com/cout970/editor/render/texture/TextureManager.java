package com.cout970.editor.render.texture;

import com.cout970.editor.render.texture.atlas.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.*;

import static org.lwjgl.BufferUtils.createByteBuffer;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_BASE_LEVEL;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;
import static org.lwjgl.stb.STBImage.*;

public class TextureManager implements ITextureLoader {

    public static final TextureManager INSTANCE = new TextureManager();

    private static String TEXTURE_DOMAIN_FOLDER = "." + File.separator + "res" + File.separator + "domains" + File.separator;
    private List<ResourceReference> textureFiles;
    private Map<ResourceReference, ITexture> registeredTextures;
    private static int maxTextureSize = -1;
    private int loadedTexture;

    public TextureManager() {
        textureFiles = new LinkedList<ResourceReference>();
        registeredTextures = new HashMap<>();
    }

    public void registerTexture(ResourceReference file, ITexture texture) {
        registeredTextures.put(file, texture);
    }

    @Override
    public ITexture getTexture(ResourceReference path) {
        if (registeredTextures.containsKey(path)) {
            return registeredTextures.get(path);
        }
        return loadTexture(path, path.getFileName());
    }

    public void bind(ITexture tex) {
        if (tex.getTextureID() != loadedTexture) {
            loadedTexture = tex.getTextureID();
            glBindTexture(GL_TEXTURE_2D, tex.getTextureID());
        }
    }

    public void bind(ITextureSprite tex) {
        bind(tex.getParent());
    }

    public void bindForced(ITexture tex) {
        loadedTexture = tex.getTextureID();
        glBindTexture(GL_TEXTURE_2D, tex.getTextureID());
    }

    @Override
    public ITexture loadTexture(ResourceReference resourceFile, String textureName) {
        ByteBuffer buff;

        try {
            buff = resourceToBuffer(resourceFile, 8 * 1024);
            ITexture tex = loadTexture(buff, textureName);

            registerTexture(resourceFile, tex);
            return tex;
        } catch (IOException e) {
            e.printStackTrace();
            return TextureStorage.MISSING_TEXTURE;
        }
    }

    @Override
    public ITexture loadTexture(ByteBuffer imageBuffer, String textureName) {

        ByteBuffer image;
        TextureGL texture;
        int w, h;

        IntBuffer wBuf = BufferUtils.createIntBuffer(1);
        IntBuffer hBuf = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);

        if (stbi_info_from_memory(imageBuffer, wBuf, hBuf, comp) == 0)
            throw new RuntimeException("Failed to read image information: " + stbi_failure_reason());

        w = wBuf.get(0);
        h = hBuf.get(0);

        // Decode the image
        image = stbi_load_from_memory(imageBuffer, wBuf, hBuf, comp, 0);
        if (image == null)
            throw new RuntimeException("Failed to load image: " + stbi_failure_reason());

        int texID = glGenTextures();

        texture = new TextureGL(texID, textureName, w, h);

        glBindTexture(GL_TEXTURE_2D, texID);
        if (comp.get(0) == 3)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, w, h, 0, GL_RGB, GL_UNSIGNED_BYTE, image);
        else {
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        }

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        return texture;
    }

    @Override
    public ITextureAtlas generateTextureAtlas(ResourceReference resource, String name) {
        searchForTextureFiles(resource);
        ITextureAtlas tex = combineTextures(textureFiles, name);
        registerTexture(resource, tex);
        return tex;
    }

    private AtlasBuilder createAtlas() {
        int size = getMaxTextureSize();
        ByteBuffer buff;
        AtlasBuilder atlas;

        buff = createByteBuffer(size * size * 4);
        atlas = new AtlasBuilder(buff, size);

        return atlas;
    }

    private void searchForTextureFiles(ResourceReference directory) {
        File root = directory.getFile();

        textureFiles.clear();
        Stack<File> stack = new Stack<File>();
        if (root != null) {
            for (File f : root.listFiles()) {
                stack.push(f);
            }
        }
        while (!stack.isEmpty()) {
            File f = stack.pop();
            if (f.getName().endsWith(".png") && f.canRead()) {
                String str = f.getPath().replace(TEXTURE_DOMAIN_FOLDER, "")
                        .replace(directory.getDomain() + File.separator, "");
                textureFiles
                        .add(new ResourceReference(directory.getDomain(), str, f.getName().replace(".png", "")));
            } else if (f.isDirectory()) {
                for (File file : f.listFiles()) {
                    stack.push(file);
                }
            }
        }
    }

    private ITextureAtlas combineTextures(List<ResourceReference> files, String name) {

        AtlasBuilder atlas = createAtlas();

        // loading textures from the files

        for (ResourceReference res : files) {
            try {
                ByteBuffer imageBuffer = resourceToBuffer(res, -1);
                ByteBuffer image;
                int w, h;

                IntBuffer wBuf = BufferUtils.createIntBuffer(1);
                IntBuffer hBuf = BufferUtils.createIntBuffer(1);
                IntBuffer comp = BufferUtils.createIntBuffer(1);
                if (stbi_info_from_memory(imageBuffer, wBuf, hBuf, comp) == 0)
                    throw new RuntimeException("Failed to read image information: " + stbi_failure_reason());

                w = wBuf.get(0);
                h = hBuf.get(0);

                // Decode the image
                image = stbi_load_from_memory(imageBuffer, wBuf, hBuf, comp, 4);
                if (image == null)
                    throw new RuntimeException("Failed to load image: " + stbi_failure_reason());

                SpriteTextureBuffer sprite = new SpriteTextureBuffer(image, w, h, comp.get(0) == 4,
                        res.getFileName());
                atlas.insert(sprite);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // registering the texture with OpenGL

        int texID = glGenTextures();

        TextureAtlas texture = new TextureAtlas(texID, name, atlas.getSizeX(), atlas.getSizeY(), atlas);

        glEnable(GL_TEXTURE_2D);

        glBindTexture(GL_TEXTURE_2D, texID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_BASE_LEVEL, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 0);
        glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, atlas.getSizeX(), atlas.getSizeY(), 0, GL_RGBA,
                GL_UNSIGNED_BYTE, atlas.getTexture());

        // combining all the textures together

        for (SpriteSlot slot : atlas.getTextureSlots()) {

            SpriteTextureBuffer sprite = slot.getSpriteTextureBuffer();
            glTexSubImage2D(GL_TEXTURE_2D, 0, slot.getPosX(), slot.getPosY(), sprite.getSizeX(),
                    sprite.getSizeY(), GL_RGBA, GL_UNSIGNED_BYTE, sprite.getTexture());
        }

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        return texture;
    }

    public int getMaxTextureSize() {
        if (maxTextureSize != -1) {
            return maxTextureSize;
        }
        for (int i = 0x4000; i > 0; i >>= 1) {
            GL11.glTexImage2D(GL11.GL_PROXY_TEXTURE_2D, 0, GL11.GL_RGBA, i, i, 0, GL11.GL_RGBA,
                    GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
            int j = GL11.glGetTexLevelParameteri(GL11.GL_PROXY_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);

            if (j != 0) {
                maxTextureSize = i;
                return i;
            }
        }
        return -1;
    }

    public static ByteBuffer resourceToBuffer(ResourceReference resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        File file = new File(resource.getCompletePath());
        if (file.isFile()) {
            FileInputStream fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();

            buffer = createByteBuffer((int) fc.size() + 1);

            while (fc.read(buffer) != -1) ;

            fis.close();
            fc.close();
        } else {
            buffer = createByteBuffer(bufferSize);

            InputStream source = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource.getCompletePath());
            if (source == null)
                throw new FileNotFoundException(resource.getCompletePath() + ", " + resource.getFile().getAbsolutePath());

            try {
                ReadableByteChannel rbc = Channels.newChannel(source);
                try {
                    while (true) {
                        int bytes = rbc.read(buffer);
                        if (bytes == -1)
                            break;
                        if (buffer.remaining() == 0)
                            buffer = resizeBuffer(buffer, buffer.capacity() * 2);
                    }
                } finally {
                    rbc.close();
                }
            } finally {
                source.close();
            }
        }

        buffer.flip();
        return buffer;
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }
}
