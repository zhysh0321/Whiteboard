package com.zhys.whiteboard.data;

import java.util.List;

/**
 * @author zhouys
 * @date 2023/4/3
 * @description 画板集合
 */
public class WhiteBoardPaths {
    private int id;
    private List<WhiteBoardPath> whiteBoardPaths;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<WhiteBoardPath> getWhiteBoardPaths() {
        return whiteBoardPaths;
    }

    public void setWhiteBoardPaths(List<WhiteBoardPath> whiteBoardPaths) {
        this.whiteBoardPaths = whiteBoardPaths;
    }

}
