package com.umc.ttg.domain.store.api;

import com.umc.ttg.domain.member.application.MemberService;
import com.umc.ttg.domain.store.application.StoreCommandService;
import com.umc.ttg.domain.store.application.StoreQueryService;
import com.umc.ttg.domain.store.dto.*;
import com.umc.ttg.global.common.BaseResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
@Slf4j
public class StoreController {

    private final StoreCommandService storeCommandService;
    private final StoreQueryService storeQueryService;
    private final MemberService memberService;

    @PostMapping
    public BaseResponseDto<StoreResponseDto> createStore(@ModelAttribute @Valid StoreRequestDto storeRequestDto) throws IOException {

        return storeCommandService.saveStore(storeRequestDto);

    }

    @GetMapping("/{store-id}")
    public BaseResponseDto<StoreFindResponseDto> findStore(@PathVariable("store-id") Long storeId, HttpServletRequest request) {

        /**
         * Header 토큰에서 멤버 ID 받아오는 로직 추가 예정
         * Header 에서 Barer token 이 없을 경우 null 을 반환하는 로직 추가
         */
        String memberName = memberService.permitAllAccess(request);

        return storeQueryService.findStore(storeId, memberName);

    }

    /**
     * 정렬 기준 X : 무조건 베스트순이므로(내림차순)
     * @param regionId default 1L(지역>전체)
     * @return
     */
    @GetMapping("/region-categories")
    public BaseResponseDto<Page<StoreResultResponseDto>> findStoreByRegion(@RequestParam Optional<Long> regionId,
                                                                                 @RequestParam Optional<Integer> page,
                                                                                 @RequestParam Optional<Integer> size, HttpServletRequest request) {

        /**
         * Header 토큰에서 멤버 ID 받아오는 로직 추가 예정
         * Header 에서 Barer token 이 없을 경우 null 을 반환하는 로직 추가
         */
        String memberName = memberService.permitAllAccess(request);

        return storeQueryService.findStoreByRegion(regionId.orElse(1L), page.orElse(0), size.orElse(20), memberName);

    }

    @GetMapping("/menu-categories")
    public BaseResponseDto<Page<StoreResultResponseDto>> findStoreByMenu(@RequestParam Optional<Long> menuId,
                                                                             @RequestParam Optional<Integer> page,
                                                                             @RequestParam Optional<Integer> size, HttpServletRequest request) {

        /**
         * Header 토큰에서 멤버 ID 받아오는 로직 추가 예정
         * Header 에서 Barer token 이 없을 경우 null 을 반환하는 로직 추가
         */
        String memberName = memberService.permitAllAccess(request);

        return storeQueryService.findStoreByMenu(menuId.orElse(1L), page.orElse(0), size.orElse(20), memberName);

    }

    @GetMapping("/home")
    public BaseResponseDto<HomeResponseDto> home(HttpServletRequest request) {

        /**
         * Header 토큰에서 멤버 ID 받아오는 로직 추가 예정
         * Header 에서 Barer token 이 없을 경우 null 을 반환하는 로직 추가
         */

        // Test MemberId
        String memberName = memberService.permitAllAccess(request);

        return storeQueryService.getHome(memberName);

    }

    @GetMapping("/search")
    public BaseResponseDto<Page<StoreResultResponseDto>> searchStore(@RequestParam(value = "keyword", required = false) String keyword,
                                                                     @RequestParam Optional<Integer> page,
                                                                     @RequestParam Optional<Integer> size, HttpServletRequest request) {

        /**
         * Header 토큰에서 멤버 ID 받아오는 로직 추가 예정
         * Header 에서 Barer token 이 없을 경우 null 을 반환하는 로직 추가
         */
        String memberName = memberService.permitAllAccess(request);

        return storeQueryService.searchStore(keyword, page.orElse(0), size.orElse(20), memberName);

    }

    @PutMapping("/{store-id}")
    public BaseResponseDto<StoreResponseDto> storeUpdate(@ModelAttribute @Valid StoreRequestDto storeRequestDto, @PathVariable("store-id") Long storeId) throws IOException {

        /**
         * Header 토큰에서 멤버 ID 받아오는 로직 추가 예정
         */
        Long memberId = 2L;

        return storeCommandService.updateStore(storeRequestDto, storeId);

    }

    @GetMapping("/heart")
    public BaseResponseDto<Page<StoreResultResponseDto>> findHeartStores(@RequestParam Optional<Integer> page,
                                                                         @RequestParam Optional<Integer> size,
                                                                         HttpServletRequest request) {

        String memberName = memberService.retrieveName(request);

        return storeQueryService.getHeartStores(page.orElse(0), size.orElse(20), memberName);

    }

    @PostMapping("/{store-id}/heart")
    public BaseResponseDto<HeartStoreResponseDto> insertHeartStore (@PathVariable("store-id") Long storeId, HttpServletRequest request) {

        String memberName = memberService.retrieveName(request);

        return storeCommandService.insertHeart(storeId, memberName);
    }

    @DeleteMapping("/{store-id}/heart")
    public BaseResponseDto<HeartStoreResponseDto> deleteHeartStore (@PathVariable("store-id") Long storeId, HttpServletRequest request) {

        String memberName = memberService.retrieveName(request);

        return storeCommandService.deleteHeart(storeId, memberName);
    }

}
