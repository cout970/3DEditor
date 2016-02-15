package com.cout970.editor.model;

import com.cout970.editor.render.engine.IRenderEngine;
import com.cout970.editor.render.texture.ITexture;
import com.cout970.editor.util.Vect2d;
import com.cout970.editor.util.Vect3d;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;

/**
 * Created by cout970 on 11/02/2016.
 */
public class TechneCube implements IModel {

    protected Quad[] quadList;
    protected String name;
    protected Vect3d cubePos;
    protected Vect3d cubeSize;
    protected ITexture texture;
    protected Vect2d textureOffset;
    protected int textureSize;
    protected boolean flipped;
    protected Vect3d rotation;
    protected Vect3d offset;

    public TechneCube(String name, Vect3d cubePos, Vect3d cubeSize, ITexture texture, Vect2d textureOffset, int textureSize) {
        this.cubePos = cubePos;
        this.name = name;
        this.cubeSize = cubeSize;
        this.texture = texture;
        this.textureOffset = textureOffset;
        this.textureSize = textureSize;
        this.offset = Vect3d.nullVector();
        this.rotation = Vect3d.nullVector();
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }

    public String getName() {
        return name;
    }

    public Vect3d getCubePos() {
        return cubePos;
    }

    public Vect3d getCubeSize() {
        return cubeSize;
    }

    public ITexture getTexture() {
        return texture;
    }

    public Vect2d getTextureOffset() {
        return textureOffset;
    }

    public int getTextureSize() {
        return textureSize;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void createQuads() {
        quadList = new Quad[6];
        Vect3d start = Vect3d.nullVector();
        Vect3d end = cubeSize.copy().multiply(0.0625f);

        if (flipped) {
            double aux = end.getX();
            end.setX(start.getX());
            start.setX(aux);
        }

        Vertex vertex1 = new Vertex(start.getX(), start.getY(), start.getZ());
        Vertex vertex2 = new Vertex(end.getX(), start.getY(), start.getZ());
        Vertex vertex3 = new Vertex(end.getX(), end.getY(), start.getZ());
        Vertex vertex4 = new Vertex(start.getX(), end.getY(), start.getZ());
        Vertex vertex5 = new Vertex(start.getX(), start.getY(), end.getZ());
        Vertex vertex6 = new Vertex(end.getX(), start.getY(), end.getZ());
        Vertex vertex7 = new Vertex(end.getX(), end.getY(), end.getZ());
        Vertex vertex8 = new Vertex(start.getX(), end.getY(), end.getZ());

        double pixel = 1d / textureSize;

        double width = cubeSize.getX() * pixel;
        double height = cubeSize.getY() * pixel;
        double length = cubeSize.getZ() * pixel;

        double offsetX = textureOffset.getX() * pixel;
        double offsetY = textureOffset.getY() * pixel;

        quadList[0] = new Quad(new Vertex[]{vertex6.copy(), vertex2.copy(), vertex3.copy(), vertex7.copy()},
                offsetX + length + width,
                offsetY + length,
                offsetX + length + width + length,
                offsetY + length + height, texture);

        quadList[1] = new Quad(new Vertex[]{vertex1.copy(), vertex5.copy(), vertex8.copy(), vertex4.copy()},
                offsetX,
                offsetY + length,
                offsetX + length,
                offsetY + length + height, texture);

        quadList[2] = new Quad(new Vertex[]{vertex6.copy(), vertex5.copy(), vertex1.copy(), vertex2.copy()},
                offsetX + length,
                offsetY,
                offsetX + length + width,
                offsetY + length, texture);

        quadList[3] = new Quad(new Vertex[]{vertex3.copy(), vertex4.copy(), vertex8.copy(), vertex7.copy()},
                offsetX + length + width,
                offsetY + length,
                offsetX + length + width + width,
                offsetY, texture);

        quadList[4] = new Quad(new Vertex[]{vertex2.copy(), vertex1.copy(), vertex4.copy(), vertex3.copy()},
                offsetX + length,
                offsetY + length,
                offsetX + length + width,
                offsetY + length + height, texture);

        quadList[5] = new Quad(new Vertex[]{vertex5.copy(), vertex6.copy(), vertex7.copy(), vertex8.copy()},
                offsetX + length + width + length,
                offsetY + length,
                offsetX + length + width + length + width,
                offsetY + length + height, texture);

        if (flipped) {
            for (Quad aQuadList : quadList) {
                aQuadList.flipFace();
            }
        }

        for (Quad q : quadList) {
            for (int i = 0; i < 4; i++) {
                q.vertex[i].getPos().add(offset);
                if (rotation != null) {
                    q.vertex[i].getPos().rotateX(rotation.getX());
                    q.vertex[i].getPos().rotateY(rotation.getY());
                    q.vertex[i].getPos().rotateZ(rotation.getZ());
                }
                q.vertex[i].getPos().add(cubePos);
            }
        }
    }

    public List<Quad> getQuads() {
        createQuads();
        return Arrays.asList(quadList);
    }

    public void setOffset(Vect3d offset) {
        this.offset = offset;
    }

    public void setRotation(Vect3d rotation) {
        this.rotation = rotation;
    }

    @Override
    public String toString() {
        return "TestTechneCube{" +
                "name='" + name + '\'' +
                ", cubePos=" + cubePos +
                ", cubeSize=" + cubeSize +
                ", texture=" + texture +
                ", textureOffset=" + textureOffset +
                ", textureSize=" + textureSize +
                ", rotation=" + rotation +
                ", offset=" + offset +
                ", flipped=" + flipped +
                ", quadList=" + Arrays.toString(quadList) +
                '}';
    }

    @Override
    public void render() {
        IRenderEngine eng = IRenderEngine.INSTANCE;
        float[] f1 = {0.9f, 0.8f, 0.5f, 0.7f, 0.6f, 0.4f};
        int i = 0;
        texture.bind();
        eng.startDrawing(GL11.GL_QUADS);
        for (Quad q : getQuads()) {
            float f = f1[i];
            i++;
            eng.setColor(f, f, f, 1f);
            for (QuadVertex v : QuadVertex.values()) {
                Vect2d uv = q.getUV(v);
                Vect3d ve = q.getVertex(v);
                eng.addTextureUV(uv.getX(), uv.getY());
                eng.addVertex(ve.getX(), ve.getY(), ve.getZ());
            }
        }
        eng.endDrawing();
    }

    public void setCubeName(String buffer) {
        name = buffer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCubePos(Vect3d cubePos) {
        this.cubePos = cubePos;
    }

    public void setCubeSize(Vect3d cubeSize) {
        this.cubeSize = cubeSize;
    }

    public void setTexture(ITexture texture) {
        this.texture = texture;
    }

    public void setTextureOffset(Vect2d textureOffset) {
        this.textureOffset = textureOffset;
    }

    public void setTextureSize(int textureSize) {
        this.textureSize = textureSize;
    }

    public Vect3d getRotation() {
        return rotation;
    }

    public Vect3d getOffset() {
        return offset;
    }

    private class Vertex {

        private Vect3d pos;
        private Vect2d uv;

        public Vertex(double x, double y, double z) {
            pos = new Vect3d(x, y, z);
        }

        public void setUV(double u, double v) {
            uv = new Vect2d(u, v);
        }

        private Vertex(Vect3d pos, Vect2d uv) {
            this.pos = pos.copy();
            this.uv = uv != null ? uv.copy() : null;
        }

        public Vect3d getPos() {
            return pos;
        }

        public Vect2d getUV() {
            return uv;
        }

        public Vertex copy() {
            return new Vertex(pos, uv);
        }

        @Override
        public String toString() {
            return "Vertex{" +
                    "pos=" + pos +
                    ", uv=" + uv +
                    '}';
        }
    }

    private class Quad {

        protected static final float EPSILON = 0.0f / 1F;
        protected Vertex[] vertex;
        protected ITexture texture;

        public Quad(Vertex[] vertices, double u, double v, double u2, double v2, ITexture texture) {
            vertex = vertices;
            this.texture = texture;
            vertices[0].setUV(u2 - EPSILON, v + EPSILON);
            vertices[1].setUV(u + EPSILON, v + EPSILON);
            vertices[2].setUV(u + EPSILON, v2 - EPSILON);
            vertices[3].setUV(u2 - EPSILON, v2 - EPSILON);
        }

        public void flipFace() {
            Vertex[] flippedVertex = new Vertex[vertex.length];
            for (int i = 0; i < vertex.length; ++i) {
                flippedVertex[i] = vertex[vertex.length - i - 1];
            }
            vertex = flippedVertex;
        }

        public Vect3d getVertex(QuadVertex pos) {
            return vertex[pos.ordinal()].getPos();
        }

        public Vect2d getUV(QuadVertex pos) {
            return vertex[pos.ordinal()].getUV();
        }

        @Override
        public String toString() {
            return "Quad{" +
                    "vertex=" + Arrays.toString(vertex) +
                    ", texture=" + texture +
                    '}';
        }
    }

    enum QuadVertex {
        FIRST,
        SECOND,
        THIRD,
        FOURTH
    }
}
