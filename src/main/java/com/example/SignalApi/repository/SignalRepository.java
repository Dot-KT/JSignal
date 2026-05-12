package com.example.SignalApi.repository;

import com.example.SignalApi.entities.Signal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignalRepository extends JpaRepository<Signal, String>, SignalRepositoryCustom {
}
