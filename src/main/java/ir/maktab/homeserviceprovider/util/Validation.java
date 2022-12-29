package ir.maktab.homeserviceprovider.util;

import ir.maktab.homeserviceprovider.exception.*;

import java.util.Objects;
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

    public static void checkImage(String imageName, Long imageSize) {
        String[] imageNameSplit = imageName.split("\\.");
        if(!Objects.equals(imageNameSplit[imageNameSplit.length - 1].toLowerCase(), "jpg")){
            throw new ImageFormatException("image must be jpg");
        }
        if(imageSize > 300000L){
            throw new ImageSizeException("image size must be less than 300 kb");
        }
    }


}
