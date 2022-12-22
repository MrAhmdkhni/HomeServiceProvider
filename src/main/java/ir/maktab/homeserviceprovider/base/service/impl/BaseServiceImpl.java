package ir.maktab.homeserviceprovider.base.service.impl;

import ir.maktab.homeserviceprovider.base.entity.BaseEntity;
import ir.maktab.homeserviceprovider.base.service.BaseService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BaseServiceImpl<E extends BaseEntity<ID>,
        ID extends Serializable, R extends JpaRepository<E, ID>>
        implements BaseService<E, ID> {

    protected final R repository;

    public BaseServiceImpl(R repository) {
        this.repository = repository;
    }


    @Override
    @Transactional
    public void saveOrUpdate(E e) {
        repository.save(e);
    }

    @Override
    @Transactional
    public void delete(E e) {
        repository.delete(e);
    }

    @Override
    public Optional<E> findById(ID id) {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<E> findAll() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
