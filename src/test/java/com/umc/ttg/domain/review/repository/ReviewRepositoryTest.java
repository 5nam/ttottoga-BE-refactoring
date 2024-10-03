package com.umc.ttg.domain.review.repository;

import com.umc.ttg.domain.member.entity.MemberEntity;
import com.umc.ttg.domain.member.repository.MemberRepository;
import com.umc.ttg.domain.review.entity.ReviewEntity;
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
@Sql("/sql/review-repository-test-data.sql")
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void findByStoreAndMember_로_리뷰_데이터를_찾을_수_있다() {
        //given
        //when
        StoreEntity store = storeRepository.findById(1L).get();
        MemberEntity member = memberRepository.findById(1L).get();
        Optional<ReviewEntity> result = reviewRepository.findByStoreAndMember(store, member);
        //then
        assertThat(result.isPresent()).isTrue();
    }
    @Test
    void findByStoreAndMember_는_데이터가_없으면_Optional_empty_를_내려준다() {
        //given
        //when
        StoreEntity store = storeRepository.findById(2L).get();
        MemberEntity member = memberRepository.findById(1L).get();
        Optional<ReviewEntity> result = reviewRepository.findByStoreAndMember(store, member);
        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void findAllByMemberId_로_리뷰_데이터를_찾아올_수_있다() {
        //given
        //when
        List<ReviewEntity> result = reviewRepository.findAllByMemberId(1L);
        //then
        assertThat(result.size() > 0).isTrue();
    }

    @Test
    void findAllByMemberId_는_리뷰_데이터가_없으면_빈_리스트를_반환한다() {
        //given
        //when
        List<ReviewEntity> result = reviewRepository.findAllByMemberId(2L);
        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void findByStoreIdAndMemberId_로_리뷰_데이터를_찾아올_수_있다() {
        //given
        //when
        Optional<ReviewEntity> result = reviewRepository.findByStoreIdAndMemberId(1L, 1L);
        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByStoreIdAndMemberId_는_리뷰_데이터가_없으면_Optional_Empty_를_반환한다() {
        //given
        //when
        Optional<ReviewEntity> result = reviewRepository.findByStoreIdAndMemberId(1L, 2L);
        //then
        assertThat(result.isEmpty()).isTrue();
    }
}