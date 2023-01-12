package ir.maktab.homeserviceprovider.util;

import ir.maktab.homeserviceprovider.exception.*;

import java.util.Objects;

public class Validation {

    private Validation() {}

    public static void checkImage(String imageName, Long imageSize) {
        String[] imageNameSplit = imageName.split("\\.");
        if(!Objects.equals(imageNameSplit[imageNameSplit.length - 1].toLowerCase(), "jpg"))
            throw new ImageFormatException("image must be jpg");
        if(imageSize > 300000L)
            throw new ImageSizeException("image size must be less than 300 kb");
    }
}
