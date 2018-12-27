package com.app.quico.entities;

/**
 * Created by saeedhyder on 4/7/2017.
 */

public class MapScreenItem {

    private String lat;
    private String lng;
    private int marker;

    public MapScreenItem(String lat, String lng, int marker) {
        this.lat = lat;
        this.lng = lng;
        this.marker = marker;
    }


    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public int getMarker() {
        return marker;
    }

    public void setMarker(int marker) {
        this.marker = marker;
    }
}
