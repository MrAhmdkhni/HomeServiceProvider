package ir.maktab.homeserviceprovider.util;

import ir.maktab.homeserviceprovider.exception.EmailFormatException;
import ir.maktab.homeserviceprovider.exception.PasswordFormatException;
import ir.maktab.homeserviceprovider.exception.PhoneNumberFormatException;

import java.util.regex.Pattern;

public class Validation {

    private Validation() {}

    public static void checkPhoneNumber(String phoneNumber) {
        String phoneNumberRegex = "^\\d{11}$";
        if (!Pattern.matches(phoneNumberRegex, phoneNumber)) {
            throw new PhoneNumberFormatException("the format of the mobile number is incorrect!");
        }
    }
    public static void checkEmail(String email) {
        String emailRegex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        if (!Pattern.matches(emailRegex, email)) {
            throw new EmailFormatException("the format of the email is incorrect!");
        }
    }
    public static void checkPassword(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8}$";
        if (!Pattern.matches(passwordRegex, password)) {
            throw new PasswordFormatException("the format of the password is incorrect!");
        }
    }


}
