package com.umc.ttg.domain.coupon.application;

import com.google.zxing.WriterException;
import com.umc.ttg.domain.coupon.dto.CouponResponseDto;
import com.umc.ttg.domain.coupon.dto.converter.CouponConverter;
import com.umc.ttg.domain.coupon.entity.Coupon;
import com.umc.ttg.domain.coupon.exception.handler.CouponHandler;
import com.umc.ttg.domain.coupon.repository.CouponRepository;
import com.umc.ttg.domain.member.entity.Member;
import com.umc.ttg.domain.member.repository.MemberRepository;
import com.umc.ttg.global.common.BaseResponseDto;
import com.umc.ttg.global.common.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;

    @Override
    public BaseResponseDto<List<CouponResponseDto>> getAllCoupons(Long memberId) {
        List<CouponResponseDto> couponResponseDtos = couponRepository
                .findAllByMemberId(memberId)
                .stream().map(CouponResponseDto::of)
                .collect(Collectors.toList());
        return BaseResponseDto.onSuccess(couponResponseDtos, ResponseCode.OK);
    }
    @Override
    public BaseResponseDto<CouponResponseDto> getCouponDetails(Long couponId, Long memberId) throws IOException, WriterException {
        Coupon coupon = getCoupon(couponId, memberId);

        return BaseResponseDto.onSuccess(CouponResponseDto.of(coupon), ResponseCode.OK);
    }

    @Override
    @Transactional
    public BaseResponseDto<String> useCoupon(Long couponId, Long memberId) {
        Coupon coupon = getCoupon(couponId, memberId);

        // 직원 확인
        if (coupon.getStatusYn().equals('Y')) {
            coupon.updateStatus('N');
            getMember(memberId).updateBenefitCount();
        }

        return BaseResponseDto.onSuccess(CouponConverter.convertToCouponUsage(coupon), ResponseCode.OK);
    }

    private Member getMember(Long memberId) {
        Member foundMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new CouponHandler(ResponseCode.MEMBER_NOT_FOUND));
        return foundMember;
    }

    private Coupon getCoupon(Long couponId, Long memberId) {
        Coupon foundCoupon = couponRepository.findByIdAndMemberId(couponId, memberId)
                .orElseThrow(() -> new CouponHandler(ResponseCode.COUPON_NOT_FOUND));
        return foundCoupon;
    }

}
