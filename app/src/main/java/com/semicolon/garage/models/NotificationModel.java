package com.semicolon.garage.models;

import java.io.Serializable;

public class NotificationModel implements Serializable {
    private String id_reservation;
    private String id_car_maintenance;
    private String approved;
    private String approved_date;
    private String title;
    private String main_photo;
    private String reservation_start_date;
    private String reservation_end_date;

    public String getId_reservation() {
        return id_reservation;
    }

    public String getId_car_maintenance() {
        return id_car_maintenance;
    }

    public String getApproved() {
        return approved;
    }

    public String getTitle() {
        return title;
    }

    public String getMain_photo() {
        return main_photo;
    }

    public String getReservation_start_date() {
        return reservation_start_date;
    }

    public String getReservation_end_date() {
        return reservation_end_date;
    }

    public String getApproved_date() {
        return approved_date;
    }
}
