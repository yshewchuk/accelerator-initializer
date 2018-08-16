package com.scotiabank.accelerator.initializer.controller;

import org.junit.rules.Stopwatch;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Creates a result matcher based on stopwatch time
 * This only has value for ensuring that the actual response time of the application is low
 * because it allows separation of timing for getting the result and verifying the result
 */
public class ExecutionTimeVerifier {

    public static ResultMatcher executionTimeLessThan(Stopwatch stopwatch, long expectedTimeMs) {
        return mvcResult -> {
            long time = stopwatch.runtime(TimeUnit.MILLISECONDS);
            System.out.println("Execution time: " + time + "ms");
            assertThat(time).isLessThanOrEqualTo(expectedTimeMs);
        };
    }

}
