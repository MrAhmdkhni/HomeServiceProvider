package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.base.service.impl.BaseServiceImpl;
import ir.maktab.homeserviceprovider.entity.person.Person;
import ir.maktab.homeserviceprovider.repository.PersonRepository;
import ir.maktab.homeserviceprovider.service.PersonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PersonServiceImpl
        extends BaseServiceImpl<Person, Long, PersonRepository>
        implements PersonService {

    public PersonServiceImpl(PersonRepository repository) {
        super(repository);
    }


    @Override
    @Transactional
    public Optional<Person> findByUsername(String username) {
        try {
            return repository.findByUsername(username);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
