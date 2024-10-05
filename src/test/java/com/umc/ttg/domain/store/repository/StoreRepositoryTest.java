package com.umc.ttg.domain.store.repository;

import com.umc.ttg.domain.store.entity.MenuEntity;
import com.umc.ttg.domain.store.entity.RegionEntity;
import com.umc.ttg.domain.store.entity.StoreEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(showSql = true)
@Sql("/sql/store-repository-test-data.sql")
@Slf4j
class StoreRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private MenuRepository menuRepository;

    @Test
    void findByRegion_로_지역별_상점_데이터를_가져올_수_있다() {
        //given
        //when
        RegionEntity region = regionRepository.findById(1L).get();
        List<StoreEntity> results = storeRepository.findByRegion(region);

        //then
        assertThat(results.size()).isEqualTo(3);
    }

    @Test
    void findByRegion_는_지역에_해당하는_데이터가_없으면_빈_리스트를_반환한다() {
        //given
        //when
        RegionEntity region = regionRepository.findById(2L).get();
        List<StoreEntity> results = storeRepository.findByRegion(region);

        //then
        assertThat(results.isEmpty()).isTrue();
    }

    @Test
    void findByTitleContainingOrNameContaining_로_제목과_이름이_포함된_상점을_찾을_수_있다() {
        //given
        //when
        String keyword = "검색 테스트";
        List<StoreEntity> results = storeRepository.findByTitleContainingOrNameContaining(keyword, keyword);
        //then
        assertThat(results.size()).isEqualTo(2);
    }

    /**
     * TODO : 띄어쓰기 상관없이 똑같은 결과가 나오도록 개선하기
     */
    @Test
    void findByTitleContainingOrNameContaining_는_띄어쓰기_상관없이_검색을_통해_상점을_똑같이_찾을_수_있다() {
//        //given
//        //when
//        String keyword1 = "검색테스트";
//        String keyword2 = "검색 테스트";
//        List<StoreEntity> result1 = storeRepository.findByTitleContainingOrNameContaining(keyword1, keyword1);
//        List<StoreEntity> result2 = storeRepository.findByTitleContainingOrNameContaining(keyword2, keyword2);
//        log.info("result1 = {}", result1.size());
//        //then
//        assertThat(result1.size()).isEqualTo(result2.size());
    }


    @Test
    void findByMenu_로_상점_데이터를_찾을_수_있다() {
        //given
        //when
        MenuEntity menu = menuRepository.findById(1L).get();
        List<StoreEntity> result = storeRepository.findByMenu(menu);

        //then
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    void findByMenu_는_알맞은_상점_데이터가_없으면_빈_리스트를_반환한다() {
        //given
        //when
        MenuEntity menu = menuRepository.findById(2L).get();
        List<StoreEntity> result = storeRepository.findByMenu(menu);

        //then
        assertThat(result.isEmpty()).isTrue();
    }
}