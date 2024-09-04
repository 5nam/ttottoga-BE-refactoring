package com.umc.ttg.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRegisterRequestDTO {

    @NotBlank
    private String reviewLink;
}
