package com.example.SignalApi.repository;

import com.example.SignalApi.dto.SignalFilterDto;
import com.example.SignalApi.entities.Signal;

import java.util.List;

public interface SignalRepositoryCustom {

    List<Signal> findWithFilters(SignalFilterDto filter);

    long countWithFilters(SignalFilterDto filter);
}
