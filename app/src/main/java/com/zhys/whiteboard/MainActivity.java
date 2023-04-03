package com.zhys.whiteboard;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zhys.whiteboard.data.SerPath;
import com.zhys.whiteboard.data.SerPathInfo;
import com.zhys.whiteboard.data.WhiteBoardPath;
import com.zhys.whiteboard.data.WhiteBoardPaths;
import com.zhys.whiteboard.util.DrawOperationUtil;
import com.zhys.whiteboard.util.LocalPathUtils;
import com.zhys.whiteboard.view.PaletteView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ll_pen;
    private LinearLayout ll_undo;
    private LinearLayout ll_redo;
    private LinearLayout ll_eraser;
    private LinearLayout ll_clear;
    private LinearLayout ll_add;
    private Button btn_last_page;
    private Button btn_next_page;
    private TextView tv_add_page;
    private PaletteView palette;
    private DrawOperationUtil mDrawUtils;
    private long lastClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ll_pen = findViewById(R.id.ll_pen);
        ll_undo = findViewById(R.id.ll_undo);
        ll_redo = findViewById(R.id.ll_redo);
        ll_eraser = findViewById(R.id.ll_eraser);
        ll_clear = findViewById(R.id.ll_clear);
        ll_add = findViewById(R.id.ll_add);
        btn_last_page = findViewById(R.id.btn_last_page);
        btn_next_page = findViewById(R.id.btn_next_page);
        tv_add_page = findViewById(R.id.tv_add_page);
        palette = findViewById(R.id.palette);

        ll_pen.setOnClickListener(this);
        ll_undo.setOnClickListener(this);
        ll_redo.setOnClickListener(this);
        ll_eraser.setOnClickListener(this);
        ll_clear.setOnClickListener(this);
        ll_add.setOnClickListener(this);
        btn_last_page.setOnClickListener(this);
        btn_next_page.setOnClickListener(this);
        mDrawUtils = new DrawOperationUtil();
        mDrawUtils.init();
        showHistoryPainter();
    }

    public void addOnePage() {
        saveCurrentPaths();
        if (mDrawUtils.getDrawPathSize() > 4) { // 限定页数，自定义值
            return;
        } else {
            mDrawUtils.newPage();
        }
        if (mDrawUtils.mCurrentIndex > 0) {
            btn_last_page.setEnabled(true);
        }
        if (mDrawUtils.mCurrentIndex == mDrawUtils.getDrawPathSize() - 1) {
            btn_next_page.setEnabled(false);
        }
        btn_last_page.setVisibility(View.VISIBLE);
        btn_next_page.setVisibility(View.VISIBLE);
        tv_add_page.setVisibility(View.VISIBLE);
        setCurrentPageState();
        palette.clear();
        palette.setBackground(Color.WHITE);
        palette.reDraw();
    }

    private void saveCurrentPaths() {
        List<SerPathInfo> savePaths = new ArrayList<>();
        if (palette.getDrawingPaths() != null)
            savePaths.addAll(palette.getDrawingPaths());
        List<SerPathInfo> removePaths = new ArrayList<>();
        if (palette.getRemovePaths() != null)
            removePaths.addAll(palette.getRemovePaths());
        mDrawUtils.setSavePaths(savePaths);
        mDrawUtils.setRemovePaths(removePaths);
        ArrayList<ArrayList<SerPath>> serPaths = new ArrayList<>();
        for (WhiteBoardPath path : mDrawUtils.getWhiteBoardPaths().getWhiteBoardPaths()) {
            ArrayList<SerPath> serPaths1 = new ArrayList<>();
            for (SerPathInfo serPathInfo : path.getSavePaths()) {
                serPaths1.add(serPathInfo.getSerPath());
            }
            serPaths.add(serPaths1);
        }
        LocalPathUtils.saveObject(serPaths);
    }

    private void setCurrentPageState() {
        String curr = (mDrawUtils.mCurrentIndex + 1) + "";
        String size = mDrawUtils.getDrawPathSize() + "";
        String s = curr + "/" + size;
        SpannableString ss = new SpannableString(s);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#1A8D47"));
        ss.setSpan(foregroundColorSpan, s.length() - size.length() - curr.length() - 1, s.length() - size.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_add_page.setText(ss);
    }

    /**
     * 显示历史轨迹
     */
    private void showHistoryPainter() {
        ArrayList<ArrayList<SerPath>> path = (ArrayList<ArrayList<SerPath>>) LocalPathUtils.readObject();
        if (path == null || path.size() == 0) return;
        ArrayList<WhiteBoardPath> whiteBoardPathList = new ArrayList<>();
        for (ArrayList<SerPath> serPaths : path) {
            WhiteBoardPath whiteBoardPath = new WhiteBoardPath();
            ArrayList<SerPathInfo> serPathInfos = new ArrayList<>();
            for (SerPath serPath : serPaths) {
                SerPathInfo pathInfo = new SerPathInfo();
                pathInfo.setSerPath(serPath);
                serPathInfos.add(pathInfo);
            }
            whiteBoardPath.setSavePaths(serPathInfos);
            whiteBoardPathList.add(whiteBoardPath);
        }
        WhiteBoardPaths whiteBoardPaths = new WhiteBoardPaths();
        whiteBoardPaths.setWhiteBoardPaths(whiteBoardPathList);
        if (whiteBoardPaths != null) {
            mDrawUtils.setWhiteBoardPaths(whiteBoardPaths);
            palette.post(new Runnable() {
                @Override
                public void run() {
                    palette.setDrawingPaths(mDrawUtils.getSavePaths());
                    palette.setRemoveList(mDrawUtils.getRemovePaths());
                    palette.reDraw();
                }
            });
            if (whiteBoardPathList.size() > 1) {
                btn_next_page.setVisibility(View.VISIBLE);
                setCurrentPageState();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_pen:
//              palette.setPenDrawSize(5);  // 设置画笔粗细
                palette.setMode(PaletteView.Mode.DRAW);
                break;
            case R.id.ll_undo:
                palette.undo();
                break;
            case R.id.ll_redo:
                palette.redo();
                break;
            case R.id.ll_eraser:
                palette.setMode(PaletteView.Mode.ERASER);
                break;

            case R.id.ll_clear:
                palette.clear();
                palette.setMode(PaletteView.Mode.DRAW);
                break;
            case R.id.ll_add:
                addOnePage();
                break;
            case R.id.btn_last_page:
                if (isFastClick()) return;
                if (mDrawUtils.mCurrentIndex <= 0) return;
                saveCurrentPaths();
                mDrawUtils.mCurrentIndex--;
                if (mDrawUtils.mCurrentIndex <= 0) {
                    mDrawUtils.mCurrentIndex = 0;
                    btn_last_page.setEnabled(false);
                    palette.setBackground(Color.TRANSPARENT);
                } else {
                    btn_last_page.setEnabled(true);
                    palette.setBackground(Color.WHITE);
                }
                btn_next_page.setEnabled(true);
                btn_next_page.setVisibility(View.VISIBLE);
                setCurrentPageState();
                palette.setDrawingPaths(mDrawUtils.getSavePaths());
                palette.setRemoveList(mDrawUtils.getRemovePaths());
                palette.reDraw();
                break;
            case R.id.btn_next_page:
                if (isFastClick()) return;
                if (mDrawUtils.mCurrentIndex >= mDrawUtils.getDrawPathSize() - 1) return;
                saveCurrentPaths();
                mDrawUtils.mCurrentIndex++;
                if (mDrawUtils.mCurrentIndex >= mDrawUtils.getDrawPathSize() - 1) {
                    btn_next_page.setEnabled(false);
                    mDrawUtils.mCurrentIndex = mDrawUtils.getDrawPathSize() - 1;
                } else {
                    btn_next_page.setEnabled(true);
                }
                btn_last_page.setEnabled(true);
                btn_last_page.setVisibility(View.VISIBLE);
                setCurrentPageState();

                palette.setBackground(Color.WHITE);
                palette.setDrawingPaths(mDrawUtils.getSavePaths());
                palette.setRemoveList(mDrawUtils.getRemovePaths());
                palette.reDraw();
                break;

        }
    }

    /**
     * 判断是否快速点击
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public boolean isFastClick() {
        long now = System.currentTimeMillis();
        if (now - lastClick >= 600) {
            lastClick = now;
            return false;
        }
        return true;
    }
}