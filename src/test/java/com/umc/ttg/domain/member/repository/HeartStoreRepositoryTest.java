package com.umc.ttg.domain.member.repository;

import com.umc.ttg.domain.member.entity.HeartStoreEntity;
import com.umc.ttg.domain.member.entity.MemberEntity;
import com.umc.ttg.domain.store.entity.StoreEntity;
import com.umc.ttg.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(showSql = true)
@Sql("/sql/heartstore-repository-test-data.sql")
class HeartStoreRepositoryTest {

    @Autowired
    private HeartStoreRepository heartStoreRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StoreRepository storeRepository;

    @Test
    void findByMemberAndStore_로_관심_상점_데이터를_가져올_수_있다() {
        //given
        //when
        MemberEntity member = memberRepository.findById(1L).get();
        StoreEntity store = storeRepository.findById(1L).get();

        Optional<HeartStoreEntity> result = heartStoreRepository.findByMemberAndStore(member, store);

        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByMemberAndStore_는_관심상점_데이터가_없을_경우_Optional_Empty_를_반환한다() {
        //given
        //when
        MemberEntity member = memberRepository.findById(2L).get();
        StoreEntity store = storeRepository.findById(1L).get();

        Optional<HeartStoreEntity> result = heartStoreRepository.findByMemberAndStore(member, store);

        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void findByMember_로_관심상점_리스트_데이터를_받아올_수_있다() {
        //given
        //when
        MemberEntity member = memberRepository.findById(1L).get();

        List<HeartStoreEntity> heartStore = heartStoreRepository.findByMember(member);

        //then
        assertThat(heartStore.size()).isEqualTo(2);
    }

    @Test
    void findByMember_는_데이터가_없으면_빈_리스트를_반환한다() {
        //given
        //when
        MemberEntity member = memberRepository.findById(2L).get();

        List<HeartStoreEntity> heartStore = heartStoreRepository.findByMember(member);

        //then
        assertThat(heartStore.isEmpty()).isTrue();
    }
}