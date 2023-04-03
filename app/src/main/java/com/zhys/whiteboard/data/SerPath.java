package com.zhys.whiteboard.data;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouys
 * @date 2023/4/3
 * @description
 */
public class SerPath implements Serializable {
    private List<SerPoint> mPoints;
    private int color;
    private float strokeWidth;
    private int mode;

    public List<SerPoint> getPoints() {
        if (mPoints == null) {
            mPoints = new ArrayList<>();
        }
        return mPoints;
    }

    public void setPoints(List<SerPoint> mPoints) {
        this.mPoints = mPoints;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void lineStart(SerPoint point) {
        getPoints().add(point);
    }

    public void lineMove(SerPoint point) {
        getPoints().add(point);
    }

    public void lineEnd(int mode, float strokeWidth) {
        setMode(mode);
        setStrokeWidth(strokeWidth);
    }

    private Paint transferPaint() {
        Paint mPaint = null;
        if (getMode() == 0) {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setFilterBitmap(true);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(getStrokeWidth());
            mPaint.setAntiAlias(true); // 抗锯齿
            mPaint.setDither(true); // 防抖
            mPaint.setColor(0XFF000000);
            PorterDuffXfermode xfermodeDraw = new PorterDuffXfermode(PorterDuff.Mode.SRC);//
            mPaint.setXfermode(xfermodeDraw);
            mPaint.setPathEffect(null);
        } else {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaint.setFilterBitmap(true);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(getStrokeWidth());
            PorterDuffXfermode xferModeClear = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
            mPaint.setXfermode(xferModeClear);
            mPaint.setPathEffect(new DashPathEffect(new float[]{10, 5}, 0));
        }

        return mPaint;
    }

    private Path transferPath() {
        Path path = new Path();
        SerPoint p;
        int size = getPoints().size();
        if (size < 2) {
            return path;
        }
        p = getPoints().get(0);
        path.moveTo(p.getX(), p.getY());

        float ox = p.getX();
        float oy = p.getY();

        for (int i = 1; i < size; i++) {
            p = getPoints().get(i);
            path.quadTo(ox, oy, (ox + p.getX()) / 2, (oy + p.getY()) / 2);
            ox = p.getX();
            oy = p.getY();
        }
        return path;
    }

    public PathDrawingInfo transfer() {
        Paint paint = transferPaint();
        Path path = transferPath();
        PathDrawingInfo drawingInfo = new PathDrawingInfo();
        drawingInfo.path = path;
        drawingInfo.paint = paint;
        return drawingInfo;
    }
}
