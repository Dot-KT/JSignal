package com.example.SignalApi.controller;

import com.example.SignalApi.dto.BoundingBoxDto;
import com.example.SignalApi.dto.SignalFilterDto;
import com.example.SignalApi.dto.SignalPageResponseDto;
import com.example.SignalApi.dto.SignalRequestDto;
import com.example.SignalApi.dto.SignalResponseDto;
import com.example.SignalApi.service.SignalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/signals")
@RequiredArgsConstructor
public class SignalController {

    private final SignalService signalService;

    @GetMapping("tests")
    public ResponseEntity<String> root() {
        return ResponseEntity.ok("Signal API is running");
    }


    @PostMapping
    public ResponseEntity<SignalResponseDto> createSignal(@RequestBody SignalRequestDto request) {
        SignalResponseDto response = signalService.createSignal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SignalResponseDto> getSignalById(@PathVariable String id) {
        return ResponseEntity.ok(signalService.getSignalById(id));
    }

    @GetMapping
    public ResponseEntity<SignalPageResponseDto> searchSignals(
            @RequestParam(required = false) Instant startDate,
            @RequestParam(required = false) Instant endDate,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Boolean hasPhotoAttached,
            @RequestParam(required = false) String communityId,
            @RequestParam(required = false) Boolean activeOnly,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String reference,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) Integer priority,
            @RequestParam(required = false) Boolean hasBoloMatch,
            @RequestParam(required = false) Boolean hasSimilarSignals,
            @RequestParam(required = false) Boolean hasPossibleDuplicates,
            @RequestParam(required = false) Double south,
            @RequestParam(required = false) Double north,
            @RequestParam(required = false) Double west,
            @RequestParam(required = false) Double east,
            @RequestParam(required = false) Instant cursorAt,
            @RequestParam(required = false) String cursorId,
            @RequestParam(defaultValue = "20") int limit) {

        SignalFilterDto filter = new SignalFilterDto();
        filter.setStartDate(startDate);
        filter.setEndDate(endDate);
        filter.setType(type);
        filter.setStatus(status);
        filter.setHasPhotoAttached(hasPhotoAttached);
        filter.setCommunityId(communityId);
        filter.setActiveOnly(activeOnly);
        filter.setSearch(search);
        filter.setReference(reference);
        filter.setCategory(category);
        filter.setTags(tags);
        filter.setPriority(priority);
        filter.setHasBoloMatch(hasBoloMatch);
        filter.setHasSimilarSignals(hasSimilarSignals);
        filter.setHasPossibleDuplicates(hasPossibleDuplicates);
        filter.setBoundingBox(new BoundingBoxDto(south, north, west, east));
        filter.setCursorAt(cursorAt);
        filter.setCursorId(cursorId);
        filter.setLimit(limit);

        return ResponseEntity.ok(signalService.searchSignals(filter));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SignalResponseDto> updateSignal(@PathVariable String id,
                                                          @RequestBody SignalRequestDto request) {
        return ResponseEntity.ok(signalService.updateSignal(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSignal(@PathVariable String id) {
        signalService.deleteSignal(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<SignalResponseDto> deactivateSignal(@PathVariable String id) {
        return ResponseEntity.ok(signalService.deactivateSignal(id));
    }
}
