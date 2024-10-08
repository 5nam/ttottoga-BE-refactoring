package com.umc.ttg.domain.member.application;

import com.umc.ttg.domain.member.dto.MemberImageRequestDTO;
import com.umc.ttg.domain.member.dto.MemberImageResponseDTO;
import com.umc.ttg.domain.member.entity.MemberEntity;
import com.umc.ttg.domain.member.exception.handler.MemberHandler;
import com.umc.ttg.domain.member.repository.MemberRepository;
import com.umc.ttg.global.common.BaseResponseDto;
import com.umc.ttg.global.common.ResponseCode;
import com.umc.ttg.global.error.handler.AwsS3Handler;
import com.umc.ttg.global.util.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final FileService awsS3FileService;

    @Override
    @Transactional
    public BaseResponseDto<MemberImageResponseDTO> updateImage(MemberImageRequestDTO memberImageRequestDTO, String memberName) throws IOException {

        MemberEntity memberEntity = memberRepository.findByName(memberName)
                .orElseThrow(() -> new MemberHandler(ResponseCode.MEMBER_NOT_FOUND));

        if (!CheckFileExtension(memberImageRequestDTO)) {
            throw new AwsS3Handler(ResponseCode.S3_UPLOAD_FAIL);
        }

        memberEntity.setProfileImage(getS3ImageLink(memberImageRequestDTO.getProfileImage()));

        memberRepository.save(memberEntity);

        MemberImageResponseDTO memberImageResponseDTO = new MemberImageResponseDTO(memberEntity.getId(), memberEntity.getProfileImage());

        return BaseResponseDto.onSuccess(memberImageResponseDTO, ResponseCode.OK);
    }

    private String getS3ImageLink(MultipartFile multipartFile) throws IOException {

        return awsS3FileService.upload(multipartFile, "memberImage");

    }

    private boolean CheckFileExtension(MemberImageRequestDTO memberImageRequestDTO) {

        // 지원하지 않는 파일일 경우
        String fileName = String.valueOf(memberImageRequestDTO.getProfileImage().getOriginalFilename());
        int lastIndex = fileName.lastIndexOf(".");
        String fileExtensionName = fileName.substring(lastIndex + 1);

        if (!fileExtensionName.equals("jpg") && !fileExtensionName.equals("png") && !fileExtensionName.equals("jpeg"))
            return false;

        return true;
    }
}
