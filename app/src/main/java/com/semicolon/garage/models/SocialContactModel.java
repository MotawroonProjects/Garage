package com.semicolon.garage.models;

import java.io.Serializable;

/**
 * Created by elashry on 15/10/2018.
 */

public class SocialContactModel implements Serializable {
    private String facebook;
    private String twitter;
    private String instagram;
    private String linkedin;
    private String googlepluse;
    private String whatsapp;
    private String snapchat;
    private String phone;
    private String email;

    public String getFacebook() {
        return facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public String getGooglepluse() {
        return googlepluse;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public String getSnapchat() {
        return snapchat;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}