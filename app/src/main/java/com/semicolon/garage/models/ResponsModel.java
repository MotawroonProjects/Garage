package com.semicolon.garage.models;

import java.io.Serializable;

public class ResponsModel implements Serializable {
    private int success_logout;
    private int success_location;
    private  int success_contact;
    private int success_token_id;
    private int success_evaluation;
    private int can_reservation;
    private int success_reservation;
    private  int success_read;
    public int getSuccess_logout() {
        return success_logout;
    }

    public int getSuccess_location() {
        return success_location;
    }

    public int getSuccess_contact() {
        return success_contact;
    }

    public int getSuccess_token_id() {
        return success_token_id;
    }

    public int getSuccess_evaluation() {
        return success_evaluation;
    }

    public int getCan_reservation() {
        return can_reservation;
    }

    public int getSuccess_reservation() {
        return success_reservation;
    }

    public int getSuccess_read() {
        return success_read;
    }
}
