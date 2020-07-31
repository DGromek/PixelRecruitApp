package com.sample.model;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class CloneableCircle extends Circle implements Cloneable {

    public CloneableCircle() {
    }

    public CloneableCircle(double v, double v1, double v2, Paint paint) {
        super(v, v1, v2, paint);
    }

    @Override
    public Object clone() {
        Circle circle = new Circle();
        circle.setCenterX(this.getCenterX());
        circle.setCenterY(this.getCenterY());
        circle.setRadius(this.getRadius());
        circle.setFill(this.getFill());
        return circle;
    }
}
