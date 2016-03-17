package com.cout970.editor2.util.raytrace;

import com.cout970.editor2.util.Vect3d;

/**
 * Created by cout970 on 16/02/2016.
 */
public class Quad implements IRayObstacle {

    private Vect3d a;
    private Vect3d b;
    private Vect3d c;
    private Vect3d d;

    public Quad(Vect3d a, Vect3d b, Vect3d c, Vect3d d) {
        this.a = a.copy();
        this.b = b.copy();
        this.c = c.copy();
        this.d = d.copy();
    }

    public void setVertex(int pos, Vect3d vertex) {
        switch (pos) {
            case 0:
                setA(vertex);
            case 1:
                setB(vertex);
            case 2:
                setC(vertex);
            case 3:
                setD(vertex);
        }
    }

    public Vect3d getVertex(int pos) {
        switch (pos) {
            case 0:
                return getA();
            case 1:
                return getB();
            case 2:
                return getC();
            case 3:
                return getD();
            default:
                return null;
        }
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

    public Vect3d getD() {
        return d.copy();
    }

    public void setD(Vect3d d) {
        this.d = d;
    }

    @Override
    public RayTraceResult rayTrace(Ray ray) {
        Triangle upper = new Triangle(a, b, c);
        RayTraceResult r0 = upper.rayTrace(ray);
        if (r0 != null) {
            r0.setObject(this);
            return r0;
        }
        Triangle lower = new Triangle(a, d, c);
        RayTraceResult r1 = lower.rayTrace(ray);
        if (r1 != null) {
            r1.setObject(this);
            return r1;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Quad{" +
                "d=" + d +
                ", c=" + c +
                ", b=" + b +
                ", a=" + a +
                '}';
    }
}
