package com.umc.ttg.domain.review.application;

import com.google.zxing.WriterException;
import com.umc.ttg.domain.coupon.entity.Coupon;
import com.umc.ttg.domain.coupon.repository.CouponRepository;
import com.umc.ttg.domain.coupon.utils.QrCodeGenerator;
import com.umc.ttg.domain.member.entity.Member;
import com.umc.ttg.domain.member.exception.handler.MemberHandler;
import com.umc.ttg.domain.member.repository.MemberRepository;
import com.umc.ttg.domain.review.dto.ReviewRegisterRequestDTO;
import com.umc.ttg.domain.review.dto.ReviewRegisterResponseDTO;
import com.umc.ttg.domain.review.entity.Review;
import com.umc.ttg.domain.review.entity.ReviewStatus;
import com.umc.ttg.domain.review.exception.handler.ReviewHandler;
import com.umc.ttg.domain.review.repository.ReviewRepository;
import com.umc.ttg.domain.store.entity.Store;
import com.umc.ttg.domain.store.exception.handler.StoreHandler;
import com.umc.ttg.domain.store.repository.StoreRepository;
import com.umc.ttg.global.common.AwsS3;
import com.umc.ttg.global.common.BaseResponseDto;
import com.umc.ttg.global.common.ResponseCode;
import com.umc.ttg.global.util.AwsS3Service;
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
    private final AwsS3Service awsS3Service;

    @Override
    @Transactional
    public BaseResponseDto<ReviewRegisterResponseDTO> save(Long storeId, ReviewRegisterRequestDTO reviewRegisterRequestDTO, String memberName) throws IOException, WriterException {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreHandler(ResponseCode.STORE_NOT_FOUND));

        Member member = memberRepository.findByName(memberName)
                .orElseThrow(() -> new MemberHandler(ResponseCode.MEMBER_NOT_FOUND));

        // 리뷰가 이미 있으면 예외 처리
        Optional<Review> foundReview = reviewRepository.findByStoreIdAndMemberId(storeId, member.getId());
        if (foundReview.isPresent()){
            throw new ReviewHandler(ResponseCode.REVIEW_ALREADY_FOUND);
        }

        Review review = new Review(store, member, reviewRegisterRequestDTO);
        review.setStatus(ReviewStatus.SUCCESS);
        review.setApplyDate(LocalDate.now());

        // DB에 저장
        Review savedReview = reviewRepository.save(review);

        ReviewRegisterResponseDTO reviewRegisterResponseDTO = new ReviewRegisterResponseDTO(savedReview.getId());

        // 리뷰 등록 시 쿠폰 생성
        MultipartFile qrCode = QrCodeGenerator.generateQrCode(store);
        String s3ImageLink = getS3ImageLink(qrCode);
        couponRepository.save(Coupon.of(store, s3ImageLink, LocalDate.now(), LocalDate.now().plusMonths(1), member));

        store.updateReviewCount(); // store reviewCount +1
        member.updateBenefitCount(); // member benefitCount +1

        return BaseResponseDto.onSuccess(reviewRegisterResponseDTO, ResponseCode.OK);
    }

    private String getS3ImageLink(MultipartFile multipartFile) throws IOException {
        AwsS3 storeImage = awsS3Service.upload(multipartFile, "qrImage");
        return storeImage.getPath();
    }

}
