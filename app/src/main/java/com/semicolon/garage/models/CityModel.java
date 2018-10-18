package com.semicolon.garage.models;

import java.io.Serializable;

public class CityModel implements Serializable {
    private String id_city;
    private String ar_city_title;
    private String en_city_title;

    public String getId_city() {
        return id_city;
    }

    public String getAr_city_title() {
        return ar_city_title;
    }

    public String getEn_city_title() {
        return en_city_title;
    }
}
