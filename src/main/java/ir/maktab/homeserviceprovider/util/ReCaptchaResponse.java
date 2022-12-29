package ir.maktab.homeserviceprovider.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReCaptchaResponse {

    private boolean success;
    private String challenge_td;
    private String hostname;

}
