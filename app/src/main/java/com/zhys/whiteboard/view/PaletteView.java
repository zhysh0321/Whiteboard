package com.zhys.whiteboard.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;


import com.zhys.whiteboard.data.PathDrawingInfo;
import com.zhys.whiteboard.data.SerPath;
import com.zhys.whiteboard.data.SerPathInfo;
import com.zhys.whiteboard.data.SerPoint;
import com.zhys.whiteboard.util.DimenUtils;

import java.util.ArrayList;
import java.util.List;

public class PaletteView extends View {
    private static String TAG = PaletteView.class.getSimpleName();
    private Paint mPaint;
    private Path mPath;
    private float mLastX;
    private float mLastY;
    private Bitmap mBufferBitmap;
    private Canvas mBufferCanvas;

    private List<SerPathInfo> mDrawingList;
    private List<SerPathInfo> mRemovedList;

    private Xfermode mXferModeClear;
    private Xfermode mXferModeDraw;
    private float mDrawSize;
    private float mEraserSize;
    private int mPenAlpha = 255;
    private boolean mCanEraser;
    private Bitmap mBitmap;
    private Paint mEraserPaint;
    private int mDashSize;
    private int mColor;
    private SerPath mSerPath;

    public enum Mode {
        DRAW,
        ERASER
    }

    private Mode mMode = Mode.DRAW;


    public PaletteView(Context context) {
        super(context);
        init(context);
    }

    public PaletteView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PaletteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setDrawingCacheEnabled(true);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setFilterBitmap(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mDrawSize = DimenUtils.dip2px(context, 1);//线宽度
        mPaint.setStrokeWidth(mDrawSize);
        mPaint.setAntiAlias(true); // 抗锯齿
        mPaint.setDither(true); // 防抖
        mPaint.setColor(0XFF000000);
        mXferModeDraw = new PorterDuffXfermode(PorterDuff.Mode.SRC);//
        mPaint.setXfermode(mXferModeDraw);

        mEraserPaint = new Paint();
        mEraserPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mEraserPaint.setFilterBitmap(true);
        mEraserPaint.setStrokeJoin(Paint.Join.ROUND);
        mEraserPaint.setStrokeCap(Paint.Cap.ROUND);
        mEraserSize = DimenUtils.dip2px(context, 1);//橡皮擦
        mEraserPaint.setStrokeWidth(mEraserSize);
        mEraserPaint.setPathEffect(new DashPathEffect(new float[]{10, 5}, 0));
        mXferModeClear = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        mEraserPaint.setXfermode(mXferModeClear);
    }

    private void initBuffer() {
        mBufferBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mBufferCanvas = new Canvas(mBufferBitmap);
    }

    public Mode getMode() {
        return mMode;
    }

    public void setMode(Mode mode) {
        mMode = mode;
        if (mMode == Mode.DRAW) {
            mPaint.setStrokeWidth(mDrawSize);
            mPaint.setPathEffect(null);
        } else {
            mPaint.setStrokeWidth(mEraserSize);
            mPaint.setPathEffect(new DashPathEffect(new float[]{10, 5}, 0));
        }
    }

    public void setEraserSize(int size) {
        mEraserSize = size;
    }

    public void setPenDrawSize(float size) {
        mDrawSize = size;
    }

    public void setPenColor(int color) {
        mPaint.setColor(color);
    }

    public void reDraw() {
        if (mDrawingList != null) {
            if (mBufferBitmap == null) {
                initBuffer();
            }
//            mBufferBitmap.eraseColor(Color.TRANSPARENT);
            mBufferCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            for (SerPathInfo drawingInfo : mDrawingList) {
                drawingInfo.getDrawingInfo().draw(mBufferCanvas);
            }
            invalidate();
        }
    }

    public int getPenColor() {
        return mPaint.getColor();
    }

    public float getPenSize() {
        return mDrawSize;
    }

    public float getEraserSize() {
        return mEraserSize;
    }

    public void setPenAlpha(int alpha) {
        mPenAlpha = alpha;
        if (mMode == Mode.DRAW) {
            mPaint.setAlpha(alpha);
        }
    }

    public int getPenAlpha() {
        return mPenAlpha;
    }

    public void setBackground(@ColorInt int color) {
        setBackgroundColor(color);
    }

    public boolean canRedo() {
        return mRemovedList != null && mRemovedList.size() > 0;
    }

    public boolean canUndo() {
        return mDrawingList != null && mDrawingList.size() > 0;
    }

    public void redo() {
        int size = mRemovedList == null ? 0 : mRemovedList.size();
        if (size > 0) {
            SerPathInfo info = mRemovedList.remove(size - 1);
            mDrawingList.add(info);
            mCanEraser = true;
            reDraw();
        }
    }

    public void undo() {
        int size = mDrawingList == null ? 0 : mDrawingList.size();
        if (size > 0) {
            SerPathInfo info = mDrawingList.remove(size - 1);
            if (mRemovedList == null) {
                mRemovedList = new ArrayList<>();
            }
            if (size == 1) {
                mCanEraser = false;
            }
            mRemovedList.add(info);
            reDraw();
        }
    }

    public void clear() {
        if (mBufferBitmap != null) {
            if (mDrawingList != null) {
                mDrawingList.clear();
            }
            if (mRemovedList != null) {
                mRemovedList.clear();
            }
            mCanEraser = false;
            mBufferBitmap.eraseColor(Color.TRANSPARENT);
            invalidate();
        }
    }

    public void reDraw(List<SerPathInfo> drawingList) {
        mDrawingList = drawingList;
        reDraw();
    }

    public List<SerPathInfo> getDrawingPaths() {
        return mDrawingList;
    }

    public void setDrawingPaths(List<SerPathInfo> drawingList) {
        if (drawingList == null) return;
        for (SerPathInfo info : drawingList) {
            if (info.getDrawingInfo() == null){
                PathDrawingInfo drawingInfo = info.getSerPath().transfer();
                info.setDrawingInfo(drawingInfo);
            }
        }
        mDrawingList = drawingList;
    }

    public List<SerPathInfo> getRemovePaths() {
        return mRemovedList;
    }

    public void setRemoveList(List<SerPathInfo> drawingList) {
        if (drawingList == null) return;
        for (SerPathInfo info : drawingList) {
            if (info.getDrawingInfo() == null){
                PathDrawingInfo drawingInfo = info.getSerPath().transfer();
                info.setDrawingInfo(drawingInfo);
            }
        }
        mRemovedList = drawingList;
    }

    public Bitmap buildBitmap() {
        Bitmap bm = getDrawingCache();
        Bitmap result = Bitmap.createBitmap(bm);
        destroyDrawingCache();
        return result;
    }

    public void redrawBitMap(Bitmap bitmap) {
        mBufferBitmap = bitmap;
        mBufferCanvas = new Canvas(mBufferBitmap);
        invalidate();
    }

    private void saveDrawingPath() {
        if (mDrawingList == null) {
            mDrawingList = new ArrayList<>();
        }
        Path cachePath = new Path(mPath);
        Paint cachePaint = null;
        if (mMode == Mode.DRAW) {
            cachePaint = new Paint(mPaint);
            mSerPath.lineEnd(0, mDrawSize);
        } else {
            cachePaint = new Paint(mEraserPaint);
            mSerPath.lineEnd(1, mEraserSize);
        }

        SerPathInfo info = new SerPathInfo();
        info.setSerPath(mSerPath);

        PathDrawingInfo info1 = new PathDrawingInfo();
        info1.path = cachePath;
        info1.paint = cachePaint;

        info.setDrawingInfo(info1);

        mDrawingList.add(info);
        mCanEraser = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
        if (mBufferBitmap != null) {
            canvas.drawBitmap(mBufferBitmap, 0, 0, null);
        }
    }

    @SuppressWarnings("all")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        final int action = event.getAction() & MotionEvent.ACTION_MASK;
        final float x = event.getX();
        final float y = event.getY();
        int type = event.getToolType(0);
        float pressure = event.getPressure();
        Log.d(TAG, type + "");
//        if (type != MotionEvent.TOOL_TYPE_STYLUS) return true;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                if (mPath == null) {
                    mPath = new Path();
                }
                mPath.moveTo(x, y);
                mSerPath = new SerPath();
                mSerPath.lineStart(new SerPoint(x, y));
                break;
            case MotionEvent.ACTION_MOVE:
                //这里终点设为两点的中心点的目的在于使绘制的曲线更平滑，如果终点直接设置为x,y，效果和lineto是一样的,实际是折线效果
                mPath.quadTo(mLastX, mLastY, (x + mLastX) / 2, (y + mLastY) / 2);
                mSerPath.lineMove(new SerPoint(x, y));
                if (mBufferBitmap == null) {
                    initBuffer();
                }
                if (mMode == Mode.ERASER && !mCanEraser) {
                    break;
                }
                if (mMode == Mode.ERASER) {
//                    mBufferCanvas.drawPath(mPath,mEraserPaint);
                } else {
//                    mBufferCanvas.drawPath(mPath,mPaint);
                }
                mBufferCanvas.drawPath(mPath, mPaint);
                invalidate();
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                if (mMode == Mode.ERASER) {
                    saveDrawingPath();
                    reDraw();
//                    mBufferCanvas.drawPath(mPath,mEraserPaint);
                } else {
                    saveDrawingPath();
                    invalidate();
                }
                mPath.reset();
                break;
        }
        return true;
    }
}
