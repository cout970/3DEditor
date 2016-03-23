package com.cout970.editor.render.examples;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by cout970 on 23/03/2016.
 */
public class Planes {


    public static final double pixel = 1 / 16d;

    public static void drawAxisGridY(double y) {
        int lenght = 3;
        glLineWidth(1f);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_DST_ALPHA);
        glBegin(GL_LINES);
        glColor4f(0.6f, 0.6f, 0.6f, 0.5f);
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 == 0) {
                continue;
            }
            glVertex3d(i * pixel, y, (-lenght * 16 - 2) * pixel);
            glVertex3d(i * pixel, y, ((lenght + 1) * 16 + 2) * pixel);
        }
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 == 0) {
                continue;
            }
            glVertex3d((-lenght * 16 - 2) * pixel, y, i * pixel);
            glVertex3d(((lenght + 1) * 16 + 2) * pixel, y, i * pixel);
        }
        glEnd();
        glLineWidth(2f);
        glBegin(GL_LINES);
        glColor4f(0.34f, 0.34f, 0.34f, 0.5f);
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 != 0) {
                continue;
            }
            glVertex3d(i * pixel, y, (-lenght * 16 - 2) * pixel);
            glVertex3d(i * pixel, y, ((lenght + 1) * 16 + 2) * pixel);
        }
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 != 0) {
                continue;
            }
            glVertex3d((-lenght * 16 - 2) * pixel, y, i * pixel);
            glVertex3d(((lenght + 1) * 16 + 2) * pixel, y, i * pixel);
        }
        glEnd();
        glDisable(GL_BLEND);
    }

    public static void drawAxisGridX(double x) {
        int lenght = 3;
        glLineWidth(1f);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_DST_ALPHA);
        glBegin(GL_LINES);
        glColor4f(0.6f, 0.6f, 0.6f, 0.5f);
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 == 0) {
                continue;
            }
            glVertex3d(x, i * pixel, (-lenght * 16 - 2) * pixel);
            glVertex3d(x, i * pixel, ((lenght + 1) * 16 + 2) * pixel);
        }
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 == 0) {
                continue;
            }
            glVertex3d(x, (-lenght * 16 - 2) * pixel, i * pixel);
            glVertex3d(x, ((lenght + 1) * 16 + 2) * pixel, i * pixel);
        }
        glEnd();
        glLineWidth(2f);
        glBegin(GL_LINES);
        glColor4f(0.34f, 0.34f, 0.34f, 0.5f);
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 != 0) {
                continue;
            }
            glVertex3d(x, i * pixel, (-lenght * 16 - 2) * pixel);
            glVertex3d(x, i * pixel, ((lenght + 1) * 16 + 2) * pixel);
        }
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 != 0) {
                continue;
            }
            glVertex3d(x, (-lenght * 16 - 2) * pixel, i * pixel);
            glVertex3d(x, ((lenght + 1) * 16 + 2) * pixel, i * pixel);
        }
        glEnd();
        glDisable(GL_BLEND);
    }

    public static void drawAxisGridZ(double z) {
        int lenght = 3;
        glLineWidth(1f);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_DST_ALPHA);
        glBegin(GL_LINES);
        glColor4f(0.6f, 0.6f, 0.6f, 0.5f);
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 == 0) {
                continue;
            }
            glVertex3d(i * pixel, (-lenght * 16 - 2) * pixel, z);
            glVertex3d(i * pixel, ((lenght + 1) * 16 + 2) * pixel, z);
        }
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 == 0) {
                continue;
            }
            glVertex3d((-lenght * 16 - 2) * pixel, i * pixel, z);
            glVertex3d(((lenght + 1) * 16 + 2) * pixel, i * pixel, z);
        }
        glEnd();
        glLineWidth(2f);
        glBegin(GL_LINES);
        glColor4f(0.34f, 0.34f, 0.34f, 0.5f);
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 != 0) {
                continue;
            }
            glVertex3d(i * pixel, (-lenght * 16 - 2) * pixel, z);
            glVertex3d(i * pixel, ((lenght + 1) * 16 + 2) * pixel, z);
        }
        for (int i = -16 * lenght - 2; i <= 16 * (lenght + 1) + 2; i++) {
            if (i % 16 != 0) {
                continue;
            }
            glVertex3d((-lenght * 16 - 2) * pixel, i * pixel, z);
            glVertex3d(((lenght + 1) * 16 + 2) * pixel, i * pixel, z);
        }
        glEnd();
        glDisable(GL_BLEND);
    }
}
