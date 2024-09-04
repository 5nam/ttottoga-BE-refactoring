package com.umc.ttg.domain.review.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReviewStatus {
    SUBSCRIPTION("SUBSCRIPTION"),
    SCREENING("SCREENING"),
    SUCCESS("SUCCESS"),
    FAIL("FAIL");

    private final String status;
}

