package com.umc.ttg.domain.store.application;

import com.umc.ttg.domain.store.dto.StoreRequestDto;
import com.umc.ttg.domain.store.dto.StoreResponseDto;
import com.umc.ttg.domain.store.entity.StoreEntity;
import com.umc.ttg.domain.store.exception.handler.StoreHandler;
import com.umc.ttg.domain.store.repository.StoreRepository;
import com.umc.ttg.global.common.BaseResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@Sql("/sql/store-service-test-data.sql")
@ActiveProfiles("test")
class StoreCommandServiceImplTest {

    @Autowired
    private StoreCommandService storeCommandService;
    @Autowired
    private StoreRepository storeRepository;

    @Test
    void 상점을_저장할_수_있다() throws IOException {
        // given
        MockMultipartFile mockMultipartFile = new MockMultipartFile("name", "originalName", "image/png", "ddd".getBytes());

        StoreRequestDto storeRequestDto = StoreRequestDto.builder()
                .name("name")
                .address("address")
                .reviewSpan(2)
                .serviceInfo("serviceInfo").sponInfo("sponInfo").placeInfo("placeInfo")
                .useInfo("useInfo").saleInfo("saleInfo")
                .storeImage(mockMultipartFile).title("title")
                .subTitle("subTitle").menu(1L).region(1L).build();

        // when
        BaseResponseDto<StoreResponseDto> storeResponseDtoBaseResponseDto = storeCommandService.saveStore(storeRequestDto);

        // then
        assertThat(storeResponseDtoBaseResponseDto.getResult().getStoreId()).isEqualTo(1L);
    }

    @Test
    void 존재하지_않는_메뉴를_설정할_경우_상점을_저장하지_않고_예외를_발생시킨다() throws IOException {
        // given
        MockMultipartFile mockMultipartFile = new MockMultipartFile("name", "originalName", "image/png", "ddd".getBytes());

        StoreRequestDto storeRequestDto = StoreRequestDto.builder()
                .name("name")
                .address("address")
                .reviewSpan(2)
                .serviceInfo("serviceInfo").sponInfo("sponInfo").placeInfo("placeInfo")
                .useInfo("useInfo").saleInfo("saleInfo")
                .storeImage(mockMultipartFile).title("title")
                .subTitle("subTitle").menu(2L).region(1L).build();

        // when
        // then
        assertThatThrownBy(() -> storeCommandService.saveStore(storeRequestDto))
                .isInstanceOf(StoreHandler.class)
                .hasMessageContaining("카테고리에 없는 메뉴입니다.");
    }

    @Test
    void 존재하지_않는_지역을_설정할_경우_상점을_저장하지_않고_예외를_발생시킨다() throws IOException {
        // given
        MockMultipartFile mockMultipartFile = new MockMultipartFile("name", "originalName", "image/png", "ddd".getBytes());

        StoreRequestDto storeRequestDto = StoreRequestDto.builder()
                .name("name")
                .address("address")
                .reviewSpan(2)
                .serviceInfo("serviceInfo").sponInfo("sponInfo").placeInfo("placeInfo")
                .useInfo("useInfo").saleInfo("saleInfo")
                .storeImage(mockMultipartFile).title("title")
                .subTitle("subTitle").menu(1L).region(2L).build();

        // when
        // then
        assertThatThrownBy(() -> storeCommandService.saveStore(storeRequestDto))
                .isInstanceOf(StoreHandler.class)
                .hasMessageContaining("카테고리에 없는 지역입니다.");
    }

    @Test
    void updateStore() throws IOException {
        // given
        MockMultipartFile mockMultipartFile = new MockMultipartFile("name", "originalName", "image/png", "ddd".getBytes());

        StoreRequestDto storeRequestDto = StoreRequestDto.builder()
                .name("change")
                .address("address")
                .reviewSpan(2)
                .serviceInfo("serviceInfo").sponInfo("sponInfo").placeInfo("placeInfo")
                .useInfo("useInfo").saleInfo("saleInfo")
                .storeImage(mockMultipartFile).title("title")
                .subTitle("subTitle").menu(1L).region(1L).build();

        BaseResponseDto<StoreResponseDto> responseDto = storeCommandService.saveStore(storeRequestDto);

        // when
        BaseResponseDto<StoreResponseDto> storeResponseDtoBaseResponseDto = storeCommandService.updateStore(storeRequestDto, responseDto.getResult().getStoreId());
        Optional<StoreEntity> store = storeRepository.findById(responseDto.getResult().getStoreId());

        // then
        assertThat(store.get().getName()).isEqualTo("change");
    }

    @Test
    void insertHeart() {
    }

    @Test
    void deleteHeart() {
    }
}