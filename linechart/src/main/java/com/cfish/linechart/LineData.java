package com.cfish.linechart;

/**
 * Created by GKX100217 on 2016/6/8.
 */
public class LineData {

    private String name;
    private float[][] value;
    private int color;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float[][] getValue() {
        return value;
    }

    public void setValue(float[][] value) {
        this.value = value;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
