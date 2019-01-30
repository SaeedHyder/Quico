package com.app.quico.entities;

public class GoogleEnt {

    private String id;
    private String name;
    private String email;
    private String photo;
    private String token;

    public GoogleEnt(String id, String name, String email, String photo, String token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.photo = photo;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
