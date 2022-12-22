package ir.maktab.homeserviceprovider.service;

import ir.maktab.homeserviceprovider.base.service.BaseService;
import ir.maktab.homeserviceprovider.entity.person.Admin;
import ir.maktab.homeserviceprovider.entity.person.Expert;
import ir.maktab.homeserviceprovider.entity.person.ExpertStatus;
import ir.maktab.homeserviceprovider.entity.service.MainService;
import ir.maktab.homeserviceprovider.entity.service.SubService;

import java.util.List;

public interface AdminService extends BaseService<Admin, Long> {

    void addMainService(MainService mainService);

    int deleteMainService(MainService mainService);

    void addSubService(Long mainServiceId, SubService subService);

    int deleteSubService(SubService subService);

    void addExpertToSubService(Long subServiceId, Long expertId);

    void deleteExpertFromSubService(Long subServiceId, Long expertId);

    List<MainService> findAllMainService();

    List<SubService> findAllSubService();

    int editSubServiceBasePriceAndDescription(Long subServiceId, Long basePrice, String description);

    List<Expert> findAllExpert();

    int changeExpertStatus(Long expertId, ExpertStatus expertStatus);

}
