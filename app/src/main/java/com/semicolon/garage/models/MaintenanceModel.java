package com.semicolon.garage.models;

import java.io.Serializable;

public class MaintenanceModel implements Serializable {
    private String id_car_maintenance;
    private String details;
    private String title;
    private String main_photo;
    private String address;
    private String address_google_lat;
    private String address_google_long;
    private String phone;
    private String id_country;
    private String ar_name;
    private String en_name;
    private String ar_nationality;
    private String en_nationality;

    public String getId_car_maintenance() {
        return id_car_maintenance;
    }

    public String getDetails() {
        return details;
    }

    public String getTitle() {
        return title;
    }

    public String getMain_photo() {
        return main_photo;
    }

    public String getAddress() {
        return address;
    }

    public String getAddress_google_lat() {
        return address_google_lat;
    }

    public String getAddress_google_long() {
        return address_google_long;
    }

    public String getPhone() {
        return phone;
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
}
