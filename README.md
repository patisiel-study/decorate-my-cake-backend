# 내 케이크를 꾸며줘!(Decorate my cake)
<img width="345" alt="image" src="https://github.com/patisiel-study/decorate-my-cake-backend/assets/84646738/53624bfe-6993-48fd-b829-739e0d834caf">

온라인 롤링 페이퍼 서비스 내 트리를 꾸며줘!를 모티브로 클론코딩 프로젝트를 진행함

## Intro

- 개발 기간
    - 2024.04 ~ 07
- 개발 인원: 4
    - Front-End: 2
    - Back-End: 2
- 배포 링크
- Github 링크
    - [Front-End](https://github.com/patisiel-study/decorate-my-cake-frontend)
    - [Back-End](https://github.com/patisiel-study/decorate-my-cake-backend)

 ## 🔥 Project

### 1.  소개

- 다른 사람의 생일을 축하하기 위해 케이크를 꾸미고, 캔들(메시지)을 작성하면, 생일 주인공이 당일 날 받아 보고 링크를 공유할 수 있는 온라인 롤링페이퍼 프로젝트
- 내 케이크 페이지 및 캔들에 대한 열람 권한을 설정하여 권한없는 유저의 열람을 방지
- 친구추가 기능을 통해 다가오는 친구들의 생일을 미리 파악할 수 있음

### 2. 사용 스택

FRONT-END

- React

BACK-END

- Springboot, Spring Security, JWT, PostgreSQL, Redis

DEPLOY

- EC2, Docker, S3

TOOL

- Notion, Figma, Github

### 3. 시스템 아키텍처

<img width="470" alt="image" src="https://github.com/patisiel-study/decorate-my-cake-backend/assets/84646738/bebf08b6-3278-4422-8738-2adc92e73219">


- Docker-compose를 활용한 컨테이너 관리 및 개발 환경 통일
- 로그인 및 api 사용시 발생하는 토큰 검증 및 갱신 기능을 redis에서 전담하도록 역할 분리
- S3 버킷을 활용한 이미지 관리
    - 해당 이미지의 URL만 PostgreSQL에서 관리하고 프론트로 반환할 수 있도록 구현
- 백엔드 및 프론트엔드 CI 파이프라인 구축
    - BE: IntelliJ를 통한 개발 후 Git push, Gradle 빌드, Docker 이미지 생성, Docker Hub 게시
    - FE: VScode를 통한 개발 후 Git push, Docker 이미지 생성, Docker Hub 게시

### 4. 플로우 차트

<img width="847" alt="image" src="https://github.com/patisiel-study/decorate-my-cake-backend/assets/84646738/29190b08-a9f3-422d-95c3-200e64b4cdd1">


### 5. ERD

<img width="849" alt="image" src="https://github.com/patisiel-study/decorate-my-cake-backend/assets/84646738/9d270249-1d8a-4dd3-aafd-12e9daa8fe17">


## 👥 협업 플로우

Swagger-api를 활용하여 프론트엔드에게 api 명세서를 작성하여 공유

<img width="850" alt="image" src="https://github.com/patisiel-study/decorate-my-cake-backend/assets/84646738/90eb9f6a-c283-469e-b2dd-b1404f2d5098">

<img width="850" alt="image" src="https://github.com/patisiel-study/decorate-my-cake-backend/assets/84646738/f45accfc-2a7b-43d8-9327-4ae7e5b03101">


Notion 및 Github Issue를 활용하여 프론트엔드 팀원과 소통

<img width="647" alt="image" src="https://github.com/patisiel-study/decorate-my-cake-backend/assets/84646738/33e8b7ec-1cf6-4917-8bb1-9c54220b6323">
<img width="855" alt="image" src="https://github.com/patisiel-study/decorate-my-cake-backend/assets/84646738/b7f29978-2c9c-458d-ab46-6051699b8dd1">


## 📌 나의 역할

- 케이크 관련 CRUD 기능
- 권한에 따른 케이크 및 캔들 열람 기능 및 페이징 처리
- 로그인/회원 가입 인증 및 토큰 검증과 갱신 처리
- 친구 요청 및 관리 기능 구현
- 사용자의 개인 설정 수정 기능 구현
- 배포 및 환경 구축

## 📌 트러블 슈팅

- **RT 저장소의 성능 최적화를 위한 Redis 도입**
    
    JWT 인증 방식을 도입하면서 리프레시 토큰(이하 RT)을 postgreSQL에 저장하였으나 다음과 같은 문제점이 있었다.
    
    - 성능 문제: RT 검증 과정에서 DB에 빈번한 읽기/쓰기 작업이 발생하면서 성능 저하가 발생할 수 있었다.
    - 확장성 문제: 사용자가 증가할수록 DB에 부하가 증가하여 확장성 문제가 나타날 가능성이 있었다.
    
    이러한 문제를 해결하기 위해 Redis를 도입하여 RT를 저장하고 관리하는 방식으로 토큰 검증 및 갱신에 대한 과정을 기존 postgreSQL에서 분리하는 방식으로 개선하였다.
    
    - Redis는 인메모리 DB로서 매우 빠른 속도의 읽기/쓰기 작업이 가능하므로 가장 부하가 많이 일어날 것으로 예상되는 인증 기능을 Redis로 분리하였다.
    - TTL(Time To Live) 기능을 활용하여 RT의 만료 시간을 자동으로 관리하도록 하였고 이를 통해 불필요한 토큰이 DB에 남아있지 않도록 방지하였다.
- **친구 요청 및 관리 기능 구현**
    
    **문제 1: DB 설계**
    일반적인 팔로잉 방식보다는 요청과 수락으로 인한 친구관계가 양방향으로 맺어지는 구조로 구현하고자 했다. 이에 따라 친구 에 대한 수락/거절/대기/ 상태를 표현하고 사용자가 보낸 요청과 받은 요청을 명확히 구분할 수 있는 구조가 필요했다.
    
    **해결방법**
    
    <img width="283" alt="image" src="https://github.com/patisiel-study/decorate-my-cake-backend/assets/84646738/b70bc370-ded3-4ef2-9d3f-b28af5857b8d">

    
    ```java
    @Entity
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public class FriendRequest {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
    
        @ManyToOne
        @JoinColumn(name = "receiver_id")
        private Member receiver;
    
        @ManyToOne
        @JoinColumn(name = "sender_id")
        private Member sender;
    
        @Enumerated(EnumType.STRING)
        private FriendRequestStatus status;
    
        private String message;
    
        private String profileImg;
    }
    
    ```
    
    FriendRequest 엔티티를 설계하여 요청자(sender)와 수신자(receiver)를 저장하고, 상태(status) 필드를 추가하여 요청 상태(PENDING, ACCEPTED, REJECTED, DELETED)를 Enum으로 관리했다. 이를 통해 데이터의 일관성을 유지하면서도 다양한 상태를 효과적으로 관리할 수 있었다.
    
    **문제2: 양방향 매핑 및 성능 문제**
    
    초기 구현에서는 사용자와 친구 요청 간의 양방향 매핑을 설정했으나, 사용자 정보를 조회할 때 친구 요청 정보까지 모두 가져오는 N+1 문제가 발생했다.
    
    해결 방법
    
    양방향 대신 단방향 매핑을 사용하고, fetch 전략을 LAZY로 설정하여 필요한 경우에만 친구 요청 정보를 로드하도록 수정했다. 이를 통해 불필요한 데이터 로드를 줄이고 성능을 향상시킬 수 있었다.
    
    ```java
    @Entity
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public class Member implements UserDetails {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "member_id", nullable = false)
        private Long memberId;
    
        ...
        
        @ElementCollection(fetch = FetchType.EAGER)
        @Builder.Default
        private List<String> roles = new ArrayList<>();
    
        @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
        private List<Cake> cakes = new ArrayList<>();
    
       ...
    
    }
    
    ```
    
    **문제3: 동시성 문제**
    
    여러 사용자가 동시에 친구 요청을 보내거나 수락할 때 동시성 문제가 발생할 수 있었다. 예를 들어, 동일한 요청에 대해 중복 수락이 발생하거나, 이미 처리된 요청을 다시 처리하는 상황이 발생할 수 있다.
    
    해결 방법
    
    JPA의 `@Lock` 어노테이션을 사용하여 PESSIMISTIC_WRITE 잠금을 설정했다. 이를 통해 동시성 문제를 방지하고 데이터의 무결성을 유지할 수 있었다.
    
    비관적 잠금(Pessimistic Lock)은 주로 데이터의 일관성을 보장하기 위해 쓰기 작업(수정, 삭제 등)에 사용된다. 
    
    읽기 작업에 비관적 잠금을 적용하면 성능 저하를 초래할 수 있기 때문에, 쓰기 작업이 발생하는 메서드에만 비관적 잠금을 적용했다. 
    
    ```java
    public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
        @Lock(LockModeType.PESSIMISTIC_WRITE)
        Optional<FriendRequest> findBySenderAndReceiver(Member sender, Member receiver);
    
        @Query("SELECT fr FROM FriendRequest fr WHERE (fr.receiver = :member OR fr.sender = :member) AND fr.status = 'ACCEPTED'")
        List<FriendRequest> findAcceptedFriendRequestsByMember(@Param("member") Member member);
    
        List<FriendRequest> findByReceiverAndStatus(Member receiver, FriendRequestStatus status);
    
        @Lock(LockModeType.PESSIMISTIC_WRITE)
        Optional<FriendRequest> findBySenderAndReceiverAndStatus(Member sender, Member receiver, FriendRequestStatus status);
    }
    
    ```
    
     이를 통해 데이터의 일관성을 보장하면서도 성능 저하를 최소화할 수 있었다.
    
    **문제4: 친구 요청 중복 문제 해결**
    
    친구 요청의 상태 변경 로직을 구현하는 과정에서 여러 가지 예외 상황을 처리해야 했다. 
    
    예를 들어, 이미 친구 관계인 사용자가 다시 친구 요청을 보낼 경우, 또는 거절된 요청을 다시 승인 대기 상태로 변경할 때 예외 상황이 발생할 수 있었다.
    
    **해결 방법**
    
    비즈니스 로직에서 각 상태에 대한 검증 로직을 추가하여 예외 상황을 명확히 처리하고자 했다. 
    
    또한, 상태 변경 메서드를 FriendRequest 엔티티 내에 구현하여 엔티티 자체에서 상태를 관리하도록 했다.
    
    ```java
    @Entity
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public class FriendRequest {
    		...
        public FriendRequest updateToPending(String message) {
            if (this.status != FriendRequestStatus.REJECTED && this.status != FriendRequestStatus.DELETED) {
                throw new IllegalStateException("Only rejected friend requests can be updated to pending.");
            }
            return FriendRequest.builder()
                    .id(this.id)
                    .sender(this.sender)
                    .receiver(this.receiver)
                    .status(FriendRequestStatus.PENDING)
                    .message(message)
                    .profileImg(this.profileImg)
                    .build();
        }
    
        public FriendRequest acceptRequest() {
            if (this.status != FriendRequestStatus.PENDING) {
                throw new IllegalStateException("Only pending friend requests can be accepted.");
            }
            return FriendRequest.builder()
                    .id(this.id)
                    .sender(this.sender)
                    .receiver(this.receiver)
                    .status(FriendRequestStatus.ACCEPTED)
                    .message(this.message)
                    .profileImg(this.profileImg)
                    .build();
        }
    
        public FriendRequest rejectRequest() {
            if (this.status != FriendRequestStatus.PENDING) {
                throw new IllegalStateException("Only pending friend requests can be rejected.");
            }
            return FriendRequest.builder()
                    .id(this.id)
                    .sender(this.sender)
                    .receiver(this.receiver)
                    .status(FriendRequestStatus.REJECTED)
                    .message(this.message)
                    .profileImg(this.profileImg)
                    .build();
        }
    
        public FriendRequest deleteRequest() {
            if (this.status != FriendRequestStatus.ACCEPTED) {
                throw new IllegalStateException("Only accepted friend requests can be deleted.");
            }
            return FriendRequest.builder()
                    .id(this.id)
                    .sender(this.sender)
                    .receiver(this.receiver)
                    .status(FriendRequestStatus.DELETED)
                    .message(this.message)
                    .profileImg(this.profileImg)
                    .build();
        }
    
    }
    
    ```
    
- **이미지 불러오는 속도 개선**
    
    프로젝트에서 사용자 경험을 향상시키기 위해 Member 프로필 사진 업로드 기능을 구현했다. 
    
    초기 구현에서는 사용자가 업로드한 이미지를 그대로 저장하고 제공했다. 하지만, 원본 이미지가 고해상도이거나 크기가 큰 경우 로드 시간이 길어지는 문제가 발생했다.
    
    해결 방법
    
    이미지 로드 시간을 개선하기 위해 리사이징 과정을 거쳐 이미지 파일을 적절한 크기로 줄여 S3 버킷에 업로드하고 해당 이미지의 url을 프론트로 제공하도록 했다.
    
    추가로 업로드 할 수 있는 파일의 확장자를 jpeg와 png로 제한하여 엉뚱한 파일을 업로드하지 못하도록 예외처리하였다.
