package com.example.SignalApi.repository;

import com.example.SignalApi.entities.Signal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface SignalRepository extends JpaRepository<Signal, String>, SignalRepositoryCustom {

    @Query("SELECT COUNT(s) FROM Signal s WHERE s.type = :type AND s.communityId = :communityId " +
            "AND s.id <> :excludeId AND s.isActive = true " +
            "AND s.createdAt BETWEEN :start AND :end")
    long countSimilarSignals(@Param("type") String type,
                             @Param("communityId") String communityId,
                             @Param("excludeId") String excludeId,
                             @Param("start") Instant start,
                             @Param("end") Instant end);

    @Query("SELECT COUNT(s) FROM Signal s WHERE s.type = :type AND s.communityId = :communityId " +
            "AND s.id <> :excludeId AND s.isActive = true " +
            "AND s.occurredAt BETWEEN :start AND :end")
    long countPossibleDuplicates(@Param("type") String type,
                                 @Param("communityId") String communityId,
                                 @Param("excludeId") String excludeId,
                                 @Param("start") Instant start,
                                 @Param("end") Instant end);
}
