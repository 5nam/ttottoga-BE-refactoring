insert into `member`
(`id`, `name`, `nickname`, `profile_image`, `phone_num`, `benefit_count`, `password`, `type`, `role`)
values (1, 'Kakao_userId', 'test', 'profileImage', '010-0000-0000', 0, 'password', 'type', 'USER');
insert into `member`
(`id`, `name`, `nickname`, `profile_image`, `phone_num`, `benefit_count`, `password`, `type`, `role`)
values (2, 'naver_userId', 'test2', 'profileImage2', '020-0000-0000', 0, 'password2', 'type2', 'USER');

insert into `region` (`id`, `name`, `upper_id`)
values (1, '서울', null);
insert into `region` (`id`, `name`, `upper_id`)
values (2, '경기', null);

insert into `menu` (`id`, `name`)
values (1, '한식');
insert into `menu` (`id`, `name`)
values (2, '중식');

insert into `store` (`id`, `name`, `title`, `sub_title`, `image`,
                     `use_info`, `sale_info`, `place_info`, `spon_info`, `service_info`,
                     `hot_yn`, `review_count`, `review_span`, `address`, `region_id`, `menu_id`)
values (1, 'testStore', '검색 테스트', 'subTitle', 'image',
        'use_info', 'sale_info', 'place_info', 'span_info', 'service_info',
        'y', 0, 5, 'address', 1, 1);
insert into `store` (`id`, `name`, `title`, `sub_title`, `image`,
                     `use_info`, `sale_info`, `place_info`, `spon_info`, `service_info`,
                     `hot_yn`, `review_count`, `review_span`, `address`, `region_id`, `menu_id`)
values (2, 'testStore2', 'title2', 'subTitle2', 'image2',
        'use_info2', 'sale_info2', 'place_info2', 'span_info2', 'service_info2',
        'y', 0, 5, 'address2', 1, 1);
insert into `store` (`id`, `name`, `title`, `sub_title`, `image`,
                     `use_info`, `sale_info`, `place_info`, `spon_info`, `service_info`,
                     `hot_yn`, `review_count`, `review_span`, `address`, `region_id`, `menu_id`)
values (3, '검색 테스트', 'title2', 'subTitle2', 'image2',
        'use_info2', 'sale_info2', 'place_info2', 'span_info2', 'service_info2',
        'y', 0, 5, 'address2', 1, 1);