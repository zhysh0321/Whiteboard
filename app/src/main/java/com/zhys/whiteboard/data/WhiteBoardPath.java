package com.zhys.whiteboard.data;

import java.util.List;

/**
 * @author zhouys
 * @date 2023/4/3
 * @description 画板笔迹
 */
public class WhiteBoardPath {
    private int id;
    private List<SerPathInfo> savePaths; // 已保存笔迹
    private List<SerPathInfo> removePaths; // 已撤销笔迹

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<SerPathInfo> getSavePaths() {
        return savePaths;
    }

    public void setSavePaths(List<SerPathInfo> savePaths) {
        this.savePaths = savePaths;
    }

    public List<SerPathInfo> getRemovePaths() {
        return removePaths;
    }

    public void setRemovePaths(List<SerPathInfo> removePaths) {
        this.removePaths = removePaths;
    }
}
