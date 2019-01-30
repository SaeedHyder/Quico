package com.app.quico.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServicesDetail {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("service_id")
    @Expose
    private Integer serviceId;
    @SerializedName("service_detail")
    @Expose
    private ServiceDetailChild serviceDetail ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public ServiceDetailChild getServiceDetail() {
        return serviceDetail;
    }

    public void setServiceDetail(ServiceDetailChild serviceDetail) {
        this.serviceDetail = serviceDetail;
    }
}
