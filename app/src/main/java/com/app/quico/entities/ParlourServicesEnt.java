package com.app.quico.entities;

import java.util.ArrayList;

public class ParlourServicesEnt {

    private int border;
    private int dot;

    public ParlourServicesEnt(int border, int dot) {
        this.border = border;
        this.dot = dot;
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }

    public int getDot() {
        return dot;
    }

    public void setDot(int dot) {
        this.dot = dot;
    }
}
