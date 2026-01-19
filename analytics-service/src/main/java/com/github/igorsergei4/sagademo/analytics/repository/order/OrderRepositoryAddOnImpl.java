package com.github.igorsergei4.sagademo.analytics.repository.order;

import com.github.igorsergei4.sagademo.analytics.dto.ResponsePage;
import com.github.igorsergei4.sagademo.analytics.model.EntityWithIdProjection_;
import com.github.igorsergei4.sagademo.analytics.model.execution.Execution;
import com.github.igorsergei4.sagademo.analytics.model.execution.Execution_;
import com.github.igorsergei4.sagademo.analytics.model.order.Order;
import com.github.igorsergei4.sagademo.analytics.model.order.Order_;
import com.github.igorsergei4.sagademo.analytics.model.payment.OrderPayment;
import com.github.igorsergei4.sagademo.analytics.model.payment.OrderPayment_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class OrderRepositoryAddOnImpl implements OrderRepositoryAddOn {
    private final EntityManager entityManager;

    public OrderRepositoryAddOnImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public ResponsePage<Order> getOrders(
            Optional<LocalDateTime> intervalStart,
            Optional<LocalDateTime> intervalEnd,
            Optional<String> orderStatus,
            Optional<String> executionStatus,
            Optional<String> paymentStatus,
            Integer page,
            int pageSize
    ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Order> mainQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> mainQueryRoot = getOrdersInfoQueryRoot(
                criteriaBuilder,
                mainQuery,
                intervalStart,
                intervalEnd,
                orderStatus,
                executionStatus,
                paymentStatus
        );
        addFetchDefinitions(mainQueryRoot);
        TypedQuery<Order> typedQuery = entityManager.createQuery(mainQuery.select(mainQueryRoot));
        typedQuery.setMaxResults(pageSize);
        typedQuery.setFirstResult((page - 1) * pageSize);
        List<Order> data = typedQuery.getResultList();

        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Order> countQueryRoot = getOrdersInfoQueryRoot(
                criteriaBuilder,
                countQuery,
                intervalStart,
                intervalEnd,
                orderStatus,
                executionStatus,
                paymentStatus
        );
        countQuery = countQuery.select(criteriaBuilder.count(countQueryRoot.get(EntityWithIdProjection_.id)));
        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        return new ResponsePage<>(data, totalCount);
    }

    private <ResultT> Root<Order> getOrdersInfoQueryRoot(
            CriteriaBuilder criteriaBuilder,
            CriteriaQuery<ResultT> criteriaQuery,
            Optional<LocalDateTime> intervalStart,
            Optional<LocalDateTime> intervalEnd,
            Optional<String> orderStatus,
            Optional<String> executionStatus,
            Optional<String> paymentStatus
    ) {
        Root<Order> root = criteriaQuery.from(Order.class);

        List<Predicate> queryPredicates = getOrdersInfoQueryPredicates(
                criteriaBuilder,
                root,
                intervalStart,
                intervalEnd,
                orderStatus,
                executionStatus,
                paymentStatus
        );
        if (!queryPredicates.isEmpty()) {
            criteriaQuery.where(queryPredicates);
        }

        return root;
    }

    private List<Predicate> getOrdersInfoQueryPredicates(
            CriteriaBuilder criteriaBuilder,
            Root<Order> root,
            Optional<LocalDateTime> intervalStart,
            Optional<LocalDateTime> intervalEnd,
            Optional<String> orderStatus,
            Optional<String> executionStatus,
            Optional<String> paymentStatus
    ) {
        List<Predicate> predicates = new ArrayList<>(5);

        intervalStart.ifPresent(localDateTime -> {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(Order_.createdAt), localDateTime));
        });
        intervalEnd.ifPresent(localDateTime -> {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(Order_.createdAt), localDateTime));
        });
        orderStatus.ifPresent(status -> {
            predicates.add(root.get(Order_.status).equalTo(status));
        });
        executionStatus.ifPresent(status -> {
            Join<Order, Execution> executionJoin = root.join(Order_.execution, JoinType.LEFT);
            predicates.add(executionJoin.get(Execution_.status).equalTo(status));
        });
        paymentStatus.ifPresent(status -> {
            Join<Order, OrderPayment> orderPaymentJoin = root.join(Order_.orderPayment, JoinType.LEFT);
            predicates.add(orderPaymentJoin.get(OrderPayment_.status).equalTo(status));
        });

        return predicates;
    }

    private void addFetchDefinitions(Root<Order> queryRoot) {
        queryRoot.fetch(Order_.client, JoinType.LEFT);
        queryRoot.fetch(Order_.offering, JoinType.LEFT);
        queryRoot.fetch(Order_.executor, JoinType.LEFT);

        Fetch<Order, Execution> executionJoin = queryRoot.fetch(Order_.execution, JoinType.LEFT);
        executionJoin.fetch(Execution_.executor, JoinType.LEFT);
        executionJoin.fetch(Execution_.offering, JoinType.LEFT);
        executionJoin.fetch(Execution_.client, JoinType.LEFT);

        Fetch<Order, OrderPayment> orderPaymentJoin = queryRoot.fetch(Order_.orderPayment, JoinType.LEFT);
        orderPaymentJoin.fetch(OrderPayment_.client, JoinType.LEFT);
    }
}
