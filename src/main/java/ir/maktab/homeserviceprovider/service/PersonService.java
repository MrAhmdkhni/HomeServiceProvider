package ir.maktab.homeserviceprovider.service;

import ir.maktab.homeserviceprovider.entity.person.Person;

import java.util.Optional;

public interface PersonService {

    Optional<Person> findByUsername(String username);
}
