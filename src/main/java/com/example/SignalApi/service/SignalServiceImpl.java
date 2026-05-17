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

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignalServiceImpl implements SignalService {

    private static final Duration SIMILAR_SIGNAL_WINDOW = Duration.ofDays(30);
    private static final Duration DUPLICATE_SIGNAL_WINDOW = Duration.ofHours(2);

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
        SignalResponseDto dto = signalMapper.toResponseDto(signal);
        enrichWithSimilarityData(signal, dto);
        return dto;
    }

    @Override
    public SignalPageResponseDto searchSignals(SignalFilterDto filter) {
        List<Signal> results = signalRepository.findWithFilters(filter);
        long totalCount = signalRepository.countWithFilters(filter);

        boolean hasMore = results.size() > filter.getLimit();
        List<Signal> page = hasMore ? results.subList(0, filter.getLimit()) : results;

        List<SignalResponseDto> data = page.stream()
                .map(signal -> {
                    SignalResponseDto dto = signalMapper.toResponseDto(signal);
                    enrichWithSimilarityData(signal, dto);
                    return dto;
                })
                .toList();

        // Apply post-query similarity filters if requested
        List<SignalResponseDto> filtered = data;
        if (Boolean.TRUE.equals(filter.getHasSimilarSignals())) {
            filtered = filtered.stream()
                    .filter(dto -> dto.getSimilarSignalCount() != null && dto.getSimilarSignalCount() > 0)
                    .toList();
        }
        if (Boolean.TRUE.equals(filter.getHasPossibleDuplicates())) {
            filtered = filtered.stream()
                    .filter(dto -> dto.getPossibleDuplicateCount() != null && dto.getPossibleDuplicateCount() > 0)
                    .toList();
        }

        SignalPageResponseDto response = new SignalPageResponseDto();
        response.setTotalCount(totalCount);
        response.setData(filtered);
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

    private void enrichWithSimilarityData(Signal signal, SignalResponseDto dto) {
        // BOLO match: signal has a linked BOLO
        dto.setPossibleBoloMatch(signal.getPrimaryBoloId() != null && !signal.getPrimaryBoloId().isBlank());

        // Similar signals: same type + community within ±30 days
        if (signal.getCreatedAt() != null) {
            Instant start = signal.getCreatedAt().minus(SIMILAR_SIGNAL_WINDOW);
            Instant end = signal.getCreatedAt().plus(SIMILAR_SIGNAL_WINDOW);
            long similarCount = signalRepository.countSimilarSignals(
                    signal.getType(), signal.getCommunityId(), signal.getId(), start, end);
            dto.setSimilarSignalCount(similarCount);
        } else {
            dto.setSimilarSignalCount(0L);
        }

        // Possible duplicates: same type + community within ±2 hours of occurredAt
        if (signal.getOccurredAt() != null) {
            Instant start = signal.getOccurredAt().minus(DUPLICATE_SIGNAL_WINDOW);
            Instant end = signal.getOccurredAt().plus(DUPLICATE_SIGNAL_WINDOW);
            long duplicateCount = signalRepository.countPossibleDuplicates(
                    signal.getType(), signal.getCommunityId(), signal.getId(), start, end);
            dto.setPossibleDuplicateCount(duplicateCount);
        } else {
            dto.setPossibleDuplicateCount(0L);
        }
    }
}
