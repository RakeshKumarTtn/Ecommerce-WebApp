package com.org.ecom.enums;

public enum Label {
    INSERTED("HOME"),
    UPDATED("OFFICE"),
    DELETED("");

    private final String labelName;

    private Label(String value) {
        this.labelName = value;
    }

    public String value() {
        return this.labelName;
    }

    @Override
    public String toString() {
        return labelName;
    }
}
