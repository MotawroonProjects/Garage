package com.semicolon.garage.models;

import java.io.Serializable;

public class ResponsModel implements Serializable {
    private int success_logout;
    private int success_location;
    private  int success_contact;
    private int success_token_id;
    private int success_evaluation;
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
}
