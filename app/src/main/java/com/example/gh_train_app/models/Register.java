package com.example.gh_train_app.models;


public class Register {
    String id;
    String fullname;
    String phonenumber;
    String username;
    String email;
    String address;
    String password;

    public Register(){}


    public Register(String id, String fullname, String phonenumber, String username, String email, String address, String password) {
        this.id = id;
        this.fullname = fullname;
        this.phonenumber = phonenumber;
        this.username = username;
        this.email = email;
        this.address = address;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
