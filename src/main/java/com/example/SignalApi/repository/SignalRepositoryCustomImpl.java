package com.example.SignalApi.repository;

import com.example.SignalApi.dto.SignalFilterDto;
import com.example.SignalApi.entities.Signal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

public class SignalRepositoryCustomImpl implements SignalRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Signal> findWithFilters(SignalFilterDto filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Signal> query = cb.createQuery(Signal.class);
        Root<Signal> root = query.from(Signal.class);

        List<Predicate> predicates = new ArrayList<>();

        if (filter.getStartDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getStartDate()));
        }

        if (filter.getEndDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.getEndDate()));
        }

        if (filter.getType() != null) {
            predicates.add(cb.equal(root.get("type"), filter.getType()));
        }

        if (filter.getStatus() != null) {
            predicates.add(cb.equal(root.get("status"), filter.getStatus()));
        }

        if (Boolean.TRUE.equals(filter.getHasPhotoAttached())) {
            predicates.add(cb.isNotEmpty(root.get("images")));
        } else if (Boolean.FALSE.equals(filter.getHasPhotoAttached())) {
            predicates.add(cb.isEmpty(root.get("images")));
        }

        if (filter.getCommunityId() != null) {
            predicates.add(cb.equal(root.get("communityId"), filter.getCommunityId()));
        }

        if (Boolean.TRUE.equals(filter.getActiveOnly())) {
            predicates.add(cb.isTrue(root.get("isActive")));
        }

        if (filter.getSearch() != null && !filter.getSearch().isBlank()) {
            String pattern = "%" + filter.getSearch().toLowerCase() + "%";
            Predicate textMatch = cb.like(cb.lower(root.get("text")), pattern);
            Predicate rawTextMatch = cb.like(cb.lower(root.get("rawText")), pattern);
            Predicate addressMatch = cb.like(cb.lower(root.get("address")), pattern);
            Predicate referenceMatch = cb.like(cb.lower(root.get("reference")), pattern);
            predicates.add(cb.or(textMatch, rawTextMatch, addressMatch, referenceMatch));
        }

        // Cursor-based pagination: fetch records after the cursor position
        if (filter.getCursorAt() != null && filter.getCursorId() != null) {
            Predicate olderThanCursor = cb.lessThan(root.get("createdAt"), filter.getCursorAt());
            Predicate sameTimeButAfterId = cb.and(
                    cb.equal(root.get("createdAt"), filter.getCursorAt()),
                    cb.greaterThan(root.get("id"), filter.getCursorId())
            );
            predicates.add(cb.or(olderThanCursor, sameTimeButAfterId));
        }

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.desc(root.get("createdAt")), cb.asc(root.get("id")));

        // Fetch limit + 1 to determine if there are more results
        return entityManager.createQuery(query)
                .setMaxResults(filter.getLimit() + 1)
                .getResultList();
    }
}
