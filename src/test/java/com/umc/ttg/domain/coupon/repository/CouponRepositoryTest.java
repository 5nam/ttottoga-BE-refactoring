package com.umc.ttg.domain.coupon.repository;

import com.umc.ttg.domain.coupon.entity.CouponEntity;
import com.umc.ttg.domain.member.entity.MemberEntity;
import com.umc.ttg.domain.member.repository.MemberRepository;
import com.umc.ttg.domain.store.dto.StoreRequestDto;
import com.umc.ttg.domain.store.entity.MenuEntity;
import com.umc.ttg.domain.store.entity.RegionEntity;
import com.umc.ttg.domain.store.entity.StoreEntity;
import com.umc.ttg.domain.store.repository.MenuRepository;
import com.umc.ttg.domain.store.repository.RegionRepository;
import com.umc.ttg.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest(showSql = true)
class CouponRepositoryTest {

    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void CouponRepository_가_제대로_연결되었다() {
        //given
        MemberEntity memberEntity = MemberEntity.builder()
                .userId("test")
                .nickname("nickname")
                .profileImage("profileImage")
                .type("type")
                .build();

        memberRepository.save(memberEntity);

        StoreRequestDto storeRequestDto = StoreRequestDto.builder()
                .title("title")
                .subTitle("subTitle")
                .useInfo("useInfo")
                .saleInfo("saleInfo")
                .placeInfo("placeInfo")
                .sponInfo("sponInfo")
                .serviceInfo("serviceInfo")
                .reviewSpan(5)
                .address("address")
                .name("name").build();

        MenuEntity menu = MenuEntity.builder().name("menuName").build();
        menuRepository.save(menu);
        RegionEntity region = RegionEntity.builder().name("regionName").build();
        regionRepository.save(region);

        StoreEntity storeEntity = StoreEntity.builder()
                .storeRequestDto(storeRequestDto)
                .storeImage("storeImage")
                .menu(menu)
                .region(region)
                .build();
        storeRepository.save(storeEntity);

        CouponEntity couponEntity = CouponEntity.builder()
                .name("name")
                .content("content")
                .qrCode("qrCode")
                .imageUrl("imageUrl")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now())
                .statusYn('y')
                .member(memberEntity)
                .store(storeEntity).build();

        //when
        CouponEntity result = couponRepository.save(couponEntity);

        //then
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void findAllByMemberId_로_쿠폰_데이터를_찾아올_수_있다() {
        //given

        //when
        //then
    }

    @Test
    void findByStoreId() {
    }

    @Test
    void findByIdAndMemberId() {
    }

    @Test
    void findByMemberIdAndStoreId() {
    }

    @Test
    void findAllByMemberIdAndStoreId() {
    }
}