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

        List<Predicate> predicates = buildPredicates(cb, root, filter);

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

    @Override
    public long countWithFilters(SignalFilterDto filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Signal> root = query.from(Signal.class);

        List<Predicate> predicates = buildPredicates(cb, root, filter);

        query.select(cb.count(root));
        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getSingleResult();
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Signal> root, SignalFilterDto filter) {
        List<Predicate> predicates = new ArrayList<>();

        // Date range filtering on occurredAt
        if (filter.getStartDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("occurredAt"), filter.getStartDate()));
        }

        if (filter.getEndDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("occurredAt"), filter.getEndDate()));
        }

        // Type filter (also accepts category as alias)
        String typeFilter = filter.getType();
        if (typeFilter == null) {
            typeFilter = filter.getCategory();
        }
        if (typeFilter != null) {
            predicates.add(cb.equal(cb.lower(root.get("type")), typeFilter.toLowerCase()));
        }

        if (filter.getStatus() != null) {
            predicates.add(cb.equal(cb.lower(root.get("status")), filter.getStatus().toLowerCase()));
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

        // Exact match on reference
        if (filter.getReference() != null && !filter.getReference().isBlank()) {
            predicates.add(cb.equal(cb.lower(root.get("reference")), filter.getReference().toLowerCase()));
        }

        // Priority filter
        if (filter.getPriority() != null) {
            predicates.add(cb.equal(root.get("priorityScore"), filter.getPriority()));
        }

        // Tags filter - match signals that have ANY of the provided tags
        if (filter.getTags() != null && !filter.getTags().isEmpty()) {
            Join<Signal, String> tagsJoin = root.join("tags");
            predicates.add(tagsJoin.in(filter.getTags()));
            // Use distinct to avoid duplicate results from the join
        }

        // BOLO match filter
        if (Boolean.TRUE.equals(filter.getHasBoloMatch())) {
            predicates.add(cb.isNotNull(root.get("primaryBoloId")));
        }

        // Bounding box filter: latitude BETWEEN south AND north, longitude BETWEEN west AND east
        if (filter.getBoundingBox() != null && filter.getBoundingBox().isComplete()) {
            var box = filter.getBoundingBox();
            predicates.add(cb.between(root.get("latitude"), box.getSouth(), box.getNorth()));
            predicates.add(cb.between(root.get("longitude"), box.getWest(), box.getEast()));
        }

        // Full-text search: each word must appear in at least one text field (AND between words)
        if (filter.getSearch() != null && !filter.getSearch().isBlank()) {
            String[] words = filter.getSearch().trim().toLowerCase().split("\\s+");
            List<Predicate> wordPredicates = new ArrayList<>();
            for (String word : words) {
                String pattern = "%" + word + "%";
                Predicate textMatch = cb.like(cb.lower(root.get("text")), pattern);
                Predicate rawTextMatch = cb.like(cb.lower(root.get("rawText")), pattern);
                Predicate addressMatch = cb.like(cb.lower(root.get("address")), pattern);
                Predicate referenceMatch = cb.like(cb.lower(root.get("reference")), pattern);
                Predicate typeMatch = cb.like(cb.lower(root.get("type")), pattern);
                wordPredicates.add(cb.or(textMatch, rawTextMatch, addressMatch, referenceMatch, typeMatch));
            }
            predicates.add(cb.and(wordPredicates.toArray(new Predicate[0])));
        }

        return predicates;
    }
}
