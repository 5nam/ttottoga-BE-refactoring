package com.umc.ttg.domain.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeartStoreResponseDto {

    Long id;
    Long memberId;
    Long storeId;
}
