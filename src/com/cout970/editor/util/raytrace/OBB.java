package com.cout970.editor.util.raytrace;

import com.cout970.editor.util.Direction;
import com.cout970.editor.util.Vect2d;
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
        Vect3d str = Vect3d.nullVector();
        Vect3d end = size.copy();
        Direction[] dirs = d.getAxis().getPerpendicularDirections();
        Vect2d[] pattern = {new Vect2d(0, 0), new Vect2d(1, 0), new Vect2d(1, 1), new Vect2d(0, 1)};
        Quad q = new Quad(
                new Vect3d(str.getX() + end.getX() * (d.getOffsetX() > 0 ? 1 : pattern[0].getX()), str.getY() + end.getY() * (d.getOffsetY() > 0 ? 1 : pattern[0].getY()),
                        str.getZ() + end.getZ() * (d.getOffsetZ() > 0 ? 1 : d.getOffsetX() > 0 ? pattern[0].getY() : pattern[0].getX())),
                new Vect3d(str.getX() + end.getX() * (d.getOffsetX() > 0 ? 1 : pattern[1].getX()), str.getY() + end.getY() * (d.getOffsetY() > 0 ? 1 : pattern[1].getY()),
                        str.getZ() + end.getZ() * (d.getOffsetZ() > 0 ? 1 : d.getOffsetX() > 0 ? pattern[1].getY() : pattern[1].getX())),
                new Vect3d(str.getX() + end.getX() * (d.getOffsetX() > 0 ? 1 : pattern[2].getX()), str.getY() + end.getY() * (d.getOffsetY() > 0 ? 1 : pattern[2].getY()),
                        str.getZ() + end.getZ() * (d.getOffsetZ() > 0 ? 1 : d.getOffsetX() > 0 ? pattern[2].getY() : pattern[2].getX())),
                new Vect3d(str.getX() + end.getX() * (d.getOffsetX() > 0 ? 1 : pattern[3].getX()), str.getY() + end.getY() * (d.getOffsetY() > 0 ? 1 : pattern[3].getY()),
                        str.getZ() + end.getZ() * (d.getOffsetZ() > 0 ? 1 : d.getOffsetX() > 0 ? pattern[3].getY() : pattern[3].getX())));
        for (int i = 0; i < 4; i++) {
            q.setVertex(i, q.getVertex(i).rotateX(rotation.getX()));
            q.setVertex(i, q.getVertex(i).rotateY(rotation.getY()));
            q.setVertex(i, q.getVertex(i).rotateZ(rotation.getZ()));
            q.setVertex(i, q.getVertex(i).add(pos));
        }
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
            if (r != null){
                r.setObject(this);
                return r;
            }
        }
        return null;
    }
}
