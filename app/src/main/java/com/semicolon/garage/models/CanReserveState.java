package com.semicolon.garage.models;

import java.io.Serializable;

public class CanReserveState implements Serializable {
    private String can_edit;
    private String can_cancel;

    public String getCan_edit() {
        return can_edit;
    }

    public String getCan_cancel() {
        return can_cancel;
    }
}
