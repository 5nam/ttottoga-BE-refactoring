package com.umc.ttg.domain.coupon.api;

import com.google.zxing.WriterException;
import com.umc.ttg.domain.coupon.application.CouponService;
import com.umc.ttg.domain.coupon.dto.CouponResponseDto;
import com.umc.ttg.domain.member.application.MemberService;
import com.umc.ttg.global.common.BaseResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;
    private final MemberService memberService;

    @GetMapping
    public BaseResponseDto<List<CouponResponseDto>> allCouponsGet(HttpServletRequest request) {
        Long memberId = memberService.permitMemberOnly(request);
        return couponService.getAllCoupons(memberId);
    }

    @GetMapping("/{coupon-id}")
    public BaseResponseDto<CouponResponseDto> CouponDetailsGet(@PathVariable("coupon-id") Long couponId, HttpServletRequest request) throws IOException, WriterException {
        Long memberId = memberService.permitMemberOnly(request);
        return couponService.getCouponDetails(couponId, memberId);
    }

    @PutMapping("/{coupon-id}/check")
    public BaseResponseDto<String> CouponUse(@PathVariable("coupon-id") Long couponId, HttpServletRequest request) throws IOException, WriterException {
        Long memberId = memberService.permitMemberOnly(request);
        return couponService.useCoupon(couponId, memberId);
    }

}
