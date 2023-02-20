package com.ahamed.citchat.model;

import java.io.Serializable;

public class UserModel implements Serializable {

    private String id;
    private String mail;
    private String name;
    private String pass;


    public UserModel(String id, String mail, String name, String pass) {
        this.id = id;
        this.mail = mail;
        this.name = name;
        this.pass = pass;
    }

    public UserModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
