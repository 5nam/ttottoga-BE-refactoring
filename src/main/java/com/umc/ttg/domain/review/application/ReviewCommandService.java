package com.umc.ttg.domain.review.application;

import com.google.zxing.WriterException;
import com.umc.ttg.domain.review.dto.ReviewRegisterRequestDTO;
import com.umc.ttg.domain.review.dto.ReviewRegisterResponseDTO;
import com.umc.ttg.global.common.BaseResponseDto;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public interface ReviewCommandService {
    BaseResponseDto<ReviewRegisterResponseDTO> save(Long storeId, ReviewRegisterRequestDTO reviewRegisterRequestDTO, String memberName) throws IOException, WriterException;
}
