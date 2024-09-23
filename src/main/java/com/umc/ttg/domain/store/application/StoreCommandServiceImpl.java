package com.umc.ttg.domain.store.application;

import com.umc.ttg.domain.member.entity.HeartStore;
import com.umc.ttg.domain.member.entity.Member;
import com.umc.ttg.domain.member.exception.handler.MemberHandler;
import com.umc.ttg.domain.member.repository.HeartStoreRepository;
import com.umc.ttg.domain.member.repository.MemberRepository;
import com.umc.ttg.domain.store.dto.*;
import com.umc.ttg.domain.store.exception.handler.StoreHandler;
import com.umc.ttg.domain.store.dto.converter.StoreConverter;
import com.umc.ttg.domain.store.entity.Menu;
import com.umc.ttg.domain.store.entity.Region;
import com.umc.ttg.domain.store.entity.Store;
import com.umc.ttg.domain.store.repository.MenuRepository;
import com.umc.ttg.domain.store.repository.RegionRepository;
import com.umc.ttg.domain.store.repository.StoreRepository;
import com.umc.ttg.global.common.BaseResponseDto;
import com.umc.ttg.global.common.ResponseCode;
import com.umc.ttg.global.util.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreCommandServiceImpl implements StoreCommandService {

    private final FileService awsS3FileService;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final RegionRepository regionRepository;
    private final MemberRepository memberRepository;
    private final HeartStoreRepository heartStoreRepository;

    @Override
    @Transactional // 저장은 모든 과정이 완료되어야 하므로
    public BaseResponseDto<StoreResponseDto> saveStore(StoreRequestDto storeRequestDto) throws IOException {

        Menu menu = menuRepository.findById(storeRequestDto.getMenu())
                .orElseThrow(() -> new StoreHandler(ResponseCode.MENU_NOT_FOUND));

        Region region = regionRepository.findById(storeRequestDto.getRegion())
                .orElseThrow(() -> new StoreHandler(ResponseCode.REGION_NOT_FOUND));

        Store store = Store.builder()
                .storeRequestDto(storeRequestDto)
                .menu(menu)
                .region(region)
                .storeImage(getS3ImageLink(storeRequestDto.getStoreImage())).build();

        Store savedStore = storeRepository.save(store);

        return BaseResponseDto.onSuccess(StoreConverter.convertToStoreResponse(savedStore.getId()), ResponseCode.OK);

    }

    private String getS3ImageLink(MultipartFile multipartFile) throws IOException {

        return awsS3FileService.upload(multipartFile, "storeImage");

    }



    @Override
    @Transactional
    public BaseResponseDto<StoreResponseDto> updateStore(StoreRequestDto storeRequestDto, Long storeId) throws IOException {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreHandler(ResponseCode._BAD_REQUEST));

        Menu menu = menuRepository.findById(storeRequestDto.getMenu())
                .orElseThrow(() -> new StoreHandler(ResponseCode._BAD_REQUEST));

        Region region = regionRepository.findById(storeRequestDto.getRegion())
                .orElseThrow(() -> new StoreHandler(ResponseCode._BAD_REQUEST));

        store.update(storeRequestDto, menu, region, getS3ImageLink(storeRequestDto.getStoreImage()));

        return BaseResponseDto.onSuccess(StoreConverter.convertToStoreResponse(store.getId()), ResponseCode.OK);

    }

    public BaseResponseDto<HeartStoreResponseDto> insertHeart(Long storeId, String memberName) {

        Member member = memberRepository.findByName(memberName)
                .orElseThrow(() -> new MemberHandler(ResponseCode.MEMBER_NOT_FOUND));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreHandler(ResponseCode.STORE_NOT_FOUND));

        if (heartStoreRepository.findByMemberAndStore(member, store).isPresent()) {
            throw new StoreHandler(ResponseCode.ALREADY_HEART_EXCEPTION);
        }

        HeartStore heartStore = HeartStore.builder()
                .member(member)
                .store(store)
                .build();

        HeartStore savedHeartStore = heartStoreRepository.save(heartStore);

        HeartStoreResponseDto heartStoreResponseDto = new HeartStoreResponseDto(savedHeartStore.getId(), member.getId(), store.getId());

        return BaseResponseDto.onSuccess(heartStoreResponseDto, ResponseCode.OK);
    }

    @Override
    public BaseResponseDto<HeartStoreResponseDto> deleteHeart(Long storeId, String memberName) {

        Member member = memberRepository.findByName(memberName)
                .orElseThrow(() -> new MemberHandler(ResponseCode.MEMBER_NOT_FOUND));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreHandler(ResponseCode.STORE_NOT_FOUND));

        HeartStore heartStore = heartStoreRepository.findByMemberAndStore(member, store)
                .orElseThrow(() -> new StoreHandler(ResponseCode.NOT_HEART_EXCEPTION));

        HeartStoreResponseDto heartStoreResponseDto = new HeartStoreResponseDto(heartStore.getId(), member.getId(), store.getId());

        heartStoreRepository.delete(heartStore);

        return BaseResponseDto.onSuccess(heartStoreResponseDto, ResponseCode.OK);
    }

}
