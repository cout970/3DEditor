package com.cout970.editor.gui;

import com.cout970.editor.InputHandler;
import com.cout970.editor.util.Color;
import com.cout970.editor.util.Vect2d;
import com.cout970.editor.util.Vect2i;

/**
 * Created by cout970 on 11/02/2016.
 */
public class DynamicPanel extends Panel {

    private boolean selected;

    public DynamicPanel(Vect2d pos, Vect2d size, Color color) {
        super(pos, size, color);
    }

    @Override
    public void updateAndRender() {
        super.updateAndRender();
        Vect2i start = this.pos.toVect2i();
        start.add(getSize().getX(), 0);
        Vect2i end = start.copy().add(5, getSize().getY());
        renderer.drawRectangle(start, end, new Color(0x4C4C4C));

        if(selected){

            if (!InputHandler.isMouseButtonPress(InputHandler.MouseButton.LEFT)){
                selected = false;
            }else{
                Vect2d mouse = InputHandler.getCursorPos();
                double x = mouse.getX()/handler.getScreenSize().getX()*100;
                if (x <= 2){
                    x = 2;
                }else if(x >= 98){
                    x = 98;
                }
                size.set(x, size.getY());
            }
        }
    }

    @Override
    public void onMouseClick(Vect2i pos, InputHandler.MouseButton b){
        Vect2i start = this.pos.toVect2i();
        start.add(getSize().getX(), 0);
        Vect2i end = start.copy().add(5, getSize().getY());
        if (b == InputHandler.MouseButton.LEFT && isInside(pos, start, end)){
            selected = true;
        }
    }

    public static boolean isInside(Vect2i mouse, Vect2i pos, Vect2i size) {
        return isInside(mouse.getX(), mouse.getY(), pos.getX(), pos.getY(), size.getX(), size.getY());
    }

    public static boolean isInside(int mx, int my, int x, int y, int w, int h) {
        if (mx > x && mx < x + w) {
            if (my > y && my < y + h) {
                return true;
            }
        }
        return false;
    }
}
