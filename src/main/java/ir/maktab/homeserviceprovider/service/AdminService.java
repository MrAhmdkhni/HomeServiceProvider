package ir.maktab.homeserviceprovider.service;

import ir.maktab.homeserviceprovider.base.service.BaseService;
import ir.maktab.homeserviceprovider.dto.CustomerFilterDTO;
import ir.maktab.homeserviceprovider.dto.ExpertFilterDTO;
import ir.maktab.homeserviceprovider.dto.OrderFilterDTO;
import ir.maktab.homeserviceprovider.entity.order.Order;
import ir.maktab.homeserviceprovider.entity.order.OrderStatus;
import ir.maktab.homeserviceprovider.entity.person.Admin;
import ir.maktab.homeserviceprovider.entity.person.Customer;
import ir.maktab.homeserviceprovider.entity.person.Expert;
import ir.maktab.homeserviceprovider.entity.person.ExpertStatus;
import ir.maktab.homeserviceprovider.entity.service.MainService;
import ir.maktab.homeserviceprovider.entity.service.SubService;

import java.util.List;
import java.util.Set;

public interface AdminService extends BaseService<Admin, Long> {

    void addMainService(MainService mainService);

    int deleteMainService(Long mainServiceId);

    void addSubService(Long mainServiceId, SubService subService);

    int deleteSubService(Long subServiceId);

    void addExpertToSubService(Long subServiceId, Long expertId);

    void deleteExpertFromSubService(Long subServiceId, Long expertId);

    List<MainService> findAllMainService();

    List<SubService> findAllSubService();

    int editSubServiceBasePriceAndDescription(Long subServiceId, Long basePrice, String description);

    List<Expert> findAllExpert();

    int changeExpertStatus(Long expertId, ExpertStatus expertStatus);

    boolean checkExpertDelayForDoingWork(Long offerId);

    int changeExpertActivation(Long expertId, Boolean isActive);

    List<Expert> expertsFilter(ExpertFilterDTO expertDTO);

    List<Customer> customersFilter(CustomerFilterDTO customerDTO);

    Set<Expert> viewSubServiceExperts(Long subServiceId);

    List<Order> ordersFilter(OrderFilterDTO orderFilterDTO);

    List<Order> viewExpertOrderHistory(Long expertId, boolean isAccept);

    List<Order> viewExpertOrderHistory(Long expertId, boolean isAccept, OrderStatus orderStatus);

    List<Order> viewCustomerOrderHistory(Long customerId);

    List<Order> viewCustomerOrderHistory(Long customerId, OrderStatus orderStatus);

    int calculateNumberOfRegisteredOrders(Long customerId);

    int calculateNumberOfRegisteredOrders(Long customerId, OrderStatus orderStatus);

    int calculateNumberOfPlacedOrders(Long expertId);

    int calculateNumberOfPlacedOrders(Long expertId, Boolean isAccept);
}
