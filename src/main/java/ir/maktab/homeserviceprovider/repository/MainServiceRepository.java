package ir.maktab.homeserviceprovider.repository;

import ir.maktab.homeserviceprovider.entity.service.MainService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MainServiceRepository extends JpaRepository<MainService, Long> {

    Optional<MainService> findByName(String name);

    int deleteMainServiceById(Long mainServiceId);
}
