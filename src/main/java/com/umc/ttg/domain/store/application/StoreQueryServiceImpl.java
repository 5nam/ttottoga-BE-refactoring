package com.umc.ttg.domain.store.application;

import com.umc.ttg.domain.member.entity.HeartStore;
import com.umc.ttg.domain.member.entity.Member;
import com.umc.ttg.domain.member.repository.HeartStoreRepository;
import com.umc.ttg.domain.member.repository.MemberRepository;
import com.umc.ttg.domain.review.entity.Review;
import com.umc.ttg.domain.review.repository.ReviewRepository;
import com.umc.ttg.domain.store.dto.*;
import com.umc.ttg.domain.store.dto.converter.StoreConverter;
import com.umc.ttg.domain.store.entity.Menu;
import com.umc.ttg.domain.store.entity.Region;
import com.umc.ttg.domain.store.entity.Store;
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

import java.util.*;
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
    public BaseResponseDto<StoreFindResponseDto> findStore(Long storeId, String memberName) {

        Member member = validateCorrectMember(memberName);

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreHandler(ResponseCode.STORE_NOT_FOUND));

        boolean submitReview = reviewRepository.findByStoreAndMember(store, member).isPresent();
        boolean heartStore = heartStoreRepository.findByMemberAndStore(member, store).isPresent();

        return BaseResponseDto.onSuccess(StoreConverter.convertToStoreFindResponseDto(store, submitReview, heartStore), ResponseCode.OK);

    }

    /**
     * HOT 상점 먼저 랜덤으로 배치 후, 다음은 베스트(또또가 누적 리뷰 순)순으로 배치
     * 한 번의 요청마다 20개씩 넘겨줌(무한 스크롤 방식)
     */
    @Override
    public BaseResponseDto<Page<StoreResultResponseDto>> findStoreByRegion(Long regionId, int page, int size, String memberName) {

        validatePageAndSize(page, size);

        Member member = validateCorrectMember(memberName);
        Region region = regionRepository.findById(regionId).orElseThrow(() -> new StoreHandler(ResponseCode.REGION_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size);

        return BaseResponseDto.onSuccess(getStoresByRegion(region, member, pageable), ResponseCode.OK);

    }

    private Page<StoreResultResponseDto> getStoresByRegion(Region region, Member member, Pageable pageable) {

        List<StoreResultResponseDto> stores =
                storeRepository.findByRegion(region).stream()
                        .sorted(comparator())
                        .map(store -> new StoreResultResponseDto(store.getId(), store.getTitle(),
                                store.getImage(), store.getServiceInfo(), store.getReviewCount(),
                                heartStoreRepository.findByMemberAndStore(member, store).isPresent())).toList();

        return paging(stores,pageable);

    }

    @Override
    public BaseResponseDto<Page<StoreResultResponseDto>> findStoreByMenu(Long menuId, int page, int size, String memberName) {

        validatePageAndSize(page, size);

        Member member = validateCorrectMember(memberName);
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new StoreHandler(ResponseCode.MENU_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size);

        return BaseResponseDto.onSuccess(getStoresByMenu(menu, member, pageable), ResponseCode.OK);

    }

    private Page<StoreResultResponseDto> getStoresByMenu(Menu menu, Member member, Pageable pageable) {

        List<StoreResultResponseDto> stores =
                storeRepository.findByMenu(menu).stream()
                        .sorted(comparator())
                        .map(store -> new StoreResultResponseDto(store.getId(), store.getTitle(),
                                store.getImage(), store.getServiceInfo(), store.getReviewCount(),
                                heartStoreRepository.findByMemberAndStore(member, store).isPresent())).toList();

        return paging(stores,pageable);

    }

    @Override
    public BaseResponseDto<Page<StoreResultResponseDto>> searchStore(String keyword, int page, int size, String memberName) {

        validatePageAndSize(page, size);

        String correctKeyword = validateCorrectKeyword(keyword);

        Member member = validateCorrectMember(memberName);

        Pageable pageable = PageRequest.of(page, size);

        return BaseResponseDto.onSuccess(searchResult(correctKeyword, member, pageable), ResponseCode.OK);
    }

    private Page<StoreResultResponseDto> searchResult(String keyword, Member member, Pageable pageable) {

        List<StoreResultResponseDto> stores =
                storeRepository.findByTitleContainingOrNameContaining(keyword, keyword).stream()
                        .sorted(comparator())
                        .map(store -> new StoreResultResponseDto(store.getId(), store.getTitle(),
                                store.getImage(), store.getServiceInfo(), store.getReviewCount(),
                                heartStoreRepository.findByMemberAndStore(member, store).isPresent())).toList();

        return paging(stores,pageable);

    }

    @Override
    public BaseResponseDto<Page<StoreResultResponseDto>> getHeartStores(int page, int size, String memberName) {

        Member member = validateCorrectMember(memberName);

        Pageable pageable = PageRequest.of(page, size);

        return BaseResponseDto.onSuccess(getPagingHeartStores(member, pageable), ResponseCode.OK);

    }

    private Page<StoreResultResponseDto> getPagingHeartStores(Member member, Pageable pageable) {

        List<StoreResultResponseDto> heartStores = heartStoreRepository.findByMember(member).stream()
                .map(HeartStore::getStore)
                .sorted(comparator())
                .map(store -> new StoreResultResponseDto(store.getId(), store.getTitle(),
                        store.getImage(), store.getServiceInfo(), store.getReviewCount(), true)).toList();

        return paging(heartStores, pageable);

    }


    /**
     * 공통 기능들
     */
    private Comparator<Store> comparator() {
        return Comparator
                .comparing(Store::getHotYn)
                .thenComparing(Store::getReviewCount).reversed();
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

    private Member validateCorrectMember(String memberName) {

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

    @Override
    public BaseResponseDto<HomeResponseDto> getHome(String memberName) {

        Member member = validateCorrectMember(memberName);

        // top 15
        List<HomeResponseDto.Top15> top15 = getTop15(member);

        // hotStore - 랜덤으로
        List<HomeResponseDto.HotStore> hotStore = getHotStore();

        // reviews - 랜덤으로
        List<HomeResponseDto.HomeReviews> homeReview = getHomeReview();

        // ResponseDTO
        HomeResponseDto homeResponseDto = HomeResponseDto.builder()
                .top15(top15)
                .hotStores(hotStore)
                .homeReviews(homeReview).build();


        return BaseResponseDto.onSuccess(homeResponseDto, ResponseCode.OK);

    }

    private List<HomeResponseDto.HomeReviews> getHomeReview() {

        List<Review> reviews = reviewRepository.findAll();

        Collections.shuffle(reviews);

        return reviews.stream()
                .limit(5)
                .map(HomeResponseDto.HomeReviews::new)
                .collect(Collectors.toList());

    }

    private List<HomeResponseDto.HotStore> getHotStore() {

        List<HomeResponseDto.HotStore> hotStores = storeRepository.findAll().stream()
                .filter(store -> store.getHotYn().equals('y'))
                .map(HomeResponseDto.HotStore::new).collect(Collectors.toList());

        Collections.shuffle(hotStores);

        return hotStores.stream()
                .limit(3).collect(Collectors.toList());

    }

    private List<HomeResponseDto.Top15> getTop15(Member member) {

        List<HomeResponseDto.Top15> top15 = new ArrayList<>();

        List<Store> topStores = storeRepository
                .findAll(Sort.by(Sort.Direction.DESC, "reviewCount"))
                .stream().limit(15).toList();

        topStores.forEach(store ->
                top15.add(new HomeResponseDto.Top15(store, heartStoreRepository.findByMemberAndStore(member, store).isPresent())));

        return top15;

    }

    private Member saveTestMember() {

        return memberRepository.save(Member.builder()
                .name("test")
                .nickname("ddd")
                .profileImage("ddd")
                .phoneNum("010")
                .benefitCount(0)
                .build());

    }

}
