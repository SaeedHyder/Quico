package com.app.quico.entities;

public class UpdateProfile {

    private String id;
    private String name;
    private String email;
    private String country_code;
    private String phone;
    private String address;
    private String latitude;
    private String longitude;
    private String image;

    public UpdateProfile(String id, String name, String email, String country_code, String phone, String address, String latitude, String longitude, String image) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.country_code = country_code;
        this.phone = phone;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
    }
}
