package com.umc.ttg.domain.store.application;

import com.umc.ttg.domain.member.entity.HeartStoreEntity;
import com.umc.ttg.domain.member.exception.handler.MemberHandler;
import com.umc.ttg.domain.member.repository.HeartStoreRepository;
import com.umc.ttg.domain.store.dto.HeartStoreResponseDto;
import com.umc.ttg.domain.store.dto.StoreRequestDto;
import com.umc.ttg.domain.store.dto.StoreResponseDto;
import com.umc.ttg.domain.store.entity.StoreEntity;
import com.umc.ttg.domain.store.exception.handler.StoreHandler;
import com.umc.ttg.domain.store.repository.StoreRepository;
import com.umc.ttg.global.common.BaseResponseDto;
import com.umc.ttg.global.util.file.LocalFileService;
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
    @Autowired
    private HeartStoreRepository heartStoreRepository;
    @Autowired
    private LocalFileService localFileService;

    /**
     * FIXME: 상점 저장, 수정마다 이미지 저장되는 것 한번에 삭제할 수 있도록 변경
     * @throws IOException
     */
//    @AfterEach
//    void deleteAllFiles() {
//
//    }

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
        BaseResponseDto<StoreResponseDto> storeResponseDtoBaseResponseDto = storeCommandService.create(storeRequestDto);

        // then
        assertThat(storeResponseDtoBaseResponseDto.getResult().getStoreId()).isEqualTo(2L);

        // 테스트 파일 삭제
        localFileService.remove(storeRepository.findById(storeResponseDtoBaseResponseDto.getResult().getStoreId()).get().getImage());
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
        assertThatThrownBy(() -> storeCommandService.create(storeRequestDto))
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
        assertThatThrownBy(() -> storeCommandService.create(storeRequestDto))
                .isInstanceOf(StoreHandler.class)
                .hasMessageContaining("카테고리에 없는 지역입니다.");
    }

    @Test
    void 상점_정보를_수정할_수_있다() throws IOException {
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

        // when
        BaseResponseDto<StoreResponseDto> storeResponseDtoBaseResponseDto = storeCommandService.update(storeRequestDto, 1L);
        Optional<StoreEntity> store = storeRepository.findById(1L);

        // then
        assertThat(store.get().getName()).isEqualTo("change");

        localFileService.remove(store.get().getImage());
    }

    @Test
    void 상점_정보_수정_시에_잘못된_상점_아이디를_주면_예외를_발생시킨다() throws IOException {
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

        // when
        // then
        assertThatThrownBy(() -> storeCommandService.update(storeRequestDto, 100L))
                .isInstanceOf(StoreHandler.class)
                .hasMessageContaining("존재하지 않는 상점입니다.");
    }

    @Test
    void 상점_정보_수정_시에_잘못된_메뉴_아이디를_주면_예외를_발생시킨다() throws IOException {
        // given
        MockMultipartFile mockMultipartFile = new MockMultipartFile("name", "originalName", "image/png", "ddd".getBytes());

        StoreRequestDto storeRequestDto = StoreRequestDto.builder()
                .name("change")
                .address("address")
                .reviewSpan(2)
                .serviceInfo("serviceInfo").sponInfo("sponInfo").placeInfo("placeInfo")
                .useInfo("useInfo").saleInfo("saleInfo")
                .storeImage(mockMultipartFile).title("title")
                .subTitle("subTitle").menu(2L).region(1L).build();

        // when
        // then
        assertThatThrownBy(() -> storeCommandService.update(storeRequestDto, 1L))
                .isInstanceOf(StoreHandler.class)
                .hasMessageContaining("카테고리에 없는 메뉴입니다.");
    }

    @Test
    void 상점_정보_수정_시에_잘못된_지역_아이디를_주면_예외를_발생시킨다() throws IOException {
        // given
        MockMultipartFile mockMultipartFile = new MockMultipartFile("name", "originalName", "image/png", "ddd".getBytes());

        StoreRequestDto storeRequestDto = StoreRequestDto.builder()
                .name("change")
                .address("address")
                .reviewSpan(2)
                .serviceInfo("serviceInfo").sponInfo("sponInfo").placeInfo("placeInfo")
                .useInfo("useInfo").saleInfo("saleInfo")
                .storeImage(mockMultipartFile).title("title")
                .subTitle("subTitle").menu(1L).region(2L).build();

        // when
        // then
        assertThatThrownBy(() -> storeCommandService.update(storeRequestDto, 1L))
                .isInstanceOf(StoreHandler.class)
                .hasMessageContaining("카테고리에 없는 지역입니다.");
    }

    @Test
    void 관심_상점을_등록할_수_있다() {
        // given
        Long storeId = 1L;
        String memberName = "Kakao_userId";

        // when
        BaseResponseDto<HeartStoreResponseDto> heartStoreResponseDtoBaseResponseDto = storeCommandService.insertHeart(storeId, memberName);
        Optional<HeartStoreEntity> heartStore = heartStoreRepository.findById(heartStoreResponseDtoBaseResponseDto.getResult().getId());

        // then
        assertThat(heartStore.isPresent()).isTrue();
    }

    @Test
    void 존재하지_않는_멤버는_관심_상점을_등록할_수_없다() {
        // given
        Long storeId = 1L;
        String memberName = "wrong";

        // when
        // then
        assertThatThrownBy(() -> storeCommandService.insertHeart(storeId, memberName))
                .isInstanceOf(MemberHandler.class)
                .hasMessageContaining("사용자가 없습니다.");
    }

    @Test
    void 존재하지_않는_상점은_관심_상점으로_등록할_수_없다() {
        // given
        Long storeId = 2L;
        String memberName = "Kakao_userId";

        // when
        // then
        assertThatThrownBy(() -> storeCommandService.insertHeart(storeId, memberName))
                .isInstanceOf(StoreHandler.class)
                .hasMessageContaining("존재하지 않는 상점입니다.");
    }

    @Test
    void 이미_관심_설정한_상점은_관심_상점으로_등록할_수_없다() {
        // given
        Long storeId = 1L;
        String memberName = "Kakao_userId";

        // when
        BaseResponseDto<HeartStoreResponseDto> responseDto = storeCommandService.insertHeart(storeId, memberName);

        // then
        assertThatThrownBy(() -> storeCommandService.insertHeart(storeId, memberName))
                .isInstanceOf(StoreHandler.class)
                .hasMessageContaining("이미 관심 상점으로 등록되었습니다.");
    }

    @Test
    void 관심_상점을_해제할_수_있다() {
        // given
        Long storeId = 1L;
        String memberName = "Kakao_userId";

        // when
        storeCommandService.insertHeart(storeId, memberName);
        BaseResponseDto<HeartStoreResponseDto> responseDto = storeCommandService.deleteHeart(storeId, memberName);
        Optional<HeartStoreEntity> heartStore = heartStoreRepository.findById(responseDto.getResult().getStoreId());

        // then
        assertThat(heartStore.isPresent()).isFalse();
    }

    @Test
    void 존재하지_않는_멤버는_관심_상점을_해제할_수_없다() {
        // given
        Long storeId = 1L;
        String memberName = "wrong";

        // when
        // then
        assertThatThrownBy(() -> storeCommandService.deleteHeart(storeId, memberName))
                .isInstanceOf(MemberHandler.class)
                .hasMessageContaining("사용자가 없습니다.");
    }

    @Test
    void 존재하지_않는_상점은_관심_상점을_해제할_수_없다() {
        // given
        Long storeId = 2L;
        String memberName = "Kakao_userId";

        // when
        // then
        assertThatThrownBy(() -> storeCommandService.deleteHeart(storeId, memberName))
                .isInstanceOf(StoreHandler.class)
                .hasMessageContaining("존재하지 않는 상점입니다.");
    }

    /**
     * FIXME: 존재하지 않는 관심상점은 하트가 비어있다는 의미이니까, 바로 등록해줘도 되는건가?
     * 그냥 존재하지 않는 관심상점이라고 해제할 수 없는 오류를 내는게 괜찮은 건가?
     */
    @Test
    void 존재하지_않는_관심상점은_관심_상점을_해제할_수_없다() {
        // given
        Long storeId = 1L;
        String memberName = "Kakao_userId";

        // when
        // then
        assertThatThrownBy(() -> storeCommandService.deleteHeart(storeId, memberName))
                .isInstanceOf(StoreHandler.class)
                .hasMessageContaining("해당 상점은 관심 상점으로 등록되지 않았으므로 삭제할 수 없습니다.");
    }
}