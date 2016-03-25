package com.cout970.editor.render.examples;

import com.cout970.editor.util.Vect3d;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by cout970 on 17/02/2016.
 */
public class Lines {

    public static void line(Vect3d a, Vect3d b) {
        new Cube(a.copy().add(new Vect3d(1, 1, 1).multiply(-0.0625 / 16)),
                b.copy().add(new Vect3d(1, 1, 1).multiply(0.0625 / 16))).render();
    }

    public static void cubeSelection(Vect3d size) {
        Vect3d start = Vect3d.nullVector();
        Vect3d vertex1 = new Vect3d(start.getX(), start.getY(), start.getZ());
        Vect3d vertex2 = new Vect3d(size.getX(), start.getY(), start.getZ());
        Vect3d vertex3 = new Vect3d(size.getX(), size.getY(), start.getZ());
        Vect3d vertex4 = new Vect3d(start.getX(), size.getY(), start.getZ());
        Vect3d vertex5 = new Vect3d(start.getX(), start.getY(), size.getZ());
        Vect3d vertex6 = new Vect3d(size.getX(), start.getY(), size.getZ());
        Vect3d vertex7 = new Vect3d(size.getX(), size.getY(), size.getZ());
        Vect3d vertex8 = new Vect3d(start.getX(), size.getY(), size.getZ());

        Vect3d[] down = {vertex6.copy(), vertex5.copy(), vertex1.copy(), vertex2.copy()};
        Vect3d[] up = {vertex3.copy(), vertex4.copy(), vertex8.copy(), vertex7.copy()};
        Vect3d[] north = {vertex2.copy(), vertex1.copy(), vertex4.copy(), vertex3.copy()};
        Vect3d[] south = {vertex5.copy(), vertex6.copy(), vertex7.copy(), vertex8.copy()};
        Vect3d[] west = {vertex6.copy(), vertex2.copy(), vertex3.copy(), vertex7.copy()};
        Vect3d[] east = {vertex1.copy(), vertex5.copy(), vertex8.copy(), vertex4.copy()};

        Vect3d[][] quads = {down, up, north, south, west, east};

        glColor4f(1, 1, 0, 1);
        for (Vect3d[] quad : quads) {
            for (int i = 0; i < 4; i++) {
                line(quad[i], quad[(i + 1) % 4]);
            }
        }
    }

    public static void drawDebugLines() {
        glLineWidth(3f);
        glBegin(GL_LINES);
        glColor3f(0, 0, 1f);
        glVertex3d(0, 0, 0);
        glVertex3d(0, 0, 1);
        glEnd();
        glBegin(GL_LINES);
        glColor3f(1f, 0, 0);
        glVertex3d(0, 0, 0);
        glVertex3d(1, 0, 0);
        glEnd();
        glBegin(GL_LINES);
        glColor3f(0, 1f, 0);
        glVertex3d(0, 0, 0);
        glVertex3d(0, 1, 0);
        glEnd();
    }
}
