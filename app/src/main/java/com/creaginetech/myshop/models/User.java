package com.creaginetech.myshop.models;

public class User {

    String username,email,address,phone,imageProfile;

    public User() {
    }

    public User(String username, String email, String address, String phone, String imageProfile) {
        this.username = username;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.imageProfile = imageProfile;
    }

    //    public User(String username, String email, String address, String phone) {
//        this.username = username;
//        this.email = email;
//        this.address = address;
//        this.phone = phone;
//    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }
}
