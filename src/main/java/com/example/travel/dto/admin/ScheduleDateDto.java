package com.example.travel.dto.admin;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ScheduleDateDto {
    private int year;
    private int month;
    private int startDayOfWeek;
    private int lastDay;
    private LocalDateTime productStartDate;
    private LocalDateTime productEndDate;
    private int maxDepartureCount;

    @Builder
    public ScheduleDateDto(int year, int month, LocalDateTime productStartDate, LocalDateTime productEndDate, int maxDepartureCount){
        this.year = year;
        this.month = month;
        this.startDayOfWeek = LocalDate.of(year, month, 1).getDayOfWeek().getValue();
        this.lastDay = LocalDate.of(year, month, 1).lengthOfMonth();
        this.productStartDate = productStartDate;
        this.productEndDate = productEndDate;
        this.maxDepartureCount = maxDepartureCount;
    }

}
