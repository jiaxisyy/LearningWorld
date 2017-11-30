package com.example.hekd.learningworld.bean;

/**
 * Created by hekd on 2017/11/30.
 */

public class Volume {

    public String path;
    public boolean removable;
    public String state;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isRemovable() {
        return removable;
    }

    public void setRemovable(boolean removable) {
        this.removable = removable;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
