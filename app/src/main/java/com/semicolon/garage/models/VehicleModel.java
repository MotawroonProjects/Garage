package com.semicolon.garage.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class VehicleModel implements Serializable {
    private String id_car_maintenance;
    private String title;
    private String car_trademarks;
    private String main_photo;
    private String category_id_fk;
    private String cost;
    private String size;
    private String details;
    private String car_model;
    private String address;
    private String address_google_lat;
    private String address_google_long;
    private String city;
    private String phone;
    private String mobile;
    private String email;
    private String evaluation_count;
    private String clients_count;
    private String nationality;
    private String id_country;
    private String ar_name;
    private String en_name;
    private String ar_nationality;
    private String en_nationality;
    @SerializedName("gallary_color")
    private List<GalleryColor> gallary_color;
    @SerializedName("gallary_inside")
    private List<GalleryInside> gallary_inside;




    public String getId_car_maintenance() {
        return id_car_maintenance;
    }

    public String getTitle() {
        return title;
    }

    public String getMain_photo() {
        return main_photo;
    }

    public String getCategory_id_fk() {
        return category_id_fk;
    }

    public String getCost() {
        return cost;
    }

    public String getSize() {
        return size;
    }

    public String getDetails() {
        return details;
    }

    public String getCar_model() {
        return car_model;
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

    public String getCity() {
        return city;
    }

    public String getPhone() {
        return phone;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getEvaluation_count() {
        return evaluation_count;
    }

    public String getClients_count() {
        return clients_count;
    }

    public String getNationality() {
        return nationality;
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

    public String getCar_trademarks() {
        return car_trademarks;
    }

    public List<GalleryColor> getGallary_color() {
        return gallary_color;
    }

    public List<GalleryInside> getGallary_inside() {
        return gallary_inside;
    }

    public class GalleryColor implements Serializable
    {
        private String id_photo;
        private String photo_name;

        public String getId_photo() {
            return id_photo;
        }

        public String getPhoto_name() {
            return photo_name;
        }
    }

    public class GalleryInside implements Serializable
    {
        private String id_photo;
        private String photo_name;

        public String getId_photo() {
            return id_photo;
        }

        public String getPhoto_name() {
            return photo_name;
        }
    }
}
