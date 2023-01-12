package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.base.service.impl.BaseServiceImpl;
import ir.maktab.homeserviceprovider.dto.CustomerFilterDTO;
import ir.maktab.homeserviceprovider.entity.token.ConfirmationToken;
import ir.maktab.homeserviceprovider.entity.comment.Comment;
import ir.maktab.homeserviceprovider.entity.offer.Offer;
import ir.maktab.homeserviceprovider.entity.order.Order;
import ir.maktab.homeserviceprovider.entity.order.OrderStatus;
import ir.maktab.homeserviceprovider.entity.person.Customer;
import ir.maktab.homeserviceprovider.entity.person.Expert;
import ir.maktab.homeserviceprovider.entity.person.Person;
import ir.maktab.homeserviceprovider.entity.person.Role;
import ir.maktab.homeserviceprovider.entity.service.MainService;
import ir.maktab.homeserviceprovider.entity.service.SubService;
import ir.maktab.homeserviceprovider.exception.*;
import ir.maktab.homeserviceprovider.repository.CustomerRepository;
import ir.maktab.homeserviceprovider.service.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerServiceImpl
        extends BaseServiceImpl<Customer, Long, CustomerRepository>
        implements CustomerService {

    private final MainServiceService mainServiceService;
    private final SubServiceService subServiceService;
    private final OrderService orderService;
    private final CommentService commentService;
    private final OfferService offerService;
    private final ExpertService expertService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    @PersistenceContext
    private EntityManager entityManager;

    public CustomerServiceImpl(CustomerRepository repository, MainServiceService mainServiceService, SubServiceService subServiceService, OrderService orderService, CommentService commentService, OfferService offerService, ExpertService expertService, BCryptPasswordEncoder passwordEncoder, ConfirmationTokenService confirmationTokenService) {
        super(repository);
        this.mainServiceService = mainServiceService;
        this.subServiceService = subServiceService;
        this.orderService = orderService;
        this.commentService = commentService;
        this.offerService = offerService;
        this.expertService = expertService;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenService = confirmationTokenService;
    }


    @Override
    public Optional<Customer> findByEmail(String email) {
        try {
            return repository.findByEmail(email);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Customer> findByUsername(String username) {
        try {
            return repository.findByUsername(username);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public int editPassword(Long customerId, String newPassword) {
        if (findById(customerId).isEmpty())
            throw new ExpertNotFoundException("this customer does not exist!");
        return repository.editPassword(customerId, passwordEncoder.encode(newPassword));
    }

    @Override
    @Transactional
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public void signUp(Customer customer) {
        if (findByEmail(customer.getEmail()).isPresent())
            throw new DuplicateEmailException("this email already exist!");
        if (findByUsername(customer.getUsername()).isPresent())
            throw new DuplicateUsernameException("this username already exist!");

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRole(Role.ROLE_CUSTOMER);
        customer.setIsActive(false);
        customer.setCredit(0L);
        saveOrUpdate(customer);
    }

    @Override
    public String signUpWithValidation(Customer customer) {
        if (findByEmail(customer.getEmail()).isPresent()) {
            Customer loadedCustomer = findByEmail(customer.getEmail()).get();

            if (!loadedCustomer.getIsActive()) {
                String token = UUID.randomUUID().toString();
                saveConfirmationToken(loadedCustomer, token);
                return token;
            }
            throw new DuplicateEmailException("this email already exist!");
        }
        if (findByUsername(customer.getUsername()).isPresent())
            throw new DuplicateUsernameException("this username already exist!");

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRole(Role.ROLE_CUSTOMER);
        customer.setIsActive(false);
        customer.setCredit(0L);
        saveOrUpdate(customer);

        String token = UUID.randomUUID().toString();
        saveConfirmationToken(customer, token);
        return token;
    }

    private void saveConfirmationToken(Person person, String token) {
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), person
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
    }

    @Override
    public int activeCustomer(String email) {
        return repository.activeCustomer(email);
    }

    @Override
    public List<MainService> findAllMainService() {
        List<MainService> mainServices = mainServiceService.findAll();
        if (mainServices.isEmpty())
            throw new MainServiceNotFoundException("there is no main-services");
        return mainServices;
    }

    @Override
    public List<SubService> findAllSubService() {
        List<SubService> subServices = subServiceService.findAll();
        if (subServices.isEmpty())
            throw new SubServiceNotFoundException("there is no sub-services");
        return subServices;
    }

    @Override
    public void addOrder(Long customerId, Long subServiceId, Order order) {
        Optional<Customer> customer = findById(customerId);
        if (customer.isEmpty())
            throw new CustomerNotFoundException("there is no customers!");
        Optional<SubService> subService = subServiceService.findById(subServiceId);
        if (subService.isEmpty())
            throw new SubServiceNotFoundException("this sub-service dose not exist!");
        order.setOrderStatus(OrderStatus.WAITING_FOR_EXPERT_SUGGESTION);
        order.setCustomer(customer.get());
        order.setSubService(subService.get());
        orderService.saveOrUpdate(order);
    }

    @Override
    public void addCommentForExpertPerformance(Long orderId, Long expertId, Comment comment) {
        if (comment.getScore() == null)
            throw new NullScoreException("The score cannot be null!");
        if (comment.getComment() == null)
            throw new NullCommentException("The comment cannot be null!");
        if (comment.getScore() < 0 && comment.getScore() > 6)
            throw new ScoreException("the expert score should be between 1 and 5!");
        Optional<Order> order = orderService.findById(orderId);
        if (order.isEmpty())
            throw new OrderNotFoundException("there is no orders!");
        Optional<Expert> expert = expertService.findById(expertId);
        if (expert.isEmpty())
            throw new CustomerNotFoundException("this expert does not exist!");
        comment.setOrder(order.get());
        commentService.saveOrUpdate(comment);
        expertService.updateScore(expertId, expert.get().getScore() + comment.getScore());
    }


    @Override
    public List<Offer> viewOrderOffersBaseOnProposedPrice(Long orderId) {
        List<Offer> offers = offerService.findByOrderIdBasedOnProposedPrice(orderId);
        if (offers.isEmpty())
            throw new OfferNotFoundException("there is no offers!");
        return offers;
    }

    @Override
    @Transactional
    public List<Offer> viewOrderOffersBaseOnExpertScore(Long orderId) {
        List<Offer> offers = offerService.findByOrderIdBasedOnExpertScore(orderId);
        if (offers.isEmpty())
            throw new OfferNotFoundException("there is no offers!");
        return offers;
    }

    @Override
    public void selectOffer(Long offerId) {
        Optional<Offer> offer = offerService.findById(offerId);
        if (offer.isEmpty())
            throw new OfferNotFoundException("there is no offers!");
        if (offer.get().getIsAccept().equals(true))
            throw new OfferAcceptedException("this offer accepted!");
        offerService.editIsAccept(offerId, true);

        long orderId = offer.get().getOrder().getId();
        Optional<Order> order = orderService.findById(orderId);
        if (order.isEmpty())
            throw new OrderNotFoundException("there is not orders!");
        if (!order.get().getOrderStatus().equals(OrderStatus.WAITING_FOR_EXPERT_SELECTION))
            throw new OrderStatusException("the status of this order is not yet \"WAITING FOR EXPERT SELECTION\"!");
        orderService.changeOrderStatus(orderId, OrderStatus.WAITING_FOR_EXPERT_SELECTION, OrderStatus.WAITING_FOR_EXPERT_TO_COME);
    }

    @Override
    public int changeOrderStatusAfterExpertComes(Long orderId) {
        Optional<Order> order = orderService.findById(orderId);
        if (order.isEmpty())
            throw new OrderNotFoundException("this order does not exist!");
        if (!order.get().getOrderStatus().equals(OrderStatus.WAITING_FOR_EXPERT_TO_COME))
            throw new OrderStatusException("the status of this order is not yet \"WAITING FOR EXPERT TO COME\"!");
        return orderService.changeOrderStatus(orderId, OrderStatus.WAITING_FOR_EXPERT_TO_COME, OrderStatus.STARTED);
    }

    @Override
    public int changeOrderStatusAfterStarted(Long orderId) {
        Optional<Order> order = orderService.findById(orderId);
        if (order.isEmpty())
            throw new OrderNotFoundException("this order does not exist!");
        if (!order.get().getOrderStatus().equals(OrderStatus.STARTED))
            throw new OrderStatusException("the status of this order is not yet \"STARTED\"!");
        return orderService.changeOrderStatus(orderId, OrderStatus.STARTED, OrderStatus.DONE);
    }

    @Override
    public void updateCredit(Long customerId, Long newCredit) {
        repository.updateCredit(customerId, newCredit);
    }

    @Override
    @Transactional
    public void payFromCredit(Long orderId, Long customerId, Long expertId, Long amount) {
        Optional<Order> order = orderService.findById(orderId);
        if (order.isEmpty())
            throw new OrderNotFoundException("this order does not exist!");
        if (!order.get().getOrderStatus().equals(OrderStatus.DONE))
            throw new OrderStatusException("the status of this order is not yet \"DONE\"!");
        Optional<Customer> customer = findById(customerId);
        if (customer.isEmpty())
            throw new CustomerNotFoundException("this customer does not exit!");
        if (customer.get().getCredit() < amount)
            throw new InsufficientFoundsException("Insufficient founds!");
        Optional<Expert> expert = expertService.findById(expertId);
        if (expert.isEmpty())
            throw new ExpertNotFoundException("this expert does not exist!");
        orderService.changeOrderStatus(orderId, OrderStatus.DONE, OrderStatus.PAID);
        updateCredit(customerId, customer.get().getCredit() - amount);
        expertService.updateCredit(expertId, expert.get().getCredit() + (long) (amount * 0.7));
    }

    @Override
    public List<Customer> customersFilter(CustomerFilterDTO customerDTO) {
        List<Predicate> predicateList = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> customerCriteriaQuery = criteriaBuilder.createQuery(Customer.class);
        Root<Customer> customerRoot = customerCriteriaQuery.from(Customer.class);

        createFilters(customerDTO, predicateList, criteriaBuilder, customerRoot);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        customerCriteriaQuery.select(customerRoot).where(predicates);
        return entityManager.createQuery(customerCriteriaQuery).getResultList();
    }

    private void createFilters(CustomerFilterDTO customerDTO, List<Predicate> predicateList, CriteriaBuilder criteriaBuilder, Root<Customer> customerRoot) {
        if (customerDTO.getFirstname() != null) {
            String firstname = "%" + customerDTO.getFirstname() + "%";
            predicateList.add(criteriaBuilder.like(customerRoot.get("firstname"), firstname));
        }
        if (customerDTO.getLastname() != null) {
            String lastname = "%" + customerDTO.getLastname() + "%";
            predicateList.add(criteriaBuilder.like(customerRoot.get("lastname"), lastname));
        }
        if (customerDTO.getEmail() != null) {
            String email = "%" + customerDTO.getEmail() + "%";
            predicateList.add(criteriaBuilder.like(customerRoot.get("email"), email));
        }
        if (customerDTO.getUsername() != null) {
            String username = "%" + customerDTO.getUsername() + "%";
            predicateList.add(criteriaBuilder.like(customerRoot.get("username"), username));
        }
        if (customerDTO.getIsActive() != null) {
            predicateList.add(criteriaBuilder.equal(customerRoot.get("isActive"), customerDTO.getIsActive()));
        }

        if (customerDTO.getMinCredit() == null && customerDTO.getMaxCredit() != null) {
            predicateList.add(criteriaBuilder.lt(customerRoot.get("credit"), customerDTO.getMaxCredit()));
        }
        if (customerDTO.getMinCredit() != null && customerDTO.getMaxCredit() == null) {
            predicateList.add(criteriaBuilder.gt(customerRoot.get("credit"), customerDTO.getMinCredit()));
        }
        if (customerDTO.getMinCredit() != null && customerDTO.getMaxCredit() != null) {
            predicateList.add(criteriaBuilder.between(customerRoot.get("credit"), customerDTO.getMinCredit(), customerDTO.getMaxCredit()));
        }


        if (customerDTO.getMinCreationDate() != null && customerDTO.getMaxCreationDate() != null) {
            predicateList
                    .add(criteriaBuilder
                            .between(customerRoot.get("creationDate"),
                                    LocalDateTime.parse(customerDTO.getMinCreationDate()),
                                    LocalDateTime.parse(customerDTO.getMaxCreationDate())));
        }
    }

    @Override
    public List<Order> viewOrderHistory(Long customerId) {
        List<Order> orders = orderService.findByCustomerId(customerId);
        if (orders.isEmpty())
            throw new OrderNotFoundException("there is no orders!");
        return orders;
    }

    @Override
    public List<Order> viewOrderHistory(Long customerId, OrderStatus orderStatus) {
        List<Order> orders = orderService.findByCustomerIdAndOrderStatus(customerId, orderStatus);
        if (orders.isEmpty())
            throw new OrderNotFoundException("there is no orders!");
        return orders;
    }

    @Override
    public Long viewCredit(Long customerId) {
        Optional<Customer> customer = findById(customerId);
        if (customer.isEmpty())
            throw new CustomerNotFoundException("this customer does not exit!");
        return customer.get().getCredit();
    }

}
