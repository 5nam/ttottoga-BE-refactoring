package com.umc.ttg.domain.store.application;

import com.umc.ttg.domain.store.dto.StoreFindResponseDto;
import com.umc.ttg.domain.store.dto.StoreResultResponseDto;
import com.umc.ttg.domain.store.exception.handler.StoreHandler;
import com.umc.ttg.global.common.BaseResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@Sql("/sql/store-query-service-test-data.sql")
@ActiveProfiles("test")
class StoreQueryServiceImplTest {

    @Autowired
    StoreQueryService storeQueryService;

    @Test
    @DisplayName("store_id와_memberName_으로_스토어를_조회할_수_있다")
    void getById() {
        // given
        Long storeId = 1L;
        String memberName = "Kakao_userId";
        // when
        BaseResponseDto<StoreFindResponseDto> response = storeQueryService.getById(storeId, memberName);
        // then
        assertThat(response.getResult().getName()).isEqualTo("testStore");
    }

    @Test
    @DisplayName("잘못된_store_id로_스토어_조회하면_예외를_발생시킨다")
    void getById_store_exception() {
        // given
        Long storeId = 100L;
        String memberName = "Kakao_userId";
        // when
        // then
        assertThatThrownBy(() -> storeQueryService.getById(storeId, memberName))
                .isInstanceOf(StoreHandler.class)
                .hasMessageContaining("존재하지 않는 상점입니다.");
    }

    @Test
    @DisplayName("잘못된_memberName으로_스토어_조회하면_예외를_발생시킨다")
    void getById_member_exception() {
        // given
        Long storeId = 1L;
        String memberName = "Kakao_userId11111";
        // when
        // then
        assertThatThrownBy(() -> storeQueryService.getById(storeId, memberName))
                .isInstanceOf(StoreHandler.class)
                .hasMessageContaining("사용자가 없습니다.");
    }

    @Test
    @DisplayName("memberName이_null일_때도_스토어_조회할_수_있다")
    void getById_member_null_ok() {
        // given
        Long storeId = 1L;
        String memberName = null;
        // when
        BaseResponseDto<StoreFindResponseDto> response = storeQueryService.getById(storeId, memberName);
        // then
        assertThat(response.getResult().getName()).isEqualTo("testStore");
    }

    @Test
    @DisplayName("regionId로 상점 게시글들을 한번에 size개씩 불러올 수 있다")
    void getByRegion() {
        // given
        Long regionId = 1L;
        String memberName = null;
        int size = 5;
        int page = 0;

        // when
        BaseResponseDto<Page<StoreResultResponseDto>> response = storeQueryService.getByRegion(regionId, page, size, memberName);

        // then
        assertThat(response.getResult().getTotalPages()).isEqualTo(3);
        assertThat(response.getResult().getTotalElements()).isEqualTo(11);
        assertThat(response.getResult().isEmpty()).isFalse();

    }

    /**
     * 궁금한 점
     * - Store 관련에서 멤버가 알맞은 멤버인지 체크하는 것인데, 이건 StoreHandler 에서 처리해야 하는지, MemberHandler 에서 처리해야 하는지 헷갈림..
     * - 애초에 Handler 를 도메인별로 나누는 것이 맞는지..?
     */
    @Test
    @DisplayName("잘못된 memberName 을 입력하면 예외를 발생시킨다.")
    void getByRegion_wrong_memberName() {
        // given
        Long regionId = 1L;
        String memberName = "12345";
        int size = 5;
        int page = 0;

        // when
        // then
        assertThatThrownBy(() -> storeQueryService.getByRegion(regionId, page, size, memberName))
                .isInstanceOf(StoreHandler.class)
                .hasMessageContaining("사용자가 없습니다.");
    }

    @Test
    @DisplayName("잘못된 regionId를 입력하면 예외를 발생시킨다.")
    void getByRegion_wrong_regionId() {
        // given
        Long regionId = 100L;
        String memberName = null;
        int size = 5;
        int page = 0;

        // when
        // then
        assertThatThrownBy(() -> storeQueryService.getByRegion(regionId, page, size, memberName))
                .isInstanceOf(StoreHandler.class)
                .hasMessageContaining("카테고리에 없는 지역입니다.");
    }

    @Test
    @DisplayName("menuId로 상점 게시글들을 한번에 size개씩 불러올 수 있다")
    void getByMenu() {
        // given
        Long menuId = 1L;
        String memberName = null;
        int size = 5;
        int page = 0;

        // when
        BaseResponseDto<Page<StoreResultResponseDto>> response = storeQueryService.getByMenu(menuId, page, size, memberName);

        // then
        assertThat(response.getResult().getTotalPages()).isEqualTo(3);
        assertThat(response.getResult().getTotalElements()).isEqualTo(11);
        assertThat(response.getResult().isEmpty()).isFalse();
    }

    /**
     * 궁금한 점
     * - Store 관련에서 멤버가 알맞은 멤버인지 체크하는 것인데, 이건 StoreHandler 에서 처리해야 하는지, MemberHandler 에서 처리해야 하는지 헷갈림..
     * - 애초에 Handler 를 도메인별로 나누는 것이 맞는지..?
     */
    @Test
    @DisplayName("잘못된 memberName 을 입력하면 예외를 발생시킨다.")
    void getByMenu_wrong_memberName() {
        // given
        Long menuId = 1L;
        String memberName = "12345";
        int size = 5;
        int page = 0;

        // when
        // then
        assertThatThrownBy(() -> storeQueryService.getByMenu(menuId, page, size, memberName))
                .isInstanceOf(StoreHandler.class)
                .hasMessageContaining("사용자가 없습니다.");
    }

    @Test
    @DisplayName("잘못된 menuId를 입력하면 예외를 발생시킨다.")
    void getByRegion_wrong_menuId() {
        // given
        Long menuId = 100L;
        String memberName = null;
        int size = 5;
        int page = 0;

        // when
        // then
        assertThatThrownBy(() -> storeQueryService.getByMenu(menuId, page, size, memberName))
                .isInstanceOf(StoreHandler.class)
                .hasMessageContaining("카테고리에 없는 메뉴입니다.");
    }

    @Test
    void search() {
    }

    @Test
    void findHeartStores() {
    }

    @Test
    void findHome() {
    }
}