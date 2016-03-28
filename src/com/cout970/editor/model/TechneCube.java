package com.cout970.editor.model;

import com.cout970.editor.Editor;
import com.cout970.editor.render.examples.Lines;
import com.cout970.editor.render.texture.TextureStorage;
import com.cout970.editor.util.Direction;
import com.cout970.editor.util.Vect2d;
import com.cout970.editor.util.Vect3d;
import com.cout970.editor.util.raytrace.IRayObstacle;
import com.cout970.editor.util.raytrace.OBB;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by cout970 on 11/02/2016.
 */
public class TechneCube implements IModel {

    protected static final float[] BRITHNESS = {0.5F, 1.0F, 0.8F, 0.8F, 0.6F, 0.6F};
    protected Quad[] quadList;
    private int renderList = -1;
    private int selectedList = -1;

    protected String name;
    protected Vect3d cubePos;
    protected Vect3d cubeSize;
    protected Vect2d textureOffset;
    protected boolean flipped;
    protected Vect3d rotation;
    protected Vect3d rotationPoint;
    private boolean visible = true;


    public TechneCube(String name, Vect3d cubePos, Vect3d cubeSize, Vect2d textureOffset) {
        this.cubePos = cubePos;
        this.name = name;
        this.cubeSize = cubeSize;
        this.textureOffset = textureOffset;
        this.rotationPoint = Vect3d.nullVector();
        this.rotation = Vect3d.nullVector();
    }

    public void setFlipped(boolean flipped) {
        resetRenderList();
        this.flipped = flipped;
    }

    public String getName() {
        return name;
    }

    public Vect3d getPos() {
        return cubePos.copy();
    }

    public Vect3d getSize() {
        return cubeSize.copy();
    }

    public Vect2d getTextureOffset() {
        return textureOffset.copy();
    }

    public boolean isFlipped() {
        return flipped;
    }

    public Vect3d getRotation() {
        return rotation.copy();
    }

    public Vect3d getRotationPoint() {
        return rotationPoint.copy();
    }

    public void setName(String buffer) {
        name = buffer;
    }

    public void setPos(Vect3d cubePos) {
        resetRenderList();
        this.cubePos = cubePos;
    }

    public void setSize(Vect3d cubeSize) {
        resetRenderList();
        this.cubeSize = cubeSize;
    }

    public void setTextureOffset(Vect2d textureOffset) {
        resetRenderList();
        this.textureOffset = textureOffset;
    }

    public void setRotation(Vect3d rotation) {
        resetRenderList();
        this.rotation = rotation;
    }

    public void setRotationPoint(Vect3d offset) {
        resetRenderList();
        this.rotationPoint = offset;
    }

    @Override
    public void render(boolean selected) {

        if (renderList == -1) {
            renderList = glGenLists(1);

            createQuads();
            glNewList(renderList, GL_COMPILE);
            glBegin(GL_QUADS);
            for (Quad q : quadList) {
                float bright = BRITHNESS[q.getNormal().ordinal()];
                glColor4f(bright, bright, bright, 1f);
                for (QuadVertex v : QuadVertex.values()) {
                    Vect2d uv = q.getUV(v);
                    Vect3d ve = q.getVertex(v);
                    glTexCoord2d(uv.getX(), uv.getY());
                    glVertex3d(ve.getX(), ve.getY(), ve.getZ());
                }
            }
            glEnd();
            glEndList();
        }
        if (selectedList == -1) {
            selectedList = glGenLists(1);
            glNewList(selectedList, GL_COMPILE);
            Lines.cubeSelection(getSize().multiply(0.0625));
            glEndList();
        }
        TextureStorage.MODEL_TEXTURE.bind();
        glCallList(renderList);

        if (selected) {
            TextureStorage.EMPTY.bind();
            glPushMatrix();

            glTranslated(rotationPoint.getX(), rotationPoint.getY(), rotationPoint.getZ());
            glRotated(Math.toDegrees(rotation.getZ()), 0, 0, 1);
            glRotated(Math.toDegrees(rotation.getY()), 0, 1, 0);
            glRotated(Math.toDegrees(rotation.getX()), 1, 0, 0);
            glTranslated(-rotationPoint.getX(), -rotationPoint.getY(), -rotationPoint.getZ());

            glTranslated(cubePos.getX(), cubePos.getY(), cubePos.getZ());
            glCallList(selectedList);
            glPopMatrix();
        }
    }

    @Override
    public List<IRayObstacle> getRayObstacles() {
        return Collections.singletonList(new OBB(getPos(), getSize().multiply(0.0625f), getRotation(), getRotationPoint().copy().sub(cubePos)));
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean b) {
        this.visible = b;
    }

    private void resetRenderList() {
        if (renderList != -1) {
            glDeleteLists(renderList, 1);
            renderList = -1;
        }
        if (selectedList != -1) {
            glDeleteLists(selectedList, 1);
            selectedList = -1;
        }
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

        double pixel = 1d / Editor.getProject().getTextureSize();

        double width = cubeSize.getX() * pixel;
        double height = cubeSize.getY() * pixel;
        double length = cubeSize.getZ() * pixel;

        double offsetX = textureOffset.getX() * pixel;
        double offsetY = textureOffset.getY() * pixel;

        quadList[0] = new Quad(Direction.WEST, new Vertex[]{vertex6.copy(), vertex2.copy(), vertex3.copy(), vertex7.copy()},
                offsetX + length + width,
                offsetY + length,
                offsetX + length + width + length,
                offsetY + length + height);

        quadList[1] = new Quad(Direction.EAST, new Vertex[]{vertex1.copy(), vertex5.copy(), vertex8.copy(), vertex4.copy()},
                offsetX,
                offsetY + length,
                offsetX + length,
                offsetY + length + height);

        quadList[2] = new Quad(Direction.DOWN, new Vertex[]{vertex6.copy(), vertex5.copy(), vertex1.copy(), vertex2.copy()},
                offsetX + length,
                offsetY,
                offsetX + length + width,
                offsetY + length);

        quadList[3] = new Quad(Direction.UP, new Vertex[]{vertex3.copy(), vertex4.copy(), vertex8.copy(), vertex7.copy()},
                offsetX + length + width,
                offsetY + length,
                offsetX + length + width + width,
                offsetY);

        quadList[4] = new Quad(Direction.NORTH, new Vertex[]{vertex2.copy(), vertex1.copy(), vertex4.copy(), vertex3.copy()},
                offsetX + length,
                offsetY + length,
                offsetX + length + width,
                offsetY + length + height);

        quadList[5] = new Quad(Direction.SOUTH, new Vertex[]{vertex5.copy(), vertex6.copy(), vertex7.copy(), vertex8.copy()},
                offsetX + length + width + length,
                offsetY + length,
                offsetX + length + width + length + width,
                offsetY + length + height);

        if (flipped) {
            for (Quad aQuadList : quadList) {
                aQuadList.flipFace();
            }
        }

        Vect3d rotationPoint = this.rotationPoint.copy().sub(cubePos);
        for (Quad q : quadList) {
            for (int i = 0; i < 4; i++) {
                q.vertex[i].getPos().sub(rotationPoint);
                q.vertex[i].getPos().rotateX(rotation.getX());
                q.vertex[i].getPos().rotateY(rotation.getY());
                q.vertex[i].getPos().rotateZ(rotation.getZ());
                q.vertex[i].getPos().add(rotationPoint);
                q.vertex[i].getPos().add(cubePos);
            }
        }
    }

    public List<Quad> getQuads() {
        if (quadList == null) {
            createQuads();
        }
        return Arrays.asList(quadList);
    }

    @Override
    public String toString() {
        return "TestTechneCube{" +
                "name='" + name + '\'' +
                ", cubePos=" + cubePos +
                ", cubeSize=" + cubeSize +
                ", textureOffset=" + textureOffset +
                ", rotation=" + rotation +
                ", rotationPoint=" + rotationPoint +
                ", flipped=" + flipped +
                ", quadList=" + Arrays.toString(quadList) +
                '}';
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
        protected Direction normal;

        public Quad(Direction normal, Vertex[] vertices, double u, double v, double u2, double v2) {
            vertex = vertices;
            this.normal = normal;
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

        public Direction getNormal() {
            return normal;
        }

        @Override
        public String toString() {
            return "Quad{" +
                    "vertex=" + Arrays.toString(vertex) +
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
