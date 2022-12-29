package ir.maktab.homeserviceprovider.service;

import ir.maktab.homeserviceprovider.base.service.BaseService;
import ir.maktab.homeserviceprovider.entity.service.MainService;

import java.util.Optional;

public interface MainServiceService extends BaseService<MainService, Long> {

    Optional<MainService> findByName(String name);

    int deleteMainServiceById(Long mainServiceId);
}
