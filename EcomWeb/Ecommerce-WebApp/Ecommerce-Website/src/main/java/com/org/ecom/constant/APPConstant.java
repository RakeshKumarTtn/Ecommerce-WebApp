package com.org.ecom.constant;

public interface APPConstant {

    public static final int INVALID_ATTEMPT_COUNT = 3;
    public static final Boolean IS_DELETED = false;
    public static final Boolean IS_ACTIVE = false;
    public static final Boolean IS_EXPIRED = false;
    public static final Boolean IS_LOCKED = false;


    public static final StringBuffer AFTER_RESET_EMAIL_MESSAGE = new StringBuffer("Hello ," + "\n" +
            "We wanted to let you know that your Ecommerce-App password was reset.\n" + "\n" +
            "Please do not reply to this email with your password. We will never ask for your password, and we strongly discourage you from sharing it with anyone.");

    public static final StringBuffer BEFORE_RESET_EMAIL_MESSAGE = new StringBuffer("Reset your Ecommerce-App password " + "\n" + " \n" + "\n" +
            "We heard that you lost your Ecommerce-App password. Sorry about that!\n" + "\n" +
            "But don’t worry! You can use the following token to reset your password: " + "\n" +
            "\n\nIf you don’t use this token within 5 Minute, it will expire. To get a new password reset token hit below API, " +
            "\n\nhttp://localhost:8080/api/v1/user/forgot_password\n" + "\n" + "Thanks,\n" + "The Ecommerce-App Team ");
}