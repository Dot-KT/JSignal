package com.example.SignalApi.service;

import com.example.SignalApi.dto.SignalFilterDto;
import com.example.SignalApi.dto.SignalPageResponseDto;
import com.example.SignalApi.dto.SignalRequestDto;
import com.example.SignalApi.dto.SignalResponseDto;

public interface SignalService {

    SignalResponseDto createSignal(SignalRequestDto request);

    SignalResponseDto getSignalById(String id);

    SignalPageResponseDto searchSignals(SignalFilterDto filter);

    SignalResponseDto updateSignal(String id, SignalRequestDto request);

    void deleteSignal(String id);

    SignalResponseDto deactivateSignal(String id);
}
