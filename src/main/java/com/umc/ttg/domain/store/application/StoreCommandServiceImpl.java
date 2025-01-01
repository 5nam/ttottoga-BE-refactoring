package com.umc.ttg.domain.store.application;

import com.umc.ttg.domain.member.entity.HeartStoreEntity;
import com.umc.ttg.domain.member.entity.MemberEntity;
import com.umc.ttg.domain.member.exception.handler.MemberHandler;
import com.umc.ttg.domain.member.repository.HeartStoreRepository;
import com.umc.ttg.domain.member.repository.MemberRepository;
import com.umc.ttg.domain.store.dto.HeartStoreResponseDto;
import com.umc.ttg.domain.store.dto.StoreRequestDto;
import com.umc.ttg.domain.store.dto.StoreResponseDto;
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

    private final FileService fileService;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final RegionRepository regionRepository;
    private final MemberRepository memberRepository;
    private final HeartStoreRepository heartStoreRepository;

    @Override
    @Transactional // 저장은 모든 과정이 완료되어야 하므로
    public BaseResponseDto<StoreResponseDto> saveStore(StoreRequestDto storeRequestDto) throws IOException {

        MenuEntity menuEntity = menuRepository.findById(storeRequestDto.getMenu())
                .orElseThrow(() -> new StoreHandler(ResponseCode.MENU_NOT_FOUND));

        RegionEntity regionEntity = regionRepository.findById(storeRequestDto.getRegion())
                .orElseThrow(() -> new StoreHandler(ResponseCode.REGION_NOT_FOUND));

        StoreEntity storeEntity = StoreEntity.builder()
                .storeRequestDto(storeRequestDto)
                .menu(menuEntity)
                .region(regionEntity)
                .storeImage(getImageLink(storeRequestDto.getStoreImage()))
                .build();

        StoreEntity savedStoreEntity = storeRepository.save(storeEntity);

        return BaseResponseDto.onSuccess(StoreConverter.convertToStoreResponse(savedStoreEntity.getId()), ResponseCode.OK);

    }

    private String getImageLink(MultipartFile multipartFile) throws IOException {

        return fileService.upload(multipartFile, "storeImage");

    }


    /**
     * FIXME: 변경하는 항목만 알아내서 update 하고 싶은데..
     */
    @Override
    @Transactional
    public BaseResponseDto<StoreResponseDto> updateStore(StoreRequestDto storeRequestDto, Long storeId) throws IOException {

        StoreEntity storeEntity = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreHandler(ResponseCode._BAD_REQUEST));

        MenuEntity menuEntity = menuRepository.findById(storeRequestDto.getMenu())
                .orElseThrow(() -> new StoreHandler(ResponseCode._BAD_REQUEST));

        RegionEntity regionEntity = regionRepository.findById(storeRequestDto.getRegion())
                .orElseThrow(() -> new StoreHandler(ResponseCode._BAD_REQUEST));

        storeEntity.update(storeRequestDto, menuEntity, regionEntity, getImageLink(storeRequestDto.getStoreImage()));

        return BaseResponseDto.onSuccess(StoreConverter.convertToStoreResponse(storeEntity.getId()), ResponseCode.OK);

    }

    public BaseResponseDto<HeartStoreResponseDto> insertHeart(Long storeId, String memberName) {

        MemberEntity memberEntity = memberRepository.findByName(memberName)
                .orElseThrow(() -> new MemberHandler(ResponseCode.MEMBER_NOT_FOUND));

        StoreEntity storeEntity = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreHandler(ResponseCode.STORE_NOT_FOUND));

        if (heartStoreRepository.findByMemberAndStore(memberEntity, storeEntity).isPresent()) {
            throw new StoreHandler(ResponseCode.ALREADY_HEART_EXCEPTION);
        }

        HeartStoreEntity heartStoreEntity = HeartStoreEntity.builder()
                .member(memberEntity)
                .store(storeEntity)
                .build();

        HeartStoreEntity savedHeartStoreEntity = heartStoreRepository.save(heartStoreEntity);

        HeartStoreResponseDto heartStoreResponseDto = new HeartStoreResponseDto(savedHeartStoreEntity.getId(), memberEntity.getId(), storeEntity.getId());

        return BaseResponseDto.onSuccess(heartStoreResponseDto, ResponseCode.OK);
    }

    @Override
    public BaseResponseDto<HeartStoreResponseDto> deleteHeart(Long storeId, String memberName) {

        MemberEntity memberEntity = memberRepository.findByName(memberName)
                .orElseThrow(() -> new MemberHandler(ResponseCode.MEMBER_NOT_FOUND));

        StoreEntity storeEntity = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreHandler(ResponseCode.STORE_NOT_FOUND));

        HeartStoreEntity heartStoreEntity = heartStoreRepository.findByMemberAndStore(memberEntity, storeEntity)
                .orElseThrow(() -> new StoreHandler(ResponseCode.NOT_HEART_EXCEPTION));

        HeartStoreResponseDto heartStoreResponseDto = new HeartStoreResponseDto(heartStoreEntity.getId(), memberEntity.getId(), storeEntity.getId());

        heartStoreRepository.delete(heartStoreEntity);

        return BaseResponseDto.onSuccess(heartStoreResponseDto, ResponseCode.OK);
    }

}
