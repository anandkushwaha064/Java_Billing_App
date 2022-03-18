package org.billing.pages;

public class MessageScreen {
    public static void showMessage(String message) {
        System.out.println("Message : " + message);
    }

    public static void showAlertMessage(String message) {
        System.out.println("Alert : " + message);
    }

    public static void showException(String message) {
        System.out.println("Error : " + message);

    }
}
