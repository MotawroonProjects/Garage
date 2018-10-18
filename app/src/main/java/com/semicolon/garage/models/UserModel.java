package com.semicolon.garage.models;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String user_id;
    private String user_type;
    private String user_phone;
    private String user_email;
    private String user_full_name;
    private String user_photo;
    private String user_token_id;
    private String user_google_lat;
    private String user_google_long;
    private String document_type_definition;
    private String country_personal_proof;
    private String user_nationality;
    private String id_country;
    private String ar_name;
    private String en_name;
    private String ar_nationality;
    private String en_nationality;
    private String ar_city_title;
    private String en_city_title;
    private int success_signup;
    private int success_login;
    private int success_update;
    private int success_update_pass;

    public String getUser_id() {
        return user_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_full_name() {
        return user_full_name;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public String getUser_token_id() {
        return user_token_id;
    }

    public String getUser_google_lat() {
        return user_google_lat;
    }

    public String getUser_google_long() {
        return user_google_long;
    }

    public String getDocument_type_definition() {
        return document_type_definition;
    }

    public String getCountry_personal_proof() {
        return country_personal_proof;
    }

    public String getUser_nationality() {
        return user_nationality;
    }

    public String getId_country() {
        return id_country;
    }

    public String getAr_name() {
        return ar_name;
    }

    public String getEn_name() {
        return en_name;
    }

    public String getAr_nationality() {
        return ar_nationality;
    }

    public String getEn_nationality() {
        return en_nationality;
    }

    public int getSuccess_signup() {
        return success_signup;
    }

    public int getSuccess_login() {
        return success_login;
    }

    public int getSuccess_update() {
        return success_update;
    }

    public int getSuccess_update_pass() {
        return success_update_pass;
    }

    public String getAr_city_title() {
        return ar_city_title;
    }

    public String getEn_city_title() {
        return en_city_title;
    }
}
