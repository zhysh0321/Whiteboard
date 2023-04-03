package com.zhys.whiteboard.util;

import com.zhys.whiteboard.data.SerPathInfo;
import com.zhys.whiteboard.data.WhiteBoardPath;
import com.zhys.whiteboard.data.WhiteBoardPaths;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouys
 * @date 2022/7/5
 * @description 白板操作集
 */
public class DrawOperationUtil {

    public int mCurrentIndex;
    private WhiteBoardPaths mWhiteBoardPaths;

    public void init() {
        mCurrentIndex = 0;
        initDrawPathList();
    }

    /**
     * 初始化白板
     */
    public void initDrawPathList() {
        if (mWhiteBoardPaths != null && mWhiteBoardPaths.getWhiteBoardPaths() != null) {
            mWhiteBoardPaths.getWhiteBoardPaths().clear();
        } else {
            getDrawPathList(mCurrentIndex);
        }
    }

    /**
     * 返回指定白板的操作集
     */
    public WhiteBoardPath getDrawPathList(int i) {
        if (null != mWhiteBoardPaths) {
            if (mWhiteBoardPaths.getWhiteBoardPaths().size() <= i) {
                WhiteBoardPath drawPathList = new WhiteBoardPath();
                drawPathList.setId(i);
                mWhiteBoardPaths.getWhiteBoardPaths().add(drawPathList);
                return getDrawPathList(i);
            } else {
                return mWhiteBoardPaths.getWhiteBoardPaths().get(i);
            }
        } else {
            mWhiteBoardPaths = new WhiteBoardPaths();
            mWhiteBoardPaths.setWhiteBoardPaths(new ArrayList<WhiteBoardPath>());
            return getDrawPathList(i);
        }
    }

    /**
     * 返回指定白板的操作集大小
     */
    public int getDrawPathSize() {
        if (mWhiteBoardPaths != null && null != mWhiteBoardPaths.getWhiteBoardPaths()) {
            return mWhiteBoardPaths.getWhiteBoardPaths().size();
        } else {
            getDrawPathList(mCurrentIndex);
            return getDrawPathSize();
        }
    }

    /**
     * 返回指定白板的操作集
     */
    public WhiteBoardPaths getWhiteBoardPaths() {
        return mWhiteBoardPaths;
    }

    /**
     * 返回指定白板的操作集
     */
    public void setWhiteBoardPaths(WhiteBoardPaths whiteBoardPoints) {
        mWhiteBoardPaths = whiteBoardPoints;
    }

    /**
     * 新建白板
     */
    public void newPage() {
        mCurrentIndex = getDrawPathSize();
        getDrawPathList(mCurrentIndex);
    }

    /**
     * 获取当前白板操作集
     */
    public List<SerPathInfo> getSavePaths() {
        return getDrawPathList(mCurrentIndex).getSavePaths();
    }

    public void setSavePaths(List<SerPathInfo> drawingInfos) {
        getDrawPathList(mCurrentIndex).setSavePaths(drawingInfos);
    }

    /**
     * 获取当前白板删除操作集
     */
    public List<SerPathInfo> getRemovePaths() {
        return getDrawPathList(mCurrentIndex).getRemovePaths();
    }

    public void setRemovePaths(List<SerPathInfo> drawingInfos) {
        getDrawPathList(mCurrentIndex).setRemovePaths(drawingInfos);
    }

    public boolean isEmpty() {
        if (mWhiteBoardPaths == null || mWhiteBoardPaths.getWhiteBoardPaths() == null || mWhiteBoardPaths.getWhiteBoardPaths().size() == 0)
            return true;
        boolean isEmpty = true;
        for (int i = 0; i < mWhiteBoardPaths.getWhiteBoardPaths().size(); i++) {
            if (mWhiteBoardPaths.getWhiteBoardPaths().get(i).getSavePaths().size() != 0) {
                isEmpty = false;
                break;
            }
        }
        return isEmpty;
    }

}
