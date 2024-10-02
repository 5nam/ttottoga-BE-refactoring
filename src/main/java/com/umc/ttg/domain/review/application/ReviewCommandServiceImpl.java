package com.umc.ttg.domain.review.application;

import com.google.zxing.WriterException;
import com.umc.ttg.domain.coupon.entity.CouponEntity;
import com.umc.ttg.domain.coupon.repository.CouponRepository;
import com.umc.ttg.domain.coupon.utils.QrCodeGenerator;
import com.umc.ttg.domain.member.entity.MemberEntity;
import com.umc.ttg.domain.member.exception.handler.MemberHandler;
import com.umc.ttg.domain.member.repository.MemberRepository;
import com.umc.ttg.domain.review.dto.ReviewRegisterRequestDTO;
import com.umc.ttg.domain.review.dto.ReviewRegisterResponseDTO;
import com.umc.ttg.domain.review.entity.ReviewEntity;
import com.umc.ttg.domain.review.entity.ReviewStatus;
import com.umc.ttg.domain.review.exception.handler.ReviewHandler;
import com.umc.ttg.domain.review.repository.ReviewRepository;
import com.umc.ttg.domain.store.entity.StoreEntity;
import com.umc.ttg.domain.store.exception.handler.StoreHandler;
import com.umc.ttg.domain.store.repository.StoreRepository;
import com.umc.ttg.global.common.BaseResponseDto;
import com.umc.ttg.global.common.ResponseCode;
import com.umc.ttg.global.util.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewCommandServiceImpl implements ReviewCommandService {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final FileService awsS3FileService;

    @Override
    @Transactional
    public BaseResponseDto<ReviewRegisterResponseDTO> save(Long storeId, ReviewRegisterRequestDTO reviewRegisterRequestDTO, String memberName) throws IOException, WriterException {

        StoreEntity storeEntity = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreHandler(ResponseCode.STORE_NOT_FOUND));

        MemberEntity memberEntity = memberRepository.findByName(memberName)
                .orElseThrow(() -> new MemberHandler(ResponseCode.MEMBER_NOT_FOUND));

        // 리뷰가 이미 있으면 예외 처리
        Optional<ReviewEntity> foundReview = reviewRepository.findByStoreIdAndMemberId(storeId, memberEntity.getId());
        if (foundReview.isPresent()){
            throw new ReviewHandler(ResponseCode.REVIEW_ALREADY_FOUND);
        }

        ReviewEntity reviewEntity = new ReviewEntity(storeEntity, memberEntity, reviewRegisterRequestDTO);
        reviewEntity.setStatus(ReviewStatus.SUCCESS);
        reviewEntity.setApplyDate(LocalDate.now());

        // DB에 저장
        ReviewEntity savedReviewEntity = reviewRepository.save(reviewEntity);

        ReviewRegisterResponseDTO reviewRegisterResponseDTO = new ReviewRegisterResponseDTO(savedReviewEntity.getId());

        // 리뷰 등록 시 쿠폰 생성
        MultipartFile qrCode = QrCodeGenerator.generateQrCode(storeEntity);
        String s3ImageLink = getS3ImageLink(qrCode);
        couponRepository.save(CouponEntity.of(storeEntity, s3ImageLink, LocalDate.now(), LocalDate.now().plusMonths(1), memberEntity));

        storeEntity.updateReviewCount(); // store reviewCount +1
        memberEntity.updateBenefitCount(); // member benefitCount +1

        return BaseResponseDto.onSuccess(reviewRegisterResponseDTO, ResponseCode.OK);
    }

    private String getS3ImageLink(MultipartFile multipartFile) throws IOException {

        return awsS3FileService.upload(multipartFile, "qrImage");

    }

}
