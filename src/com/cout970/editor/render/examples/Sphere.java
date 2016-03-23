package com.cout970.editor.render.examples;

import com.cout970.editor.util.Vect3d;

import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by cout970 on 15/02/2016.
 */
public class Sphere {

    private double radius;
    private int lista;

    public Sphere(double radius) {
        this.radius = radius;
        init();
    }

    private void init() {
        List<Cara> caras = new LinkedList<>();

        double incremento = 0.1;
        Vect3d[] verts = new Vect3d[4];

        for (double j = -Math.PI; j < Math.PI; j += incremento) {
            for (double i = -Math.PI / 2; i < Math.PI / 2; i += incremento) {

                verts[3] = new Vect3d(
                        radius * Math.cos(i) * Math.cos(j),
                        radius * Math.cos(i) * Math.sin(j),
                        radius * Math.sin(i));
                verts[2] = new Vect3d(
                        radius * Math.cos(i + incremento) * Math.cos(j),
                        radius * Math.cos(i + incremento) * Math.sin(j),
                        radius * Math.sin(i + incremento));
                verts[1] = new Vect3d(
                        radius * Math.cos(i + incremento) * Math.cos(j + incremento),
                        radius * Math.cos(i + incremento) * Math.sin(j + incremento),
                        radius * Math.sin(i + incremento));
                verts[0] = new Vect3d(
                        radius * Math.cos(i) * Math.cos(j + incremento),
                        radius * Math.cos(i) * Math.sin(j + incremento),
                        radius * Math.sin(i));

                caras.add(new Cara(verts[0], verts[1], verts[2], verts[3]));
            }
        }

        lista = glGenLists(1);
        glNewList(lista, GL_COMPILE);
        glBegin(GL_QUADS);

        for (Cara cara : caras) {
            Vect3d v = cara.getA();
            Vect3d n = cara.getNormal();
            glNormal3d(n.getX(), n.getY(), n.getZ());
            glVertex3d(v.getX(), v.getY(), v.getZ());
            v = cara.getB();
            glNormal3d(n.getX(), n.getY(), n.getZ());
            glVertex3d(v.getX(), v.getY(), v.getZ());
            v = cara.getC();
            glNormal3d(n.getX(), n.getY(), n.getZ());
            glVertex3d(v.getX(), v.getY(), v.getZ());
            v = cara.getD();
            glNormal3d(n.getX(), n.getY(), n.getZ());
            glVertex3d(v.getX(), v.getY(), v.getZ());
        }

        glEnd();
        glEndList();
    }

    public void render() {
        glCallList(lista);
    }

    private static class Cara {
        private Vect3d a, b, c, d;

        public Cara(Vect3d a, Vect3d b, Vect3d c, Vect3d d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }

        public Vect3d getA() {
            return a;
        }

        public Vect3d getB() {
            return b;
        }

        public Vect3d getC() {
            return c;
        }

        public Vect3d getD() {
            return d;
        }

        public Vect3d getNormal() {
            return a.copy().sub(b).crossProduct(c.copy().sub(b));
        }
    }
}
