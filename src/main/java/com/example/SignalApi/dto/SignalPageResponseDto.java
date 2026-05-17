package com.example.SignalApi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignalPageResponseDto {

    private long totalCount;
    private List<SignalResponseDto> data;
    private String nextCursorId;
    private Instant nextCursorAt;
    private boolean hasMore;
}
