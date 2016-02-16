package com.cout970.editor.util.raytrace;

import com.cout970.editor.util.Vect3d;

/**
 * Created by cout970 on 16/02/2016.
 */
public class RayTraceResult {

    private Ray ray;
    private Vect3d hit;
    private IRayObstacle object;

    public RayTraceResult(Ray ray, Vect3d hit, IRayObstacle object) {
        this.ray = ray;
        this.hit = hit;
        this.object = object;
    }

    public Ray getRay() {
        return ray.copy();
    }

    public void setRay(Ray ray) {
        this.ray = ray;
    }

    public Vect3d getHit() {
        return hit.copy();
    }

    public void setHit(Vect3d hit) {
        this.hit = hit;
    }

    public IRayObstacle getObject() {
        return object;
    }

    public void setObject(IRayObstacle object) {
        this.object = object;
    }
}
