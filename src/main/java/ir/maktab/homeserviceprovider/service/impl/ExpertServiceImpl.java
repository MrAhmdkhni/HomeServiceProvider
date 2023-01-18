package ir.maktab.homeserviceprovider.service.impl;

import ir.maktab.homeserviceprovider.base.service.impl.BaseServiceImpl;
import ir.maktab.homeserviceprovider.dto.ExpertFilterDTO;
import ir.maktab.homeserviceprovider.entity.comment.Comment;
import ir.maktab.homeserviceprovider.entity.offer.Offer;
import ir.maktab.homeserviceprovider.entity.order.Order;
import ir.maktab.homeserviceprovider.entity.order.OrderStatus;
import ir.maktab.homeserviceprovider.entity.person.*;
import ir.maktab.homeserviceprovider.entity.token.ConfirmationToken;
import ir.maktab.homeserviceprovider.exception.*;
import ir.maktab.homeserviceprovider.repository.ExpertRepository;
import ir.maktab.homeserviceprovider.service.*;
import ir.maktab.homeserviceprovider.util.Validation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExpertServiceImpl
        extends BaseServiceImpl<Expert, Long, ExpertRepository> implements ExpertService {

    private final OfferService offerService;
    private final OrderService orderService;
    private final CommentService commentService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSenderService emailSenderService;
    @PersistenceContext
    private EntityManager entityManager;

    public ExpertServiceImpl(ExpertRepository repository, OfferService offerService, OrderService orderService, CommentService commentService, BCryptPasswordEncoder passwordEncoder, ConfirmationTokenService confirmationTokenService, EmailSenderService emailSenderService) {
        super(repository);
        this.offerService = offerService;
        this.orderService = orderService;
        this.commentService = commentService;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenService = confirmationTokenService;
        this.emailSenderService = emailSenderService;
    }


    @Override
    public Optional<Expert> findByEmail(String email) {
        try {
            return repository.findByEmail(email);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Expert> findByUsername(String username) {
        try {
            return repository.findByUsername(username);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public int editPassword(Long expertId, String newPassword) {
        if (findById(expertId).isEmpty())
            throw new ExpertNotFoundException("this expert does not exist!");
        return repository.editPassword(expertId, passwordEncoder.encode(newPassword));
    }

    @Override
    @Transactional
    public int changeExpertStatus(Long expertId, ExpertStatus expertStatus) {
        return repository.changeExpertStatus(expertId, expertStatus);
    }

    @Override
    public void signUp(Expert expert, String imageName, Long imageSize) {
        if (findByEmail(expert.getEmail()).isPresent())
            throw new DuplicateEmailException("this email already exist!");
        if (findByUsername(expert.getUsername()).isPresent())
            throw new DuplicateUsernameException("this username already exist!");
        Validation.checkImage(imageName, imageSize);

        expert.setPassword(passwordEncoder.encode(expert.getPassword()));
        expert.setExpertStatus(ExpertStatus.NEW);
        expert.setRole(Role.ROLE_EXPERT);
        expert.setIsActive(false);
        expert.setCredit(0L);
        expert.setScore(0);
        saveOrUpdate(expert);
    }

    @Override
    public void signUpWithValidation(Expert expert, String imageName, Long imageSize) {
        if (findByEmail(expert.getEmail()).isPresent())
            throw new DuplicateEmailException("this email already exist!");
        if (findByUsername(expert.getUsername()).isPresent())
            throw new DuplicateUsernameException("this username already exist!");
        Validation.checkImage(imageName, imageSize);

        expert.setPassword(passwordEncoder.encode(expert.getPassword()));
        expert.setExpertStatus(ExpertStatus.NEW);
        expert.setRole(Role.ROLE_EXPERT);
        expert.setIsActive(false);
        expert.setCredit(0L);
        expert.setScore(0);
        saveOrUpdate(expert);

        ConfirmationToken confirmationToken = new ConfirmationToken(expert);
        confirmationToken.setConfirmationToken(UUID.randomUUID().toString());
        confirmationTokenService.saveOrUpdate(confirmationToken);

        SimpleMailMessage mailMessage = emailSenderService.createEmail(expert.getEmail(), confirmationToken.getConfirmationToken(), "expert");
        emailSenderService.sendEmail(mailMessage);
    }

    @Override
    @Transactional
    public void confirmExpertAccount(String confirmationToken) {
        Optional<ConfirmationToken> token = confirmationTokenService.findByConfirmationToken(confirmationToken);
        if (token.isEmpty())
            throw new TokenNotFoundException("token not found!");
        if (token.get().getPerson().getIsActive())
            throw new RegistrationException("this expert's account is currently active!");
        Optional<Expert> expert = findByEmail(token.get().getPerson().getEmail());
        if (expert.isEmpty())
            throw new ExpertNotFoundException("there is no expert!");
        expert.get().setIsActive(true);
        expert.get().setExpertStatus(ExpertStatus.AWAITING);
        saveOrUpdate(expert.get());
    }

    @Override
    public List<Order> viewOrdersRelatedToExpert(Long expertId) {
        List<Order> orders = new ArrayList<>();
        Optional<Expert> expert = findById(expertId);
        if (expert.isEmpty())
            throw new ExpertNotFoundException("this expert does not exist!");
        expert.get().getSubServices().forEach(subService -> {
            List<Order> filteredOrder = orderService
                    .findBySubServiceIdAndOrderStatus(
                            subService.getId(),
                            OrderStatus.WAITING_FOR_EXPERT_SELECTION,
                            OrderStatus.WAITING_FOR_EXPERT_SUGGESTION);
            orders.addAll(filteredOrder);
        });
        return orders;
    }

    @Override
    public void addOfferForOrder(Long expertId, Long orderId, Offer offer) {
        Optional<Expert> expert = findById(expertId);
        if (expert.isEmpty())
            throw new ExpertNotFoundException("this expert does not exist!");
        if (!expert.get().getExpertStatus().equals(ExpertStatus.CONFIRMED))
            throw new ExpertNoAccessException("the status of expert is not \"confirmed\"");
        Optional<Order> order = orderService.findById(orderId);
        if (order.isEmpty())
            throw new OrderNotFoundException("there is no order!");

        if (!expert.get().getSubServices().contains(order.get().getSubService()))
            throw new UnRelatedExpertSubServiceException("this expert is not related to this sub-service!");
        if (order.get().getSubService().getBasePrice() > offer.getProposedPrice())
            throw new PriceException("the proposed-price should not be lower than the base-price!");
        if (!order.get().getOrderStatus().equals(OrderStatus.WAITING_FOR_EXPERT_SUGGESTION))
            throw new OrderStatusException("the status of this order not \"WAITING FOR EXPERT SUGGESTION\"!");

        offer.setIsAccept(false);
        offer.setExpert(expert.get());
        offer.setOrder(order.get());
        offerService.saveOrUpdate(offer);
        orderService.changeOrderStatus(orderId, OrderStatus.WAITING_FOR_EXPERT_SUGGESTION, OrderStatus.WAITING_FOR_EXPERT_SELECTION);
    }

    @Override
    @Transactional
    public int changeExpertActivation(Long expertId, Boolean isActive) {
        return repository.changeExpertActivation(expertId, isActive);
    }

    @Override
    @Transactional
    public int updateScore(Long expertId, Integer newScore) {
        return repository.updateScore(expertId, newScore);
    }

    @Override
    public int viewExpertScore(Long expertId) {
        Optional<Expert> expert = findById(expertId);
        if (expert.isEmpty())
            throw new ExpertNotFoundException("this expert does not exist!");
        return expert.get().getScore();
    }

    @Override
    public int viewExpertScoreFromCustomerComment(Long commentId) {
        Optional<Comment> comment = commentService.findById(commentId);
        if (comment.isEmpty())
            throw new CommentNotFoundException("this comment does not exist!");
        return comment.get().getScore();
    }

    @Override
    public void updateCredit(Long expertId, Long newCredit) {
        repository.updateCredit(expertId, newCredit);
    }

    @Override
    @Transactional
    public List<Expert> expertsFilter(ExpertFilterDTO expertDTO) {
        List<Predicate> predicateList = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Expert> expertCriteriaQuery = criteriaBuilder.createQuery(Expert.class);
        Root<Expert> expertRoot = expertCriteriaQuery.from(Expert.class);

        createFilters(expertDTO, predicateList, criteriaBuilder, expertRoot);
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        expertCriteriaQuery.select(expertRoot).where(predicates);
        return entityManager.createQuery(expertCriteriaQuery).getResultList();
    }

    private void createFilters(ExpertFilterDTO expertDTO, List<Predicate> predicateList, CriteriaBuilder criteriaBuilder, Root<Expert> expertRoot) {
        if (expertDTO.getFirstname() != null) {
            String firstname = "%" + expertDTO.getFirstname() + "%";
            predicateList.add(criteriaBuilder.like(expertRoot.get("firstname"), firstname));
        }
        if (expertDTO.getLastname() != null) {
            String lastname = "%" + expertDTO.getLastname() + "%";
            predicateList.add(criteriaBuilder.like(expertRoot.get("lastname"), lastname));
        }
        if (expertDTO.getEmail() != null) {
            String email = "%" + expertDTO.getEmail() + "%";
            predicateList.add(criteriaBuilder.like(expertRoot.get("email"), email));
        }
        if (expertDTO.getUsername() != null) {
            String username = "%" + expertDTO.getUsername() + "%";
            predicateList.add(criteriaBuilder.like(expertRoot.get("username"), username));
        }
        if (expertDTO.getIsActive() != null) {
            predicateList.add(criteriaBuilder.equal(expertRoot.get("isActive"), expertDTO.getIsActive()));
        }
        if (expertDTO.getExpertStatus() != null) {
            predicateList.add(criteriaBuilder.equal(expertRoot.get("expertStatus"), expertDTO.getExpertStatus()));
        }

        if (expertDTO.getMinScore() == null && expertDTO.getMaxScore() != null) {
            predicateList.add(criteriaBuilder.lt(expertRoot.get("score"), expertDTO.getMaxScore()));
        }
        if (expertDTO.getMinScore() != null && expertDTO.getMaxScore() == null) {
            predicateList.add(criteriaBuilder.gt(expertRoot.get("score"), expertDTO.getMinScore()));
        }
        if (expertDTO.getMinScore() != null && expertDTO.getMaxScore() != null) {
            predicateList.add(criteriaBuilder.between(expertRoot.get("score"), expertDTO.getMinScore(), expertDTO.getMaxScore()));
        }

        if (expertDTO.getMinCredit() == null && expertDTO.getMaxCredit() != null) {
            predicateList.add(criteriaBuilder.lt(expertRoot.get("credit"), expertDTO.getMaxCredit()));
        }
        if (expertDTO.getMinCredit() != null && expertDTO.getMaxCredit() == null) {
            predicateList.add(criteriaBuilder.gt(expertRoot.get("credit"), expertDTO.getMinCredit()));
        }
        if (expertDTO.getMinCredit() != null && expertDTO.getMaxCredit() != null) {
            predicateList.add(criteriaBuilder.between(expertRoot.get("credit"), expertDTO.getMinCredit(), expertDTO.getMaxCredit()));
        }

        if (expertDTO.getMinCreationDate() != null && expertDTO.getMaxCreationDate() != null) {
            predicateList
                    .add(criteriaBuilder
                            .between(expertRoot.get("creationDate"),
                                    LocalDateTime.parse(expertDTO.getMinCreationDate()),
                                    LocalDateTime.parse(expertDTO.getMaxCreationDate())));
        }
    }

    @Override
    public List<Offer> viewOfferHistory(Long expertId, boolean isAccept) {
        Optional<Expert> expert = findById(expertId);
        if (expert.isEmpty())
            throw new ExpertNotFoundException("this expert does not exist!");
        return offerService.findOfferByExpertIdAndIsAccept(expertId, isAccept);
    }

    @Override
    public List<Order> viewOrderHistory(Long expertId, boolean isAccept) {
        List<Order> orders = new ArrayList<>();
        List<Offer> expertOffers = viewOfferHistory(expertId, isAccept);
        expertOffers.forEach(offer ->
                orders.add(offer.getOrder()));
        return orders;
    }

    @Override
    public List<Order> viewOrderHistory(Long expertId, boolean isAccept, OrderStatus orderStatus) {
        List<Order> orders = new ArrayList<>();
        List<Offer> expertOffers = viewOfferHistory(expertId, isAccept);
        expertOffers.stream()
                .filter(offer -> offer.getOrder().getOrderStatus().equals(orderStatus))
                .forEach(offer -> {
                    orders.add(offer.getOrder());
                });
        return orders;
    }

    @Override
    public Long viewCredit(Long expertId) {
        Optional<Expert> expert = findById(expertId);
        if (expert.isEmpty())
            throw new ExpertNotFoundException("this expert does not exist!");
        return expert.get().getCredit();
    }
}
