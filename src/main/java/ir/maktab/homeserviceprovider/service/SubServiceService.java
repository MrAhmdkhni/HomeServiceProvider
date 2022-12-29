package ir.maktab.homeserviceprovider.service;

import ir.maktab.homeserviceprovider.base.service.BaseService;
import ir.maktab.homeserviceprovider.entity.service.SubService;

import java.util.Optional;

public interface SubServiceService extends BaseService<SubService, Long> {

    Optional<SubService> findByName(String name);

    int deleteSubServiceById(Long subServiceId);

    int editBasePriceAndDescription(Long subServiceId, Long basePrice, String description);
}
