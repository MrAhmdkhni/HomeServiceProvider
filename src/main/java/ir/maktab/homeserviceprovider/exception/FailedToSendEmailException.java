package ir.maktab.homeserviceprovider.exception;

public class FailedToSendEmailException extends RuntimeException {

    public FailedToSendEmailException(String message) {
        super(message);
    }
}
