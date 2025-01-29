package com.umc.ttg.domain.store.application;

import com.umc.ttg.domain.member.entity.HeartStoreEntity;
import com.umc.ttg.domain.member.entity.MemberEntity;
import com.umc.ttg.domain.member.repository.HeartStoreRepository;
import com.umc.ttg.domain.member.repository.MemberRepository;
import com.umc.ttg.domain.review.entity.ReviewEntity;
import com.umc.ttg.domain.review.repository.ReviewRepository;
import com.umc.ttg.domain.store.dto.HomeResponseDto;
import com.umc.ttg.domain.store.dto.StoreFindResponseDto;
import com.umc.ttg.domain.store.dto.StoreResultResponseDto;
import com.umc.ttg.domain.store.dto.converter.StoreConverter;
import com.umc.ttg.domain.store.entity.MenuEntity;
import com.umc.ttg.domain.store.entity.RegionEntity;
import com.umc.ttg.domain.store.entity.StoreEntity;
import com.umc.ttg.domain.store.exception.handler.StoreHandler;
import com.umc.ttg.domain.store.repository.MenuRepository;
import com.umc.ttg.domain.store.repository.RegionRepository;
import com.umc.ttg.domain.store.repository.StoreRepository;
import com.umc.ttg.global.common.BaseResponseDto;
import com.umc.ttg.global.common.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreQueryServiceImpl implements StoreQueryService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final RegionRepository regionRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final HeartStoreRepository heartStoreRepository;

    @Override
    public BaseResponseDto<StoreFindResponseDto> getById(Long storeId, String memberName) {
        // 상점 내부를 조회하는 것은 회원가입 없이도 가능하므로 체크만 한다?
        MemberEntity memberEntity = validateCorrectMember(memberName);

        StoreEntity storeEntity = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreHandler(ResponseCode.STORE_NOT_FOUND));

        /**
         * FIXME: MemberEntity 에 null 이 담기면, memberName 즉, 로그인 안한 상태로 접근하는 것이므로 아예 아래가 False 여도 상관 없음
         * 불필요한 조회 줄이기 위해 고치기!
         */
        // 리뷰를 남긴 적이 있는 상점인지에 대한 여부
        boolean submitReview = reviewRepository.findByStoreAndMember(storeEntity, memberEntity).isPresent();
        // 관심으로 등록한 적이 있는 상점인지에 대한 여부
        boolean heartStore = heartStoreRepository.findByMemberAndStore(memberEntity, storeEntity).isPresent();

        return BaseResponseDto.onSuccess(StoreConverter.convertToStoreFindResponseDto(storeEntity, submitReview, heartStore), ResponseCode.OK);

    }

    /**
     * HOT 상점 먼저 랜덤으로 배치 후, 다음은 베스트(또또가 누적 리뷰 순)순으로 배치
     * 한 번의 요청마다 20개씩 넘겨줌(무한 스크롤 방식)
     */

    /**
     * FIXME : JPA 의 Page 리턴 사용하고, map 으로 리턴값 반환하는 식으로 변환
     * - 지금은 너무 복잡한 로직임
     * - 사용법을 몰라서 너무 붙여넣는 식으로 코드를 짬
     */
    @Override
    public BaseResponseDto<Page<StoreResultResponseDto>> getByRegion(Long regionId, int page, int size, String memberName) {

        validatePageAndSize(page, size);

        MemberEntity memberEntity = validateCorrectMember(memberName);
        RegionEntity regionEntity = regionRepository.findById(regionId).orElseThrow(() -> new StoreHandler(ResponseCode.REGION_NOT_FOUND));

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "HotYn", "ReviewCount"));

        Page<StoreEntity> result = storeRepository.findByRegionId(regionId, pageRequest);
        Page<StoreResultResponseDto> results = result.map(store -> new StoreResultResponseDto(store.getId(), store.getTitle(),
                                                            store.getImage(), store.getServiceInfo(), store.getReviewCount(),
                                                            heartStoreRepository.findByMemberAndStore(memberEntity, store).isPresent()));

        return BaseResponseDto.onSuccess(results, ResponseCode.OK);

    }

    /**
     * FIXME : JPA 의 Page 리턴 사용하고, map 으로 리턴값 반환하는 식으로 변환
     * - 지금은 너무 복잡한 로직임
     * - 사용법을 몰라서 너무 붙여넣는 식으로 코드를 짬
     */
    @Override
    public BaseResponseDto<Page<StoreResultResponseDto>> getByMenu(Long menuId, int page, int size, String memberName) {

        validatePageAndSize(page, size);

        MemberEntity memberEntity = validateCorrectMember(memberName);
        MenuEntity menuEntity = menuRepository.findById(menuId).orElseThrow(() -> new StoreHandler(ResponseCode.MENU_NOT_FOUND));

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "HotYn", "ReviewCount"));
        Page<StoreEntity> result = storeRepository.findByMenuId(menuId, pageRequest);
        Page<StoreResultResponseDto> results = result.map(store -> new StoreResultResponseDto(store.getId(), store.getTitle(),
                store.getImage(), store.getServiceInfo(), store.getReviewCount(),
                heartStoreRepository.findByMemberAndStore(memberEntity, store).isPresent()));

        return BaseResponseDto.onSuccess(results, ResponseCode.OK);

    }

    @Override
    public BaseResponseDto<Page<StoreResultResponseDto>> search(String keyword, int page, int size, String memberName) {

        validatePageAndSize(page, size);

        String correctKeyword = validateCorrectKeyword(keyword);

        MemberEntity memberEntity = validateCorrectMember(memberName);

        Pageable pageable = PageRequest.of(page, size);

        return BaseResponseDto.onSuccess(searchResult(correctKeyword, memberEntity, pageable), ResponseCode.OK);
    }

    private Page<StoreResultResponseDto> searchResult(String keyword, MemberEntity memberEntity, Pageable pageable) {

        List<StoreResultResponseDto> stores =
                storeRepository.findByTitleContainingOrNameContaining(keyword, keyword).stream()
                        .sorted(comparator())
                        .map(store -> new StoreResultResponseDto(store.getId(), store.getTitle(),
                                store.getImage(), store.getServiceInfo(), store.getReviewCount(),
                                heartStoreRepository.findByMemberAndStore(memberEntity, store).isPresent())).toList();

        return paging(stores,pageable);

    }

    @Override
    public BaseResponseDto<Page<StoreResultResponseDto>> findHeartStores(int page, int size, String memberName) {

        MemberEntity memberEntity = validateCorrectMember(memberName);

        Pageable pageable = PageRequest.of(page, size);

        return BaseResponseDto.onSuccess(findPagingHeartStores(memberEntity, pageable), ResponseCode.OK);

    }

    private Page<StoreResultResponseDto> findPagingHeartStores(MemberEntity memberEntity, Pageable pageable) {

        List<StoreResultResponseDto> heartStores = heartStoreRepository.findByMember(memberEntity).stream()
                .map(HeartStoreEntity::getStore)
                .sorted(comparator())
                .map(store -> new StoreResultResponseDto(store.getId(), store.getTitle(),
                        store.getImage(), store.getServiceInfo(), store.getReviewCount(), true)).toList();

        return paging(heartStores, pageable);

    }


    /**
     * 공통 기능들
     */
    private Comparator<StoreEntity> comparator() {
        return Comparator
                .comparing(StoreEntity::getHotYn)
                .thenComparing(StoreEntity::getReviewCount).reversed();
    }

    private Page paging(List<?> stores, Pageable pageable) {

        // 다음 페이지 요청 시, offset 정보 활용하여 데이터 선별하여 전달
        int start = Math.toIntExact(pageable.getOffset());
        int end = Math.min((start + pageable.getPageSize()), stores.size());

        if (start >= end) {
            throw new StoreHandler(ResponseCode.PAGE_NOT_FOUND);
        }

        return new PageImpl<>(stores.subList(start, end), pageable, stores.size());

    }

    private String validateCorrectKeyword(String keyword) {

        if(keyword == null || keyword.isEmpty() || keyword.isBlank()) {
            throw new StoreHandler(ResponseCode.SEARCH_KEYWORD_NOT_FOUND);
        }

        return keyword;

    }

    private MemberEntity validateCorrectMember(String memberName) {

        if(memberName == null) {
            return null;
        }

        return memberRepository.findByName(memberName).orElseThrow(() -> new StoreHandler(ResponseCode.MEMBER_NOT_FOUND));

    }

    private void validatePageAndSize(int page, int size) {
        if(page < 0 || size < 0) {
            throw new StoreHandler(ResponseCode.PAGE_AND_SIZE_NOT_CORRECT);
        }
    }

    /**
     * FIXME : 홈과 관련된 기능과 상점과 관련된 기능을 나눠야 할듯
     * @param memberName
     * @return
     */
    @Override
    public BaseResponseDto<HomeResponseDto> findHome(String memberName) {

        MemberEntity memberEntity = validateCorrectMember(memberName);

        // top 15
        List<HomeResponseDto.Top15> top15 = findTop15(memberEntity);

        // hotStore - 랜덤으로
        List<HomeResponseDto.HotStore> hotStore = findHotStore();

        // reviews - 랜덤으로
        List<HomeResponseDto.HomeReviews> homeReview = findHomeReview();

        // ResponseDTO
        HomeResponseDto homeResponseDto = HomeResponseDto.builder()
                .top15(top15)
                .hotStores(hotStore)
                .homeReviews(homeReview).build();


        return BaseResponseDto.onSuccess(homeResponseDto, ResponseCode.OK);

    }

    private List<HomeResponseDto.HomeReviews> findHomeReview() {

        List<ReviewEntity> reviewEntities = reviewRepository.findAll();

        Collections.shuffle(reviewEntities);

        return reviewEntities.stream()
                .limit(5)
                .map(HomeResponseDto.HomeReviews::new)
                .collect(Collectors.toList());

    }

    private List<HomeResponseDto.HotStore> findHotStore() {

        List<HomeResponseDto.HotStore> hotStores = storeRepository.findAll().stream()
                .filter(store -> store.getHotYn().equals('y'))
                .map(HomeResponseDto.HotStore::new).collect(Collectors.toList());

        Collections.shuffle(hotStores);

        return hotStores.stream()
                .limit(3).collect(Collectors.toList());

    }

    private List<HomeResponseDto.Top15> findTop15(MemberEntity memberEntity) {

        List<HomeResponseDto.Top15> top15 = new ArrayList<>();

        List<StoreEntity> topStoreEntities = storeRepository
                .findAll(Sort.by(Sort.Direction.DESC, "reviewCount"))
                .stream().limit(15).toList();

        topStoreEntities.forEach(store ->
                top15.add(new HomeResponseDto.Top15(store, heartStoreRepository.findByMemberAndStore(memberEntity, store).isPresent())));

        return top15;

    }

//    private MemberEntity saveTestMember() {
//
//        return memberRepository.save(MemberEntity.builder()
//                .name("test")
//                .nickname("ddd")
//                .profileImage("ddd")
//                .phoneNum("010")
//                .benefitCount(0)
//                .build());
//
//    }

}
