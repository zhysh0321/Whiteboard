package com.zhys.whiteboard.data;

import java.io.Serializable;

/**
 * @author zhouys
 * @date 2023/4/3
 * @description
 */
public class SerPoint implements Serializable {

    private float x;
    private float y;

    public SerPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
