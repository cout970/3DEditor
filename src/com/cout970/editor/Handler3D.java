package com.cout970.editor;

import com.cout970.editor.render.engine.IRenderEngine;
import com.cout970.editor.util.Log;
import com.cout970.editor.util.RotationVect;
import com.cout970.editor.util.Vect2d;
import com.cout970.editor.util.Vect3d;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by cout970 on 10/02/2016.
 */
public class Handler3D {

    private ConfigurationFile config = ConfigurationFile.INSTANCE;
    private IRenderEngine engine = IRenderEngine.INSTANCE;
    private RotationVect cameraRotation = new RotationVect(0, 0);
    private Vect3d cameraTranslation = new Vect3d(0, 0, 0);

    public void update() {
        glPushMatrix();
        handleCursor();
        glRotatef(cameraRotation.getYaw(), 0, 1, 0);
        glRotatef(cameraRotation.getPitch(), 1, 0, 0);
        glRotatef(cameraRotation.getPitch(), 0, 0, 1);
        glTranslated(cameraTranslation.getX(), cameraTranslation.getY(), cameraTranslation.getZ());

        if (config.showAxisGridY) {
            drawAxisGridY();
        }
        drawDebugLines();
        glPopMatrix();
    }

    private void handleCursor() {
        if (InputHandler.isMouseButtonPress(InputHandler.MouseButton.MIDDLE)) {
            Vect3d rot;
            rot = new Vect3d(Math.cos(Math.toRadians(cameraRotation.getYaw())), Math.cos(Math.toRadians(cameraRotation.getPitch())), Math.sin(Math.toRadians(cameraRotation.getYaw())));
//            rot = new Vect3d(1,0,0);
            rot.normalize();
            Log.debug(rot);
            rot.multiply(new Vect3d(-InputHandler.getCursorDiffX() * 0.15D, InputHandler.getCursorDiffY() * 0.15D, -InputHandler.getCursorDiffX() * 0.15D));
            cameraTranslation.add(rot);
        } else if (InputHandler.isMouseButtonPress(InputHandler.MouseButton.RIGHT)) {
            Vect2d rotation = cameraRotation.toVect2d();
            rotation.add(InputHandler.getCursorDiffY() * -0.15D, InputHandler.getCursorDiffX() * -0.15D);
            if (rotation.getX() > 90)
                rotation.set(90, rotation.getY());
            if (rotation.getX() < -90)
                rotation.set(-90, rotation.getY());

            if (rotation.getY() > 360)
                rotation.add(0, -360);
            if (rotation.getY() < 0)
                rotation.add(0, 360);
            cameraRotation.set(rotation);
        }
    }

    private void drawAxisGridY() {
        engine.setColorOpaque(0xFFFFFF);
        int lenght = 5;
        engine.startDrawing(GL_LINES);
        for (int i = 0; i <= 16*lenght; i++) {
            engine.addVertex(i, 0, 0);
            engine.addVertex(i, 0, lenght*16);
        }
        for (int i = 0; i <= 16*lenght; i++) {
            engine.addVertex(0, 0, i);
            engine.addVertex(lenght*16, 0, i);
        }
        engine.endDrawing();
    }

    private void drawDebugLines(){
        engine.startDrawing(GL_LINES);
        engine.setColorOpaque(0xFF0000);
        engine.addVertex(0, 0, 0);
        engine.addVertex(0, 0, 50);
        engine.endDrawing();
        engine.startDrawing(GL_LINES);
        engine.setColorOpaque(0x0000FF);
        engine.addVertex(0, 0, 0);
        engine.addVertex(50, 0, 0);
        engine.endDrawing();
        engine.startDrawing(GL_LINES);
        engine.setColorOpaque(0x00FF00);
        engine.addVertex(0, 0, 0);
        engine.addVertex(0, 50, 0);
        engine.endDrawing();

        engine.startDrawing(GL_LINES);
        engine.setColorOpaque(0xFFFFFF);
        engine.addVertex(0, 0, 0);
        engine.addVertex(Math.cos(Math.toRadians(cameraRotation.getYaw())), Math.sin(Math.toRadians(cameraRotation.getPitch())), Math.sin(Math.toRadians(cameraRotation.getYaw())));
        engine.endDrawing();
    }
}
