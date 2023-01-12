package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.entity.person.Person;
/*import ir.maktab.homeserviceprovider.registration.VerificationToken;
import ir.maktab.homeserviceprovider.registration.VerificationTokenRepository;*/
import ir.maktab.homeserviceprovider.repository.PersonRepository;
import ir.maktab.homeserviceprovider.service.PersonService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService, UserDetailsService {

    private final PersonRepository personRepository;
    // baeldung
    /*private final VerificationTokenRepository token;*/

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return personRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username %s not found", username)));
    }

    // don't needed
    @Override
    @Transactional
    public Optional<Person> findByUsername(String username) {
        try {
            return personRepository.findByUsername(username);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    //// baeldung

    /*@Override
    public Person getPerson(String verificationToken) {
        return token.findByToken(verificationToken).getPerson();
    }

    @Override
    public void saveRegisteredUser(Person person) {
        personRepository.save(person);
    }

    @Override
    public void createVerificationToken(Person person, String token) {
        VerificationToken myToken = new VerificationToken(token, person);
        this.token.save(myToken);
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return token.findByToken(VerificationToken);
    }*/




    //gitttttttttttttttttttt
    /*@Override
    public void createVerificationTokenForUser(Person person, String token) {
        final VerificationToken myToken = new VerificationToken(token, person);
        tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    @Override
    public void saveRegisteredUser(final Person person) {
        personRepository.save(person);
    }*/
}
