package com.cout970.editor.model;

import com.cout970.editor.util.raytrace.IRayObstacle;

import java.util.List;

/**
 * Created by cout970 on 11/02/2016.
 */
public interface IModel {

    void render(boolean selected);

    List<IRayObstacle> getRayObstacles();
}
