package com.example.SignalApi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SignalFilterDto {

    private Instant startDate;
    private Instant endDate;
    private String type;
    private String status;
    private Boolean hasPhotoAttached;
    private String communityId;
    private Boolean activeOnly;
    private String search;

    // Dedicated filters for accurate matching
    private String reference;
    private String category;
    private List<String> tags;
    private Integer priority;

    // Similarity filters
    private Boolean hasBoloMatch;
    private Boolean hasSimilarSignals;
    private Boolean hasPossibleDuplicates;

    // Cursor-based pagination
    private Instant cursorAt;
    private String cursorId;
    private int limit = 20;
}
