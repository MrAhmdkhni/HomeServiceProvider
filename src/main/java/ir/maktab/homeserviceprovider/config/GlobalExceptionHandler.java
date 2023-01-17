package ir.maktab.homeserviceprovider.config;

import ir.maktab.homeserviceprovider.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CommentNotFoundException.class)
    public ResponseEntity<Object> commentNotFoundExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = CustomerNotFoundException.class)
    public ResponseEntity<Object> customerNotFoundExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = DuplicateEmailException.class)
    public ResponseEntity<Object> duplicateEmailExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = DuplicateMainServiceException.class)
    public ResponseEntity<Object> mainServiceAlreadyExistExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = DuplicateSubServiceException.class)
    public ResponseEntity<Object> duplicateSubServiceExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


    @ExceptionHandler(value = DuplicateUsernameException.class)
    public ResponseEntity<Object> duplicateUsernameExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = ExpertActivationException.class)
    public ResponseEntity<Object> expertActivationExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = ExpertNoAccessException.class)
    public ResponseEntity<Object> expertNoAccessExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = ExpertNotFoundException.class)
    public ResponseEntity<Object> expertNotFoundExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = FileNotFoundException.class)
    public ResponseEntity<Object> fileNotFoundExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = ImageFormatException.class)
    public ResponseEntity<Object> imageFormatExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = ImageSizeException.class)
    public ResponseEntity<Object> imageSizeExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = InsufficientFoundsException.class)
    public ResponseEntity<Object> insufficientFoundsExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = MainServiceNotFoundException.class)
    public ResponseEntity<Object> mainServiceNotFoundExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = NegativeCreditException.class)
    public ResponseEntity<Object> negativeCreditExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = NullCommentException.class)
    public ResponseEntity<Object> nullCommentExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = NullScoreException.class)
    public ResponseEntity<Object> nullScoreExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = OfferAcceptedException.class)
    public ResponseEntity<Object> offerAcceptedExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = OfferNotFoundException.class)
    public ResponseEntity<Object> offerNotFoundExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = OrderNotFoundException.class)
    public ResponseEntity<Object> orderNotFoundExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = OrderStatusException.class)
    public ResponseEntity<Object> orderStatusExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = PriceException.class)
    public ResponseEntity<Object> priceExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = RegistrationException.class)
    public ResponseEntity<Object> duplicateTokenExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = ScoreException.class)
    public ResponseEntity<Object> scoreExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = SubServiceNotFoundException.class)
    public ResponseEntity<Object> subServiceNotFoundExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = UnRelatedExpertSubServiceException.class)
    public ResponseEntity<Object> unRelatedExpertSubServiceExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = TokenNotFoundException.class)
    public ResponseEntity<Object> tokenNotFoundExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<Object> usernameNotFoundExceptionHandler(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
