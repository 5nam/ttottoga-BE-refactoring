package com.umc.ttg.domain.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StoreRequestDto {

    @NotNull @NotEmpty @NotBlank
    private String title;

    @NotNull @NotEmpty @NotBlank
    private String name;

    @NotNull @NotEmpty @NotBlank
    private String subTitle;

    @NotNull
    private Long region;

    @NotNull
    private Long menu;

    @NotNull
    private MultipartFile storeImage;

    private String serviceInfo;

    @NotNull
    private Integer reviewSpan;

    private String useInfo;

    private String saleInfo;

    private String sponInfo;

    private String placeInfo;

    @NotNull @NotEmpty @NotBlank
    private String address;

}
