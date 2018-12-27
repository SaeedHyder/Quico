package com.app.quico.entities;

public class SideMenuEnt {

    String title;
    int selectedImage;
    int unSelectedImage;
    boolean isSelected=false;


    public SideMenuEnt(String title, int unSelectedImage,int selectedImage) {
        this.title = title;
        this.selectedImage = selectedImage;
        this.unSelectedImage = unSelectedImage;
    }

    public SideMenuEnt(String title, int unSelectedImage, int selectedImage, boolean isSelected) {
        this.title = title;
        this.selectedImage = selectedImage;
        this.unSelectedImage = unSelectedImage;
        this.isSelected = isSelected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(int selectedImage) {
        this.selectedImage = selectedImage;
    }

    public int getUnSelectedImage() {
        return unSelectedImage;
    }

    public void setUnSelectedImage(int unSelectedImage) {
        this.unSelectedImage = unSelectedImage;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
