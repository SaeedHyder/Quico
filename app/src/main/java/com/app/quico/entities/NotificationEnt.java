package com.app.quico.entities;

import com.app.quico.helpers.DateHelper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by saeedhyder on 9/15/2017.
 */

public class NotificationEnt {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sender_id")
    @Expose
    private Integer senderId;
    @SerializedName("reciever_id")
    @Expose
    private Integer recieverId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("ma_message")
    @Expose
    private String Mamessage;
    @SerializedName("in_message")
    @Expose
    private String Inmessage;
    @SerializedName("action_type")
    @Expose
    private String actionType;
    @SerializedName("action_id")
    @Expose
    private Integer actionId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getMamessage() {
        return Mamessage;
    }

    public void setMamessage(String mamessage) {
        Mamessage = mamessage;
    }

    public String getInmessage() {
        return Inmessage;
    }

    public void setInmessage(String inmessage) {
        Inmessage = inmessage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(Integer recieverId) {
        this.recieverId = recieverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }

    public String getCreatedAt() {
        return DateHelper.getLocalTimeDate(createdAt);
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}