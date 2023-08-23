package com.lampirg;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.util.Map;

@Data
@AllArgsConstructor
public class Result {

    private Map<String, Duration> min;
    private double div;
}
