package com.umc.ttg.domain.coupon.repository;

import com.umc.ttg.domain.coupon.entity.CouponEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest(showSql = true)
@Sql("/sql/coupon-repository-test-data.sql")
class CouponRepositoryTest {

    @Autowired
    private CouponRepository couponRepository;

    @Test
    void findAllByMemberId_로_쿠폰_데이터를_찾아올_수_있다() {
        //given

        //when
        List<CouponEntity> result = couponRepository.findAllByMemberId(1L);

        //then
        assertThat(result.size() > 0).isTrue();
    }

    @Test
    void findAllByMemberId_로_쿠폰_데이터를_찾아올_때_값이_없으면_빈_리스트를_반환한다() {
        //given

        //when
        List<CouponEntity> result = couponRepository.findAllByMemberId(2L);

        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void findByIdAndMemberId_로_쿠폰_데이터를_찾아올_수_있다() {
        //given
        //when
        Optional<CouponEntity> result = couponRepository.findByIdAndMemberId(1L, 1L);
        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByIdAndMemberId_로_쿠폰_데이터를_찾아올_때_결과가_없으면_Optional_emtpy_를_내려준다() {
        //given
        //when
        Optional<CouponEntity> result = couponRepository.findByIdAndMemberId(2L, 1L);
        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void findAllByMemberIdAndStoreId_로_쿠폰_데이터를_찾아올_수_있다() {
        //given
        //when
        List<CouponEntity> result = couponRepository.findAllByMemberIdAndStoreId(1L, 1L);
        //then
        assertThat(result.size() > 0).isTrue();
    }

    @Test
    void findAllByMemberIdAndStoreId_는_데이터가_없으면_빈_리스트를_내려준다() {
        //given
        //when
        List<CouponEntity> result = couponRepository.findAllByMemberIdAndStoreId(2L, 2L);
        //then
        assertThat(result.isEmpty()).isTrue();
    }

}