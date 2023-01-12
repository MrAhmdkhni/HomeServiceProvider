package ir.maktab.homeserviceprovider.service;

import ir.maktab.homeserviceprovider.dto.PersonDTO;
import ir.maktab.homeserviceprovider.entity.person.Person;
/*import ir.maktab.homeserviceprovider.registration.VerificationToken;*/

import java.util.Optional;

public interface PersonService {

    Optional<Person> findByUsername(String username);

    // baeldung

    /*Person getPerson(String verificationToken);

    void saveRegisteredUser(Person person);

    void createVerificationToken(Person person, String token);

    VerificationToken getVerificationToken(String VerificationToken);*/

    //gitttttttttttttt
    /*void createVerificationTokenForUser(Person person, String token);

    VerificationToken getVerificationToken(String VerificationToken);

    void saveRegisteredUser(final Person person);*/
}
