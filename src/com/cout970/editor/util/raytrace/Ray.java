package com.cout970.editor.util.raytrace;

import com.cout970.editor.util.Vect3d;

/**
 * Created by cout970 on 16/02/2016.
 */
public class Ray {

    private Vect3d start;
    private Vect3d end;

    public Ray(Vect3d start, Vect3d end) {
        this.start = start.copy();
        this.end = end.copy();
    }

    public Vect3d getStart() {
        return start.copy();
    }

    public void setStart(Vect3d start) {
        this.start = start;
    }

    public Vect3d getEnd() {
        return end.copy();
    }

    public void setEnd(Vect3d end) {
        this.end = end;
    }

    public Ray copy(){
        return new Ray(start, end);
    }

    @Override
    public String toString() {
        return "Ray{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
