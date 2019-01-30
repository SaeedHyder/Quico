package com.app.quico.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProjectDetail {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("project_name")
    @Expose
    private String projectName;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("icon_url")
    @Expose
    private String iconUrl;
    @SerializedName("media")
    @Expose
    private ArrayList<Medium> media = new ArrayList<>();

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public ArrayList<Medium> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<Medium> media) {
        this.media = media;
    }
}
