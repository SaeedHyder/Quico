package com.app.quico.entities;

public class ServicesEnt {

    String title;
    String Detail;
    int image;
    int border;

    public ServicesEnt(String title, int image, int border) {
        this.title = title;
        this.image = image;
        this.border = border;
    }

    public ServicesEnt(String title, String detail, int image, int border) {
        this.title = title;
        Detail = detail;
        this.image = image;
        this.border = border;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String detail) {
        Detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }
}
