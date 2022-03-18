package org.billing.utility;

public class CommonUtils {

    public static boolean isValidString(String inputString) {
        return (inputString != null && !inputString.trim().isEmpty());
    }

    public static boolean isValidNumber(String s) {
        if (isValidString(s)) {
            try {
                Integer.parseInt(s);
                return true;
            } catch (Exception exception) {
                try {
                    Double.parseDouble(s);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return false;
    }

    public static boolean isValidNumberNotZero(String s) {
        if (isValidString(s)) {
            try {
                if (Integer.parseInt(s) != 0)
                    return true;
            } catch (Exception exception) {
                try {
                    if (Double.parseDouble(s) != 0.0)
                        return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return false;
    }

    public static Double convertToNumeric(String value) {
        double doubleValue = 0.0;
        value = value.replace(",","");
        if (isValidNumberNotZero(value)) {
            doubleValue = Double.parseDouble(value);
        }
        return doubleValue;
    }

    public static boolean isValidMobileNumber(String phoneNumber) {
        String regex = "(0/91)?[6-9][0-9]{9}";
        return phoneNumber.matches(regex);
    }

}
