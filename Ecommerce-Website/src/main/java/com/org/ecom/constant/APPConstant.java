package com.org.ecom.constant;

public interface APPConstant {
    //TODO pick Timezone, date format, and date time formate from application properties.
    public static final String TIMEZONE = "Asia/Kolkata";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String DATE_TIME_FORMAT_HM = "dd/MM/yyyy HH:mm";
    public static final String DATE_TIME_FORMAT_HMA = " dd-MMM,EEE hh:mm a";

    public static final int INVALID_ATTEMPT_COUNT = 3;
    public static final Boolean IS_DELETED = false;
    public static final Boolean IS_ACTIVE = false;
    public static final Boolean IS_EXPIRED = false;
    public static final Boolean IS_LOCKED = false;


    public enum CASE_MAP {
        LOST("LOST"),
        INTERNAL_SR("INTERNAL_SR"),
        NEW("NEW");

        private final String value;

        private CASE_MAP(String val) {
            this.value = val;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }
}