package ir.maktab.homeserviceprovider.exception;

public class DuplicatePhoneNumberException extends RuntimeException {

    public DuplicatePhoneNumberException(String message) {
        super(message);
    }
}
