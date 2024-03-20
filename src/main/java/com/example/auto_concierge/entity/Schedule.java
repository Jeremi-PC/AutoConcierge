package com.example.auto_concierge.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.TimeZone;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    private DayOfWeek dayOfWeek;
    private TimeZone openTime;
    private TimeZone closeTime;
}
