package com.semicolon.garage.models;

import java.io.Serializable;

public class ResponsModel implements Serializable {
    private int success_logout;
    private int success_location;
    private  int success_contact;
    public int getSuccess_logout() {
        return success_logout;
    }

    public int getSuccess_location() {
        return success_location;
    }

    public int getSuccess_contact() {
        return success_contact;
    }
}
