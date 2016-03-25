package com.cout970.editor.render.examples;

import com.cout970.editor.util.Vect2d;
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
        Vect2d[] uv = {new Vect2d(0, 0), new Vect2d(1, 0), new Vect2d(1, 1), new Vect2d(0, 1)};

        int count = 0;
        glBegin(GL_QUADS);
        //y
        for (Vect3d v : down) {
            glTexCoord2d(uv[count].getX(), uv[count].getY());
            count++;
            glVertex3d(v.getX(), v.getY(), v.getZ());
        }
        count = 0;
        for (Vect3d v : up) {
            glTexCoord2d(uv[count].getX(), uv[count].getY());
            count++;
            glVertex3d(v.getX(), v.getY(), v.getZ());
        }
        count = 0;
        //z
        for (Vect3d v : north) {
            glTexCoord2d(uv[count].getX(), uv[count].getY());
            count++;
            glVertex3d(v.getX(), v.getY(), v.getZ());
        }
        count = 0;
        for (Vect3d v : south) {
            glTexCoord2d(uv[count].getX(), uv[count].getY());
            count++;
            glVertex3d(v.getX(), v.getY(), v.getZ());
        }
        count = 0;
        //x
        for (Vect3d v : west) {
            glTexCoord2d(uv[count].getX(), uv[count].getY());
            count++;
            glVertex3d(v.getX(), v.getY(), v.getZ());
        }
        count = 0;
        for (Vect3d v : east) {
            glTexCoord2d(uv[count].getX(), uv[count].getY());
            count++;
            glVertex3d(v.getX(), v.getY(), v.getZ());
        }
        glEnd();
    }


}
