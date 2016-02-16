package com.cout970.editor.util.raytrace;

import com.cout970.editor.util.Direction;
import com.cout970.editor.util.Vect3d;

/**
 * Created by cout970 on 16/02/2016.
 */
public class OBB implements IRayObstacle {

    private Vect3d pos;
    private Vect3d size;
    private Vect3d rotation;

    public OBB(Vect3d pos, Vect3d size, Vect3d rotation) {
        this.pos = pos.copy();
        this.size = size.copy();
        this.rotation = rotation.copy();
    }

    public Vect3d getPos() {
        return pos.copy();
    }

    public void setPos(Vect3d pos) {
        this.pos = pos;
    }

    public Vect3d getSize() {
        return size.copy();
    }

    public void setSize(Vect3d size) {
        this.size = size;
    }

    public Vect3d getRotation() {
        return rotation.copy();
    }

    public void setRotation(Vect3d rotation) {
        this.rotation = rotation;
    }

    private Quad getQuad(Direction d) {
        Vect3d str = Vect3d.nullVector().add(pos);
        Vect3d end = size.copy();
        Vect3d[] pattern = null;
        if (d == Direction.DOWN) {
            pattern = new Vect3d[]{new Vect3d(0, 0, 0), new Vect3d(1, 0, 0), new Vect3d(1, 0, 1), new Vect3d(0, 0, 1)};
        } else if (d == Direction.UP) {
            pattern = new Vect3d[]{new Vect3d(0, 1, 0), new Vect3d(1, 1, 0), new Vect3d(1, 1, 1), new Vect3d(0, 1, 1)};
        } else if (d == Direction.NORTH) {
            pattern = new Vect3d[]{new Vect3d(0, 0, 0), new Vect3d(1, 0, 0), new Vect3d(1, 1, 0), new Vect3d(1, 0, 0)};
        } else if (d == Direction.SOUTH) {
            pattern = new Vect3d[]{new Vect3d(0, 0, 1), new Vect3d(1, 0, 1), new Vect3d(1, 1, 1), new Vect3d(1, 0, 1)};
        } else if (d == Direction.WEST) {
            pattern = new Vect3d[]{new Vect3d(0, 0, 0), new Vect3d(0, 1, 0), new Vect3d(0, 1, 1), new Vect3d(0, 0, 1)};
        } else {
            pattern = new Vect3d[]{new Vect3d(1, 0, 0), new Vect3d(1, 1, 0), new Vect3d(1, 1, 1), new Vect3d(1, 0, 1)};
        }
        Quad q = new Quad(
                new Vect3d(str.getX() + end.getX() * pattern[0].getX(), str.getY() + end.getY() * pattern[0].getY(), str.getZ() + end.getZ() * pattern[0].getZ()),
                new Vect3d(str.getX() + end.getX() * pattern[1].getX(), str.getY() + end.getY() * pattern[1].getY(), str.getZ() + end.getZ() * pattern[1].getZ()),
                new Vect3d(str.getX() + end.getX() * pattern[2].getX(), str.getY() + end.getY() * pattern[2].getY(), str.getZ() + end.getZ() * pattern[2].getZ()),
                new Vect3d(str.getX() + end.getX() * pattern[3].getX(), str.getY() + end.getY() * pattern[3].getY(), str.getZ() + end.getZ() * pattern[3].getZ()));

//        for (int i = 0; i < 4; i++) {
//            q.setVertex(i, q.getVertex(i).rotateX(rotation.getX()));
//            q.setVertex(i, q.getVertex(i).rotateY(rotation.getY()));
//            q.setVertex(i, q.getVertex(i).rotateZ(rotation.getZ()));
//            q.setVertex(i, q.getVertex(i).add(pos));
//        }

        return q;
    }

    @Override
    public RayTraceResult rayTrace(Ray ray) {
        Vect3d str = pos.copy();
        Vect3d end = pos.copy().add(size);
        RayTraceResult r;
        for (Direction d : Direction.values()) {
            Quad q = getQuad(d);

            r = q.rayTrace(ray);
            if (r != null) {
                r.setObject(this);
                return r;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "OBB{" +
                "pos=" + pos +
                ", size=" + size +
                ", rotation=" + rotation +
                '}';
    }
}
