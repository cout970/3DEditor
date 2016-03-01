package com.cout970.editor.render.examples;

import com.cout970.editor.util.Vect3d;

/**
 * Created by cout970 on 17/02/2016.
 */
public class Lines {

    public static void line(Vect3d a, Vect3d b) {
        Cube c = new Cube(a.copy().add(new Vect3d(1, 1, 1).multiply(-0.0625 / 16)), b.copy().add(new Vect3d(1, 1, 1).multiply(0.0625 / 16)));
        c.render();
    }

    public static void cubeSelection(Vect3d size) {
        Vect3d start = Vect3d.nullVector();
        Vect3d end = size;
        Vect3d vertex1 = new Vect3d(start.getX(), start.getY(), start.getZ());
        Vect3d vertex2 = new Vect3d(end.getX(), start.getY(), start.getZ());
        Vect3d vertex3 = new Vect3d(end.getX(), end.getY(), start.getZ());
        Vect3d vertex4 = new Vect3d(start.getX(), end.getY(), start.getZ());
        Vect3d vertex5 = new Vect3d(start.getX(), start.getY(), end.getZ());
        Vect3d vertex6 = new Vect3d(end.getX(), start.getY(), end.getZ());
        Vect3d vertex7 = new Vect3d(end.getX(), end.getY(), end.getZ());
        Vect3d vertex8 = new Vect3d(start.getX(), end.getY(), end.getZ());

        Vect3d[] down = {vertex6.copy(), vertex5.copy(), vertex1.copy(), vertex2.copy()};
        Vect3d[] up = {vertex3.copy(), vertex4.copy(), vertex8.copy(), vertex7.copy()};
        Vect3d[] north = {vertex2.copy(), vertex1.copy(), vertex4.copy(), vertex3.copy()};
        Vect3d[] south = {vertex5.copy(), vertex6.copy(), vertex7.copy(), vertex8.copy()};
        Vect3d[] west = {vertex6.copy(), vertex2.copy(), vertex3.copy(), vertex7.copy()};
        Vect3d[] east = {vertex1.copy(), vertex5.copy(), vertex8.copy(), vertex4.copy()};

        Vect3d[][] quads = {down, up, north, south, west, east};

        for (Vect3d[] quad : quads) {
            for (int i = 0; i < 4; i++) {
                line(quad[i], quad[(i + 1) % 4]);
            }
        }
    }
}
