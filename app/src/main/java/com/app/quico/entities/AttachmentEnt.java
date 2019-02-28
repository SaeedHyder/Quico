package com.app.quico.entities;

import android.graphics.Bitmap;

public class AttachmentEnt {

    String type;
    String attahcment;
    Bitmap bitmapImage;

    public AttachmentEnt(String type, String attahcment) {
        this.type = type;
        this.attahcment = attahcment;
    }
    public AttachmentEnt(String type, Bitmap bitmapImage, String attahcment) {
        this.type = type;
        this.bitmapImage = bitmapImage;
        this.attahcment = attahcment;
    }

    public Bitmap getBitmapImage() {
        return bitmapImage;
    }

    public void setBitmapImage(Bitmap bitmapImage) {
        this.bitmapImage = bitmapImage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAttahcment() {
        return attahcment;
    }

    public void setAttahcment(String attahcment) {
        this.attahcment = attahcment;
    }
}
