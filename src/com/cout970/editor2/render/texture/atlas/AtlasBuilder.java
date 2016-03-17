package com.cout970.editor2.render.texture.atlas;

import java.nio.ByteBuffer;
import java.util.*;

public class AtlasBuilder {

    private List<SpriteSlot> slots;
    private ByteBuffer buffer;
    private int size;

    public AtlasBuilder(ByteBuffer buff, int size) {
        this.buffer = buff;
        slots = new LinkedList<SpriteSlot>();
        slots.add(new SpriteSlot(0, 0, size, size));
        this.size = size;
    }

    public void insert(SpriteTextureBuffer image) {
        int maxDim = Math.max(image.getSizeX(), image.getSizeY());

        Stack<SpriteSlot> remove = new Stack<>();
        Stack<SpriteSlot> add = new Stack<>();
        boolean found = false, toBig;
        while (!found) {
            remove.clear();
            add.clear();
            Collections.sort(slots, new Comparator<SpriteSlot>() {

                @Override
                public int compare(SpriteSlot a, SpriteSlot b) {
                    return -(Math.max(b.getSizeX(), b.getSizeY()) - Math.max(a.getSizeX(), a.getSizeY()));
                }

            });
            toBig = true;
            for (SpriteSlot s : slots) {
                if (s.getSpriteTextureBuffer() == null) {
                    if (s.getSizeX() >= maxDim && s.getSizeY() >= maxDim) {
                        toBig = false;
                    }
                    if (s.getSizeX() >= maxDim * 2 && s.getSizeY() >= maxDim * 2) {
                        List<SpriteSlot> sub = s.subSlots();
                        remove.push(s);
                        for (int index = 0; index < sub.size(); index++)
                            add.push(sub.get(index));
                        break;
                    } else if (s.getSizeX() >= maxDim && s.getSizeY() >= maxDim) {
                        s.setSpriteTextureBuffer(image);
                        found = true;
                        break;
                    }
                }
            }
            slots.removeAll(remove);
            slots.addAll(add);
            if (slots.isEmpty()) {
                throw new RuntimeException("No enough slots in this atlas, atlas size: [" + size + ", " + size + "] num of slots used: " + slots.size());
            } else if (toBig) {
                throw new RuntimeException("No enough space to store the texture: " + image);
            }
        }
    }


    public List<SpriteSlot> getTextureSlots() {
        List<SpriteSlot> list = new LinkedList<>();
        for (SpriteSlot slot : slots) {
            if (slot.getSpriteTextureBuffer() != null) {
                list.add(slot);
            }
        }
        return list;
    }

    public ByteBuffer getTexture() {
        return buffer;
    }

    public int getSizeX() {
        return size;
    }

    public int getSizeY() {
        return size;
    }
}
