package com.umc.ttg.domain.member.repository;

import com.umc.ttg.domain.member.entity.MemberEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(showSql = true)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void MemberRepository_가_제대로_연결되었다() { // jpa 랑 h2 제대로 연결되었는지 확인
        //given
        MemberEntity memberEntity = MemberEntity.builder()
                .userId("test")
                .nickname("nickname")
                .profileImage("profileImage")
                .type("type")
                .build();
        //when
        MemberEntity result = memberRepository.save(memberEntity);

        //then
        assertThat(result.getId()).isNotNull(); // 저장이 잘 되어 있다면, id 가 잘 할당되어 있을 것!
    }

    @Test
    void findByName_로_유저_데이터를_찾아올_수_있다() {
        //given
        MemberEntity memberEntity = MemberEntity.builder()
                .userId("test")
                .nickname("nickname")
                .profileImage("profileImage")
                .type("type")
                .build();

        //when
        memberRepository.save(memberEntity);
        Optional<MemberEntity> member = memberRepository.findByName("test");

        //then
        assertThat(member.isPresent()).isTrue();
    }

    @Test
    void findByName_은_데이터가_없으면_Optional_empty_를_내려준다() {
        //given
        MemberEntity memberEntity = MemberEntity.builder()
                .userId("test")
                .nickname("nickname")
                .profileImage("profileImage")
                .type("type")
                .build();

        //when
        memberRepository.save(memberEntity);
        Optional<MemberEntity> member = memberRepository.findByName("nothing");

        //then
        assertThat(member.isEmpty()).isTrue();
    }


}