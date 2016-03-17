package com.cout970.editor2.model;

import com.cout970.editor2.util.raytrace.IRayObstacle;

import java.util.List;

/**
 * Created by cout970 on 11/02/2016.
 */
public interface IModel {

    void render(boolean selected);

    List<IRayObstacle> getRayObstacles();

    boolean isVisible();

    void setVisible(boolean b);
}
