package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.entity.person.Person;
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

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return personRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username %s not found", username)));
    }

    @Override
    @Transactional
    public Optional<Person> findByUsername(String username) {
        try {
            return personRepository.findByUsername(username);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
