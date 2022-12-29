package ir.maktab.homeserviceprovider.exception;

public class NegativeCreditException extends RuntimeException {

    public NegativeCreditException(String message) {
        super(message);
    }
}
