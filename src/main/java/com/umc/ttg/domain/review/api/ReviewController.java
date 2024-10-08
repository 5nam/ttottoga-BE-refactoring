package com.umc.ttg.domain.review.api;

import com.google.zxing.WriterException;
import com.umc.ttg.domain.review.application.ReviewCommandService;
import com.umc.ttg.domain.review.dto.ReviewRegisterRequestDTO;
import com.umc.ttg.domain.review.dto.ReviewRegisterResponseDTO;
import com.umc.ttg.global.common.BaseResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores/{storeId}/reviews")
public class ReviewController {

    private final ReviewCommandService reviewService;
//    private final AuthService authService;

    @PostMapping
    public BaseResponseDto<ReviewRegisterResponseDTO> registerReview(
            @PathVariable("storeId") Long storeId,
            @ModelAttribute @Valid ReviewRegisterRequestDTO reviewRegisterRequestDTO,
            HttpServletRequest request) throws IOException, WriterException {

        /**
         * FIXME: 로그인 구현 후 수정
         */

//        String memberName = authService.retrieveName(request);
        String memberName = "";

        return reviewService.save(storeId, reviewRegisterRequestDTO, memberName);
    }
}
