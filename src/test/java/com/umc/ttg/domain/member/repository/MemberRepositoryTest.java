package com.umc.ttg.domain.member.repository;

import com.umc.ttg.domain.member.entity.MemberEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(showSql = true)
@Sql("/sql/member-repository-test-data.sql")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void findByName_로_유저_데이터를_찾아올_수_있다() {
        //given

        //when
        Optional<MemberEntity> member = memberRepository.findByName("Kakao_userId");

        //then
        assertThat(member.isPresent()).isTrue();
    }

    @Test
    void findByName_은_데이터가_없으면_Optional_empty_를_내려준다() {
        //given

        //when
        Optional<MemberEntity> member = memberRepository.findByName("nothing");

        //then
        assertThat(member.isEmpty()).isTrue();
    }


}