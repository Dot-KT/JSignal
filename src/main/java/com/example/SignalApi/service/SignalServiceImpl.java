package com.example.SignalApi.service;

import com.example.SignalApi.dto.SignalFilterDto;
import com.example.SignalApi.dto.SignalPageResponseDto;
import com.example.SignalApi.dto.SignalRequestDto;
import com.example.SignalApi.dto.SignalResponseDto;
import com.example.SignalApi.entities.Signal;
import com.example.SignalApi.exception.SignalNotFoundException;
import com.example.SignalApi.mapper.SignalMapper;
import com.example.SignalApi.repository.SignalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignalServiceImpl implements SignalService {

    private final SignalRepository signalRepository;
    private final SignalMapper signalMapper;

    @Override
    @Transactional
    public SignalResponseDto createSignal(SignalRequestDto request) {
        Signal signal = signalMapper.toEntity(request);
        Signal saved = signalRepository.save(signal);
        return signalMapper.toResponseDto(saved);
    }

    @Override
    public SignalResponseDto getSignalById(String id) {
        Signal signal = signalRepository.findById(id)
                .orElseThrow(() -> new SignalNotFoundException(id));
        return signalMapper.toResponseDto(signal);
    }

    @Override
    public SignalPageResponseDto searchSignals(SignalFilterDto filter) {
        List<Signal> results = signalRepository.findWithFilters(filter);

        boolean hasMore = results.size() > filter.getLimit();
        List<Signal> page = hasMore ? results.subList(0, filter.getLimit()) : results;

        List<SignalResponseDto> data = page.stream()
                .map(signalMapper::toResponseDto)
                .toList();

        SignalPageResponseDto response = new SignalPageResponseDto();
        response.setData(data);
        response.setHasMore(hasMore);

        if (!page.isEmpty()) {
            Signal last = page.getLast();
            response.setNextCursorId(last.getId());
            response.setNextCursorAt(last.getCreatedAt());
        }

        return response;
    }

    @Override
    @Transactional
    public SignalResponseDto updateSignal(String id, SignalRequestDto request) {
        Signal existing = signalRepository.findById(id)
                .orElseThrow(() -> new SignalNotFoundException(id));

        Signal updated = signalMapper.toEntity(request);
        updated.setId(existing.getId());
        updated.setCreatedAt(existing.getCreatedAt());

        Signal saved = signalRepository.save(updated);
        return signalMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public void deleteSignal(String id) {
        if (!signalRepository.existsById(id)) {
            throw new SignalNotFoundException(id);
        }
        signalRepository.deleteById(id);
    }

    @Override
    @Transactional
    public SignalResponseDto deactivateSignal(String id) {
        Signal signal = signalRepository.findById(id)
                .orElseThrow(() -> new SignalNotFoundException(id));
        signal.setIsActive(false);
        signal.setDeactivatedAt(Instant.now());
        Signal saved = signalRepository.save(signal);
        return signalMapper.toResponseDto(saved);
    }
}
