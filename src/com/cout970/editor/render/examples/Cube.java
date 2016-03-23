package com.cout970.editor.render.examples;

import com.cout970.editor.util.Vect3d;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by cout970 on 17/02/2016.
 */
public class Cube {

    private Vect3d start;
    private Vect3d end;

    public Cube(Vect3d start, Vect3d end) {
        this.start = start;
        this.end = end;
    }

    public Vect3d getStart() {
        return start;
    }

    public void setStart(Vect3d start) {
        this.start = start;
    }

    public Vect3d getEnd() {
        return end;
    }

    public void setEnd(Vect3d end) {
        this.end = end;
    }

    public void render() {
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

        glBegin(GL_QUADS);
        glColor4f(1f, 1f, 0f, 1f);
        //y
        for (Vect3d v : down)
            glVertex3d(v.getX(), v.getY(), v.getZ());
        for (Vect3d v : up)
            glVertex3d(v.getX(), v.getY(), v.getZ());
        //z
        for (Vect3d v : north)
            glVertex3d(v.getX(), v.getY(), v.getZ());
        for (Vect3d v : south)
            glVertex3d(v.getX(), v.getY(), v.getZ());
        //x
        for (Vect3d v : west)
            glVertex3d(v.getX(), v.getY(), v.getZ());
        for (Vect3d v : east)
            glVertex3d(v.getX(), v.getY(), v.getZ());
        glEnd();
    }


}
