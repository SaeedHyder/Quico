package com.app.quico.entities.Chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocumentDetail {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("thumb_nail")
    @Expose
    private String thumbNail;
    @SerializedName("file_url")
    @Expose
    private String fileUrl;
    @SerializedName("thumb_url")
    @Expose
    private String thumbUrl;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
}
