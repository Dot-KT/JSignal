package com.example.SignalApi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoundingBoxDto {

    private Double south;
    private Double north;
    private Double west;
    private Double east;

    public boolean isComplete() {
        return south != null && north != null && west != null && east != null;
    }
}
