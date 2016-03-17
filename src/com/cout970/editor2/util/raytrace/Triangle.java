package com.cout970.editor2.util.raytrace;

import com.cout970.editor2.util.Vect3d;

/**
 * Created by cout970 on 16/02/2016.
 */
public class Triangle implements IRayObstacle {

    private Vect3d a;
    private Vect3d b;
    private Vect3d c;

    public Triangle(Vect3d point1, Vect3d point2, Vect3d point3) {
        this.a = point1.copy();
        this.b = point2.copy();
        this.c = point3.copy();
    }

    public Vect3d getA() {
        return a.copy();
    }

    public void setA(Vect3d a) {
        this.a = a;
    }

    public Vect3d getB() {
        return b.copy();
    }

    public void setB(Vect3d b) {
        this.b = b;
    }

    public Vect3d getC() {
        return c.copy();
    }

    public void setC(Vect3d c) {
        this.c = c;
    }

    public Vect3d intersectPlane(Ray r) {
        Vect3d ab = b.copy().sub(a);
        Vect3d ac = c.copy().sub(a);
        Vect3d n = ab.crossProduct(ac);
        Vect3d v = r.getEnd().sub(r.getStart());
        Vect3d w = a.copy().sub(r.getStart());
        double div = v.dotProduct(n);
        if (Math.abs(div) < 0.0000000001) {
            return null;
        }
        double k = w.dotProduct(n) / v.dotProduct(n);
        return r.getStart().add(v.copy().multiply(k));
    }

    @Override
    public RayTraceResult rayTrace(Ray ray) {
        Vect3d i = intersectPlane(ray);
        if (i == null) return null;

        Vect3d edge0 = b.copy().sub(a);
        Vect3d edge1 = c.copy().sub(b);
        Vect3d edge2 = a.copy().sub(c);

        Vect3d C0 = i.copy().sub(a);
        Vect3d C1 = i.copy().sub(b);
        Vect3d C2 = i.copy().sub(c);

        Vect3d ab = b.copy().sub(a);
        Vect3d ac = c.copy().sub(a);
        Vect3d N = ab.crossProduct(ac);

        if (N.dotProduct(edge0.crossProduct(C0)) >= 0 &&
                N.dotProduct(edge1.crossProduct(C1)) >= 0 &&
                N.dotProduct(edge2.crossProduct(C2)) >= 0) {
            return new RayTraceResult(ray, i, this);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                '}';
    }
}
