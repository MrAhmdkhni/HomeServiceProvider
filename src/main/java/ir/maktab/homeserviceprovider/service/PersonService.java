package ir.maktab.homeserviceprovider.service;

import ir.maktab.homeserviceprovider.base.service.BaseService;
import ir.maktab.homeserviceprovider.entity.person.Person;

import java.util.Optional;

public interface PersonService extends BaseService<Person, Long> {

    Optional<Person> findByUsername(String username);
}
