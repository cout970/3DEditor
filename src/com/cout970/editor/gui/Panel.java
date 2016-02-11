package com.cout970.editor.gui;

import com.cout970.editor.util.Color;
import com.cout970.editor.util.Vect2d;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cout970 on 11/02/2016.
 */
public class Panel extends Component{

    protected Color color;
    protected List<Component> subComponents;

    public Panel(Vect2d pos, Vect2d size, Color color) {
        super(pos, size);
        this.color = color;
        subComponents = new LinkedList<>();
    }

    public void addComponent(Component c) {
        subComponents.add(c);
    }

    public List<Component> getSubComponents() {
        return new ArrayList<>(subComponents);
    }

    @Override
    public void updateAndRender() {
        if (color != null) {
            renderer.drawRectangle(pos.toVect2i(), pos.toVect2i().add(getSize()), color);
        }
    }


}
