package com.cout970.editor.render.examples;

import com.cout970.editor.render.engine.DisplayList;
import com.cout970.editor.render.engine.IRenderEngine;
import com.cout970.editor.render.texture.ITexture;
import org.lwjgl.opengl.GL11;

/**
 * Created by cout970 on 15/02/2016.
 */
public class Sphere {

    private DisplayList list;
    private double radius;
    private ITexture texture;

    public Sphere(double radius, ITexture texture) {
        this.radius = radius;
        this.texture = texture;
        init();
    }

    private void init() {
        list = new DisplayList();
        IRenderEngine eng = IRenderEngine.INSTANCE;
        eng.startCompile(list);
        eng.startDrawing(GL11.GL_TRIANGLE_STRIP);

        double increment = 0.1;
        for (double j = -Math.PI; j < Math.PI; j += increment) {
            for (double i = -Math.PI / 2; i < Math.PI / 2; i += increment) {
                for (int k = 0; k < 2; k++) {
                    eng.addTextureUV(0, 0);
                    eng.addVertex(
                            radius * Math.cos(i + increment * k) * Math.cos(j + increment * k),
                            radius * Math.cos(i + increment * k) * Math.sin(j + increment * k),
                            radius * Math.sin(i + increment * k));
                }
            }
        }
        eng.endDrawing();
        eng.endCompile();
    }

    public void render() {
        texture.bind();
        IRenderEngine.INSTANCE.render(list);
    }
}
