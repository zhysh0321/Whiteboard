package com.zhys.whiteboard.data;

import android.graphics.Canvas;
import android.graphics.Path;

public class PathDrawingInfo extends DrawingInfo {
    public Path path;

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }
}