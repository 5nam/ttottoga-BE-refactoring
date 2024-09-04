# TTOTTOGA-PROJECT

## 서비스 설명
![image](https://github.com/Ttottoga/BE/assets/86754153/3572108f-67e1-4a58-8e6b-2d3a5898243c)
![image](https://github.com/Ttottoga/BE/assets/86754153/5bbcc097-ea2d-4bc5-9662-26376ca31fa2)
![image](https://github.com/Ttottoga/BE/assets/86754153/e296b6d9-a0ff-4bf4-8341-f73d4dc0129c)
![image](https://github.com/Ttottoga/BE/assets/86754153/88d55f02-e1a8-4675-9110-a675a3e82b1c)
![image](https://github.com/Ttottoga/BE/assets/86754153/b101821d-df8a-46b2-8f7a-0574a2dccc42)
![image](https://github.com/Ttottoga/BE/assets/86754153/6afe405a-af58-47ef-bac2-0491c2f253d4)

## 서비스 이용 가이드
![image](https://github.com/Ttottoga/BE/assets/86754153/2e8f52eb-1487-42a3-93e8-63cd2a312f9f)

## 팀원 구성

### [제이드/김광현](https://github.com/g00hyun)

### [로빈/김여원](https://github.com/YeowonKIM)

### [루아/오남의](https://github.com/5nam)

### [허찬영/허찬영](https://github.com/nicehcy2)

## 1. Stack Info

- JAVA 17
- SpringBoot 3.2.1
- Build Tool Gradle 0 groovy

### Dependencies

- Spring Web, H2(로컬용), JPA, Lombok, MySql

## 2. 브랜치 전략

![image](https://github.com/Ttottoga/BE/assets/86754153/7de4ebee-ed04-4b53-9460-5cb443927c57)

- `master`는 언제든지 배포가 가능한 상태(릴리즈)
- 새로운 프로젝트는 `develop`을 기반으로 별도 `feature` 브랜치를 생성하여 작업을 진행함
- 브랜치는 로컬에 commit하고, 정기적으로 원격 브랜치에 push함
- 피드백이나 도움이 필요하거나,코드 병합 할 준비가 되었다면 pull request를 만듬
- 다른사람이 변경된 코드를 검토 한 뒤 승인하면 `master`에 병합함
- 병합된 `master`는 즉시 배포할 수 있으며, 배포 해야만 함

## 3. 커밋 컨벤션

![image](https://github.com/Ttottoga/BE/assets/86754153/6c2654d4-38ad-4f7c-b15f-e990528c3a20)
![image](https://github.com/Ttottoga/BE/assets/86754153/9c6bebce-b9da-4f0f-81aa-7aab8a5c025b)

출처 : https://puleugo.tistory.com/165

### 예시(깃모지 사용)

![image](https://github.com/Ttottoga/BE/assets/86754153/ace349f9-85ba-4011-9c63-c155bc85a7f0)

## 4. 코드 컨벤션

### 🐫 함수명, 변수명은 카멜케이스로 작성합니다.

### for문 / if문은 다음과 같이 기입합니다. (한 칸 띄어쓰기)

```java
List<HouseResponseDto> houseResponseDtoList = new ArrayList<>();
        for (House house : findHouses) {
            List<Review> reviewList = reviewRepository.findAllByHouse(house);
            if (reviewList.size() == 0) {
                houseResponseDtoList.add(HouseResponseDto.of(house, 0, 0));
                continue;
            }
           
```

```java
Optional<CardLike> cardLike = cardLikeRepository.findByCardIdAndUserId(id, user.getId());
        if(cardLike.isEmpty()) {
            cardLikeRepository.saveAndFlush(CardLike.of(card.get(), user));
            return ResponseEntity.ok().body(MessageResponseDto.of("좋아요 추가", HttpStatus.OK));
        } else{
            cardLikeRepository.delete(cardLike.get());
            return ResponseEntity.ok().body(MessageResponseDto.of("좋아요 취소", HttpStatus.OK));
        }
```

### 주석 예시

```java
// 댓글 수정
@Transactional
    public ResponseEntity updateComment(Long id, CommentRequestDto commentRequestDto, User user){

        // 해당 id의 댓글이 DB에 있는지 확인
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            throw new IllegalArgumentException("해당 댓글이 존재하지 않습니다.");
        }
```

### 연산자 사이에는 공백을 추가하여 가독성 높이기

```java
a+b+c+d // bad
a + b + c + d // good
```

### 콤마 다음에 값이 올 경우 공백을 추가하여 가독성을 높입니다.

```java
int[] num = {1,2,3,4,5,6,7,8,9}; //bad
int[] num = {1, 2, 3, 4, 5, 6, 7, 8, 9}; //good
```

---

### Refactoring 이후 추가된 부분

1️⃣ **클래스명 뒤에는 한 칸 띄어쓰기, 위에 첫 줄 띄어쓰기 아래는 한 줄 띄어쓰기**

```java
public class AdminController {
                                                                               
    private final AdminService adminService;

    @Operation(summary = "회원가입 API", description = "회원을 등록합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @Secured(PositionEnum.Authority.ADMIN)
    @PostMapping("/signup")
    public SuccessResponse signup(@Valid @RequestBody SignupRequestDto signupRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.signup(signupRequestDto, userDetails.getMember());
    }
                                                                             
}
```

2️⃣ **import 해서 쓸 때는 줄 띄어쓰기 (X)**

```java
private final PostRepository postRepository;
private final CategoryRepository categoryRepository;
private final KeywordRepository keywordRepository;
private final EmitterRepository emitterRepository;
```

3️⃣ **변수 선언 다음 메소드 오면 줄바꿈 해 주세요.**

```java
Optional<Post> findPost = postRepository.findById(id);
                                                                             
        if (findPost.isEmpty()) {
            throw new IllegalArgumentException("해당 게시글이 없습니다.");
        } else if (member.getPosition().getNum() < findPost.get().getModifyPermission().getNum()) {
            throw new IllegalArgumentException("수정 가능한 회원 등급이 아닙니다.");
        }
```

4️⃣ **else if는 앞에만 띄어쓰기 해 주세요.**

```java
if(post.isEmpty()) {
            throw new IllegalArgumentException("해당 게시글이 없습니다.");
        } else if(member.getPosition().getNum() < post.get().getReadablePosition().getNum()){
            throw new IllegalArgumentException("읽기 가능한 회원 등급이 아닙니다.");
        }
```

5️⃣ **“}” 사이의 return문은 띄어쓰기를 해 주세요.**

```java
	  }
                                                                         
    return key.toString();
}
```

6️⃣ **메서드 명과 다음 줄은 띄어쓰기 해 주세요.**

```java
@Transactional
    public SuccessResponse createBookMarkFolder(String folderName, Member member){
                                                                                          
        Optional<Member> findMember = memberRepository.findById(member.getId());
		...
		}
```

## 5. 프로젝트 구조

```
ttg
    ├── TtgApplication.java
    ├── domain
    │   ├── coupon
    │   │   ├── api
    │   │   │   └── CouponController.java
    │   │   ├── application
    │   │   │   ├── CouponService.java
    │   │   │   └── CouponServiceImpl.java
    │   │   ├── dto
    │   │   │   ├── CouponResponseDto.java
    │   │   │   ├── MyPageCouponResponseDTO.java
    │   │   │   └── converter
    │   │   │       └── CouponConverter.java
    │   │   ├── entity
    │   │   │   └── Coupon.java
    │   │   ├── exception
    │   │   │   └── handler
    │   │   │       └── CouponHandler.java
    │   │   ├── repository
    │   │   │   └── CouponRepository.java
    │   │   └── utils
    │   │       ├── BitMatrixToMultipartFileConverter.java
    │   │       ├── CustomMultipartFile.java
    │   │       └── QrCodeGenerator.java
    │   ├── member
    │   │   ├── api
    │   │   │   └── MemberController.java
    │   │   ├── application
    │   │   │   ├── MemberCommandService.java
    │   │   │   ├── MemberCommandServiceImpl.java
    │   │   │   ├── MemberQueryService.java
    │   │   │   ├── MemberQueryServiceImpl.java
    │   │   │   ├── MemberService.java
    │   │   │   ├── MemberServiceImpl.java
    │   │   │   └── OAuth2MemberServiceImpl.java
    │   │   ├── dto
    │   │   │   ├── MemberImageRequestDTO.java
    │   │   │   ├── MemberImageResponseDTO.java
    │   │   │   ├── MyPageAllResponseDto.java
    │   │   │   ├── MyPageMemberResponseDTO.java
    │   │   │   └── converter
    │   │   │       └── MemberConverter.java
    │   │   ├── entity
    │   │   │   ├── CustomOAuth2Member.java
    │   │   │   ├── HeartStore.java
    │   │   │   └── Member.java
    │   │   ├── exception
    │   │   │   └── handler
    │   │   │       └── MemberHandler.java
    │   │   └── repository
    │   │       ├── HeartStoreRepository.java
    │   │       └── MemberRepository.java
    │   ├── review
    │   │   ├── api
    │   │   │   └── ReviewController.java
    │   │   ├── application
    │   │   │   ├── ReviewCommandService.java
    │   │   │   └── ReviewCommandServiceImpl.java
    │   │   ├── dto
    │   │   │   ├── MyPageReviewResponseDTO.java
    │   │   │   ├── ReviewRegisterRequestDTO.java
    │   │   │   └── ReviewRegisterResponseDTO.java
    │   │   ├── entity
    │   │   │   ├── Review.java
    │   │   │   └── ReviewStatus.java
    │   │   ├── exception
    │   │   │   └── handler
    │   │   │       └── ReviewHandler.java
    │   │   └── repository
    │   │       └── ReviewRepository.java
    │   └── store
    │       ├── api
    │       │   └── StoreController.java
    │       ├── application
    │       │   ├── StoreCommandService.java
    │       │   ├── StoreCommandServiceImpl.java
    │       │   ├── StoreQueryService.java
    │       │   └── StoreQueryServiceImpl.java
    │       ├── dto
    │       │   ├── HeartStoreResponseDto.java
    │       │   ├── HomeResponseDto.java
    │       │   ├── MyPageStoreResponseDto.java
    │       │   ├── StoreFindResponseDto.java
    │       │   ├── StoreRequestDto.java
    │       │   ├── StoreResponseDto.java
    │       │   ├── StoreResultResponseDto.java
    │       │   └── converter
    │       │       └── StoreConverter.java
    │       ├── entity
    │       │   ├── Menu.java
    │       │   ├── Region.java
    │       │   └── Store.java
    │       ├── exception
    │       │   └── handler
    │       │       └── StoreHandler.java
    │       └── repository
    │           ├── MenuRepository.java
    │           ├── RegionRepository.java
    │           └── StoreRepository.java
    └── global
        ├── auth
        │   ├── WebSecurityConfiguration.java
        │   ├── dto
        │   │   ├── KakaoAccessTokenInfoResponseDto.java
        │   │   └── NaverAccessTokenInfoResponseDto.java
        │   ├── handler
        │   │   └── OAuth2SuccessHandler.java
        │   └── jwt
        │       ├── JwtAuthenticationFilter.java
        │       ├── JwtProvider.java
        │       └── TokenParser.java
        ├── common
        │   ├── AwsS3.java
        │   ├── BaseResponseDto.java
        │   └── ResponseCode.java
        ├── config
        │   └── AwsS3Config.java
        ├── error
        │   ├── GeneralException.java
        │   └── handler
        │       ├── AwsS3Handler.java
        │       └── MasterExceptionHandler.java
        ├── infra
        │   └── infra.java
        └── util
            ├── AwsS3Service.java
            └── Time.java
```
