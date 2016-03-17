package com.cout970.editor2.display;

import com.cout970.editor2.ConfigurationFile;
import com.cout970.editor2.Editor;
import com.cout970.editor2.ModelTree;
import com.cout970.editor2.model.IModel;
import com.cout970.editor2.model.TechneCube;
import com.cout970.editor2.render.engine.IRenderEngine;
import com.cout970.editor2.render.examples.Sphere;
import com.cout970.editor2.render.texture.TextureStorage;
import com.cout970.editor2.util.RotationVect;
import com.cout970.editor2.util.Vect2d;
import com.cout970.editor2.util.Vect2i;
import com.cout970.editor2.util.Vect3d;
import com.cout970.editor2.util.raytrace.IRayObstacle;
import com.cout970.editor2.util.raytrace.ProjectionUtil;
import com.cout970.editor2.util.raytrace.Ray;
import com.cout970.editor2.util.raytrace.RayTraceResult;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by cout970 on 10/02/2016.
 */
public class Handler3D implements InputHandler.IMouseWheelCallback, InputHandler.IMouseButtonCallback, InputHandler.IKeyboardCallback {

    private ConfigurationFile config = ConfigurationFile.INSTANCE;
    private IRenderEngine engine = IRenderEngine.INSTANCE;
    private RotationVect cameraRotation = new RotationVect(30, -45);
    private Vect3d cameraTranslation = new Vect3d(-1, -1, -2);
    private static final double pixel = 1 / 16d;
    private static Sphere sphere;

    public Handler3D() {
    }

    public void update() {
        GLFWDisplay.set3D();
        TextureStorage.EMPTY.bind();
        glPushMatrix();
        glLoadIdentity();
        handleCursor();

        if (config.cameraController == 0) {
            Vect3d rotP = new Vect3d(0, 0, 1);
            glTranslated(-rotP.getX(), -rotP.getY(), -rotP.getZ());
            glRotatef(cameraRotation.getPitch(), 1, 0, 0);
            glRotatef(cameraRotation.getYaw(), 0, 1, 0);
            glTranslated(rotP.getX(), rotP.getY(), rotP.getZ());
            glTranslated(cameraTranslation.getX(), cameraTranslation.getY(), cameraTranslation.getZ());
        } else if (config.cameraController == 1) {
            glRotatef(cameraRotation.getPitch(), 1, 0, 0);
            glRotatef(cameraRotation.getYaw(), 0, 1, 0);
            glTranslated(cameraTranslation.getX(), cameraTranslation.getY(), cameraTranslation.getZ());
        }

        if (config.showAxisGridY) {
            drawAxisGridY(config.axisGridY);
        }
        if (config.showAxisGridX) {
            drawAxisGridX(config.axisGridX);
        }
        if (config.showAxisGridZ) {
            drawAxisGridZ(config.axisGridZ);
        }
        drawDebugLines();
        ModelTree tree = Editor.getProject().getModelTree();
        for (IModel m : tree.getModelsToRender()) {
            m.render(tree.getSelectedModels().contains(m));
        }
        if (Editor.getProject().getModelTree().getSelectedModels().size() == 1) {
            IModel m = Editor.getProject().getModelTree().getSelectedModels().get(0);
            if (m instanceof TechneCube) {
                glPushMatrix();
                Vect3d v = ((TechneCube) m).getRotationPoint();
                glTranslated(v.getX(), v.getY(), v.getZ());
                sphere.render();
                glPopMatrix();
            }
        }
        glPopMatrix();
    }

    private void handleCursor() {
        if (InputHandler.isMouseButtonPress(InputHandler.MouseButton.MIDDLE)) {

            Vect2d axisX = new Vect2d(Math.cos(Math.toRadians(cameraRotation.getYaw())), Math.sin(Math.toRadians(cameraRotation.getYaw())));
            Vect2d axisY = new Vect2d(Math.cos(Math.toRadians(cameraRotation.getYaw() - 90)), Math.sin(Math.toRadians(cameraRotation.getYaw() - 90)));
            axisY.multiply(Math.sin(Math.toRadians(cameraRotation.getPitch())));
            Vect3d a = new Vect3d(axisX.getX(), 0, axisX.getY());
            Vect3d b = new Vect3d(axisY.getX(), Math.cos(Math.toRadians(cameraRotation.getPitch())), axisY.getY());
            a.normalize().multiply(-InputHandler.getCursorDiffX() * config.translationSpeedX * GLFWDisplay.getDeltaSec());
            b.normalize().multiply(InputHandler.getCursorDiffY() * config.translationSpeedY * GLFWDisplay.getDeltaSec());
            cameraTranslation.add(a);
            cameraTranslation.add(b);

        } else if (InputHandler.isMouseButtonPress(InputHandler.MouseButton.RIGHT)) {

            Vect2d rotation = cameraRotation.toVect2d();
            rotation.add(-InputHandler.getCursorDiffX() * config.rotationSpeedX * GLFWDisplay.getDeltaSec(), -InputHandler.getCursorDiffY() * config.rotationSpeedY * GLFWDisplay.getDeltaSec());
            cameraRotation.set(rotation);
        }
    }

    private void drawDebugLines() {
        glLineWidth(3f);
        engine.startDrawing(GL_LINES);
        engine.setColorOpaque(0x0000FF);
        engine.addVertex(0, 0, 0);
        engine.addVertex(0, 0, 1);
        engine.endDrawing();
        engine.startDrawing(GL_LINES);
        engine.setColorOpaque(0xFF0000);
        engine.addVertex(0, 0, 0);
        engine.addVertex(1, 0, 0);
        engine.endDrawing();
        engine.startDrawing(GL_LINES);
        engine.setColorOpaque(0x00FF00);
        engine.addVertex(0, 0, 0);
        engine.addVertex(0, 1, 0);
        engine.endDrawing();
    }

    @Override
    public void onWheelMoves(double amount) {
        if (!GLFWDisplay.handler2D.getGui().blockMouse()) {
            Vect3d a = new Vect3d(0, 0, 1);
            a.rotateX(Math.toRadians(cameraRotation.getPitch()));
            a.rotateY(Math.toRadians(180 - cameraRotation.getYaw()));
            a.normalize().multiply(-amount * pixel);
            cameraTranslation.add(a);
        }
    }

    private Vect3d getLookVector() {
        Vect3d a = new Vect3d(0, 0, 1);
        a.rotateX(Math.toRadians(cameraRotation.getPitch()));
        a.rotateY(Math.toRadians(180 - cameraRotation.getYaw()));
        return a.normalize().multiply(-1);
    }

    public void init() {
        sphere = new Sphere(0.0625f, TextureStorage.ROTATION_POINT);
        InputHandler.registerMouseButtonCallback(this);
        InputHandler.registerKeyboardCallback(this);
    }

    @Override
    public void onMouseClick(Vect2i pos, InputHandler.MouseButton button, int action) {
        if (action == GLFW_PRESS && button == InputHandler.MouseButton.LEFT && !GLFWDisplay.handler2D.getGui().blockMouse()) {
            GLFWDisplay.set3D();
            glPushMatrix();
            glLoadIdentity();

            if (config.cameraController == 0) {
                Vect3d rotP = new Vect3d(0, 0, 1);
                glTranslated(-rotP.getX(), -rotP.getY(), -rotP.getZ());
                glRotatef(cameraRotation.getPitch(), 1, 0, 0);
                glRotatef(cameraRotation.getYaw(), 0, 1, 0);
                glTranslated(rotP.getX(), rotP.getY(), rotP.getZ());
                glTranslated(cameraTranslation.getX(), cameraTranslation.getY(), cameraTranslation.getZ());
            } else if (config.cameraController == 1) {
                glRotatef(cameraRotation.getPitch(), 1, 0, 0);
                glRotatef(cameraRotation.getYaw(), 0, 1, 0);
                glTranslated(cameraTranslation.getX(), cameraTranslation.getY(), cameraTranslation.getZ());
            }

            Ray ray = ProjectionUtil.getRay(pos);
            LinkedHashMap<RayTraceResult, IModel> hits = new LinkedHashMap<>();
            for (IModel m : Editor.getProject().getModelTree().getAllVisibleModels()) {
                for (IRayObstacle r : m.getRayObstacles()) {
                    RayTraceResult res = r.rayTrace(ray);
                    if (res != null) {
                        hits.put(res, m);
                    }
                }
            }
            double dist = 0;
            RayTraceResult best = null;
            IModel model = null;
            for (Map.Entry<RayTraceResult, IModel> e : hits.entrySet()) {
                RayTraceResult o = e.getKey();
                if (best == null) {
                    best = o;
                    dist = o.getHit().distance(ray.getStart());
                    model = e.getValue();
                } else if (o.getHit().distance(ray.getStart()) < dist) {
                    best = o;
                    dist = o.getHit().distance(ray.getStart());
                    model = e.getValue();
                }
            }
            if (!InputHandler.isKeyDown(GLFW_KEY_LEFT_CONTROL)) {
                Editor.getProject().getModelTree().clearSelection();
            }
            if (best != null) {
                if (Editor.getProject().getModelTree().getSelectedModels().contains(model)) {
                    Editor.getProject().getModelTree().removeModelFromSelection(model);
                } else {
                    Editor.getProject().getModelTree().addModelToSelection(model);
                }
            }

            glPopMatrix();
        }
    }

    @Override
    public void onKeyPress(int key, int code, int action) {
        if (action == GLFW_PRESS){
            if (key == GLFW_KEY_DELETE){
                List<IModel> list = Editor.getProject().getModelTree().getSelectedModels();
                if (!list.isEmpty()){
                    Editor.getProject().getModelTree().removeModels(list);
                }
            }
        }
    }

    private void drawAxisGridY(double y) {
        int lenght = 3;
        glLineWidth(1f);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_DST_ALPHA);
        engine.startDrawing(GL_LINES);
        engine.setColor(0x979694, 0.5f);
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 == 0) {
                continue;
            }
            engine.addVertex(i * pixel, y, (-lenght * 16 - 2) * pixel);
            engine.addVertex(i * pixel, y, ((lenght + 1) * 16 + 2) * pixel);
        }
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 == 0) {
                continue;
            }
            engine.addVertex((-lenght * 16 - 2) * pixel, y, i * pixel);
            engine.addVertex(((lenght + 1) * 16 + 2) * pixel, y, i * pixel);
        }
        engine.endDrawing();
        glLineWidth(2f);
        engine.startDrawing(GL_LINES);
        engine.setColor(0x555555, 0.5f);
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 != 0) {
                continue;
            }
            engine.addVertex(i * pixel, y, (-lenght * 16 - 2) * pixel);
            engine.addVertex(i * pixel, y, ((lenght + 1) * 16 + 2) * pixel);
        }
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 != 0) {
                continue;
            }
            engine.addVertex((-lenght * 16 - 2) * pixel, y, i * pixel);
            engine.addVertex(((lenght + 1) * 16 + 2) * pixel, y, i * pixel);
        }
        engine.endDrawing();
        glDisable(GL_BLEND);
    }

    private void drawAxisGridX(double x) {
        int lenght = 3;
        glLineWidth(1f);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_DST_ALPHA);
        engine.startDrawing(GL_LINES);
        engine.setColor(0x979694, 0.5f);
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 == 0) {
                continue;
            }
            engine.addVertex(x, i * pixel, (-lenght * 16 - 2) * pixel);
            engine.addVertex(x, i * pixel, ((lenght + 1) * 16 + 2) * pixel);
        }
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 == 0) {
                continue;
            }
            engine.addVertex(x, (-lenght * 16 - 2) * pixel, i * pixel);
            engine.addVertex(x, ((lenght + 1) * 16 + 2) * pixel, i * pixel);
        }
        engine.endDrawing();
        glLineWidth(2f);
        engine.startDrawing(GL_LINES);
        engine.setColor(0x555555, 0.5f);
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 != 0) {
                continue;
            }
            engine.addVertex(x, i * pixel, (-lenght * 16 - 2) * pixel);
            engine.addVertex(x, i * pixel, ((lenght + 1) * 16 + 2) * pixel);
        }
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 != 0) {
                continue;
            }
            engine.addVertex(x, (-lenght * 16 - 2) * pixel, i * pixel);
            engine.addVertex(x, ((lenght + 1) * 16 + 2) * pixel, i * pixel);
        }
        engine.endDrawing();
        glDisable(GL_BLEND);
    }

    private void drawAxisGridZ(double z) {
        int lenght = 3;
        glLineWidth(1f);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_DST_ALPHA);
        engine.startDrawing(GL_LINES);
        engine.setColor(0x979694, 0.5f);
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 == 0) {
                continue;
            }
            engine.addVertex(i * pixel, (-lenght * 16 - 2) * pixel, z);
            engine.addVertex(i * pixel, ((lenght + 1) * 16 + 2) * pixel, z);
        }
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 == 0) {
                continue;
            }
            engine.addVertex((-lenght * 16 - 2) * pixel, i * pixel, z);
            engine.addVertex(((lenght + 1) * 16 + 2) * pixel, i * pixel, z);
        }
        engine.endDrawing();
        glLineWidth(2f);
        engine.startDrawing(GL_LINES);
        engine.setColor(0x555555, 0.5f);
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 != 0) {
                continue;
            }
            engine.addVertex(i * pixel, (-lenght * 16 - 2) * pixel, z);
            engine.addVertex(i * pixel, ((lenght + 1) * 16 + 2) * pixel, z);
        }
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 != 0) {
                continue;
            }
            engine.addVertex((-lenght * 16 - 2) * pixel, i * pixel, z);
            engine.addVertex(((lenght + 1) * 16 + 2) * pixel, i * pixel, z);
        }
        engine.endDrawing();
        glDisable(GL_BLEND);
    }
}
