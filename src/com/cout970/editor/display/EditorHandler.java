package com.cout970.editor.display;

import com.cout970.editor.ConfigurationFile;
import com.cout970.editor.Editor;
import com.cout970.editor.ModelTree;
import com.cout970.editor.model.IModel;
import com.cout970.editor.model.TechneCube;
import com.cout970.editor.render.examples.Cube;
import com.cout970.editor.render.examples.Lines;
import com.cout970.editor.render.examples.Planes;
import com.cout970.editor.render.examples.Sphere;
import com.cout970.editor.render.texture.TextureStorage;
import com.cout970.editor.util.RotationVect;
import com.cout970.editor.util.Vect2d;
import com.cout970.editor.util.Vect2i;
import com.cout970.editor.util.Vect3d;
import com.cout970.editor.util.raytrace.IRayObstacle;
import com.cout970.editor.util.raytrace.ProjectionUtil;
import com.cout970.editor.util.raytrace.Ray;
import com.cout970.editor.util.raytrace.RayTraceResult;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created by cout970 on 10/02/2016.
 */
public class EditorHandler implements InputHandler.IMouseWheelCallback, InputHandler.IMouseButtonCallback, InputHandler.IKeyboardCallback {

    private ConfigurationFile config = ConfigurationFile.INSTANCE;
    private RotationVect cameraRotation = new RotationVect(30, -45);
    private Vect3d cameraTranslation = new Vect3d(-1, -1, -2);
    private static Sphere sphere;
    private Cube baseBlock;

    public EditorHandler() {
        baseBlock = new Cube(new Vect3d(-1, -1, -1), Vect3d.nullVector());
    }

    public void update() {
        setCamera();
        TextureStorage.EMPTY.bind();

        if (config.showAxisGridY) {
            Planes.drawAxisGridY(config.axisGridY);
        }
        if (config.showAxisGridX) {
            Planes.drawAxisGridX(config.axisGridX);
        }
        if (config.showAxisGridZ) {
            Planes.drawAxisGridZ(config.axisGridZ);
        }
        Lines.drawDebugLines();

        TextureStorage.CUBE.bind();
        glColor4f(1, 1, 1, 1);
        baseBlock.render();

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
                TextureStorage.EMPTY.bind();
                glColor4f(0, 0, 1, 1);
                sphere.render();
                glPopMatrix();
            }
        }
    }

    private void setCamera() {
        int WIDTH = Display.getWindowWidth();
        int HEIGHT = Display.getWindowHeight();

        glViewport(0, 0, WIDTH, HEIGHT);
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        Matrix4f m = new Matrix4f();
        glMatrixMode(GL_PROJECTION);
        m.setPerspective((float) Math.toRadians(60), WIDTH / (float) HEIGHT, 0.001f, 1000f).get(fb);

        glLoadMatrixf(fb);

        handleMouseMotion();
        if (config.cameraController == 0) {

            Vect3d rotP = new Vect3d(0, 0, 2);
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
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
    }

    private void handleMouseMotion() {
        if (InputHandler.isMouseButtonPress(InputHandler.MouseButton.MIDDLE)) {

            Vect2d axisX = new Vect2d(Math.cos(Math.toRadians(cameraRotation.getYaw())), Math.sin(Math.toRadians(cameraRotation.getYaw())));
            Vect2d axisY = new Vect2d(Math.cos(Math.toRadians(cameraRotation.getYaw() - 90)), Math.sin(Math.toRadians(cameraRotation.getYaw() - 90)));
            axisY.multiply(Math.sin(Math.toRadians(cameraRotation.getPitch())));
            Vect3d a = new Vect3d(axisX.getX(), 0, axisX.getY());
            Vect3d b = new Vect3d(axisY.getX(), Math.cos(Math.toRadians(cameraRotation.getPitch())), axisY.getY());
            a.normalize().multiply(-InputHandler.getCursorDiffX() * config.translationSpeedX * Display.getDeltaSec());
            b.normalize().multiply(InputHandler.getCursorDiffY() * config.translationSpeedY * Display.getDeltaSec());
            cameraTranslation.add(a);
            cameraTranslation.add(b);

        } else if (InputHandler.isMouseButtonPress(InputHandler.MouseButton.RIGHT)) {

            Vect2d rotation = cameraRotation.toVect2d();
            rotation.add(-InputHandler.getCursorDiffX() * config.rotationSpeedX * Display.getDeltaSec(), -InputHandler.getCursorDiffY() * config.rotationSpeedY * Display.getDeltaSec());
            cameraRotation.set(rotation);
        }
    }

    @Override
    public void onWheelMoves(double amount) {
        if (!Editor.getGuiHandler().getGui().blockMouse()) {
            Vect3d a = new Vect3d(0, 0, 1);
            a.rotateX(Math.toRadians(cameraRotation.getPitch()));
            a.rotateY(Math.toRadians(180 - cameraRotation.getYaw()));
            a.normalize().multiply(-amount * Planes.pixel);
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
        sphere = new Sphere(0.0625f);
        InputHandler.registerMouseButtonCallback(this);
        InputHandler.registerKeyboardCallback(this);
        InputHandler.registerMouseWheelCallback(this);
    }

    @Override
    public void onMouseClick(Vect2i pos, InputHandler.MouseButton button, int action) {
        if (action == GLFW_PRESS && button == InputHandler.MouseButton.LEFT && !Editor.getGuiHandler().getGui().blockMouse()) {

            glPushMatrix();
            setCamera();

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
        if (action == GLFW_PRESS) {
            if (key == GLFW_KEY_DELETE) {
                List<IModel> list = Editor.getProject().getModelTree().getSelectedModels();
                if (!list.isEmpty()) {
                    Editor.getProject().getModelTree().removeModels(list);
                }
            }
        }
    }

}
