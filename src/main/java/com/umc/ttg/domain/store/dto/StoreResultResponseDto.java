package com.umc.ttg.domain.store.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreResultResponseDto {

    private Long storeId;
    private String storeTitle;
    private String storeImage;
    private String serviceInfo;
    private Integer reviewCount;
    private Boolean heartStore;

}
