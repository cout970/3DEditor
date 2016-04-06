package com.cout970.editor.gui.event;

/**
 * Created by cout970 on 05/04/2016.
 */
public class MouseWheelEvent {

    private double amount;
    private double unknown;

    public MouseWheelEvent(double amount, double unknown) {
        this.amount = amount;
        this.unknown = unknown;
    }

    public double getAmount() {
        return amount;
    }

    public double getUnknown() {
        return unknown;
    }
}
