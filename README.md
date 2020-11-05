# 커뮤니티 웹사이트 개발
웹 프로젝트 주소: http://ec2-3-34-115-249.ap-northeast-2.compute.amazonaws.com:8080/

## 1. 개발환경
프론트엔드
- HTML
- CSS
- JavaScript 

백엔드
- SpringBoot
- Spring Data JPA
- Spring Security
- Java 11

## 2. AWS EC2에 배포
 - Travis CI를 Github에 연동하여 master브랜치로 push할 경우 자동으로 빌드하도록 설정
 - 빌드 후 생성된 .jar 파일을 AWS S3에 저장
 - S3에 있는 .jar 파일을 CodeDeploy에 배포 요청
 - EC2에 배포


## 3. 프로젝트 설명
게시글, 댓글의 CRUD를 제공하는 게시판 서비스

## 4. 구현
### 4.1 Controller의 핸들러별로 통합테스트 작성
```java

// import 생략 ....

@MockMvcTest
public class SignUpTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountRepository accountRepository;

    // 생략 ...

    @DisplayName("회원 가입 처리 - 입력값 오류")
    @ParameterizedTest(name = "{index} {displayName} nickname={0} email={1} password={2}")
    @CsvSource({
            "'', 'twins1@gmail.com', '12345678'",
            "'as', 'twins2@gmail.com', '12345678'",
            "'twins', '', '12345678'",
            "'twins', 'twins1@gmail.com', '12345'",
    })
    public void signUpSubmit_with_wrong_input(@AggregateWith(SignUpFormAggregator.class) SignUpForm signUpForm) throws Exception {
        // given
        String requestUri = "/sign-up";

        // when
        ResultActions resultActions = mockMvc.perform(post(requestUri)
                .param("nickname", signUpForm.getNickname())
                .param("email", signUpForm.getEmail())
                .param("password", signUpForm.getPassword())
                .with(csrf()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("account/sign-up"))
                .andExpect(unauthenticated());
    }

    // 생략 ...
}
```

### 4.2 인증 된 사용자가 접근할 수 있는 기능을 테스트하기 위해 @WithSecurityContext를 이용해 커스텀 어노테이션 구현
```java
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithAccountSecurityContextFactory.class)
public @interface WithAccount {

    String value();
}
```

```java
@RequiredArgsConstructor
public class WithAccountSecurityContextFactory implements WithSecurityContextFactory<WithAccount> {

    private final AccountService accountService;

    @Override
    public SecurityContext createSecurityContext(WithAccount withAccount) {
        String nickname = withAccount.value();

        // 회원 가입
        SignUpForm signUpForm = SignUpForm.builder()
                .nickname(nickname)
                .email(nickname + "@gmail.com")
                .password("12345678")
                .build();
        accountService.processNewAccount(signUpForm);

        // Authentication 객체를 SecurityContext에 넣어주기
        UserDetails principal = accountService.loadUserByUsername(nickname);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal, principal.getPassword(), principal.getAuthorities()
        );
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
        return context;
    }
}
```

### 4.3 QueryDsl을 사용하여 게시글 조건에 따른 검색 및 페이징 처리
```java
import static me.weekbelt.community.modules.account.QAccount.account;
import static me.weekbelt.community.modules.board.QBoard.board;

@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Board> findByBoardSearch(BoardSearch boardSearch, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (isTotalSearch(boardSearch)) {
            builder.and(board.title.containsIgnoreCase(boardSearch.getKeyword()));
            builder.or(board.account.nickname.containsIgnoreCase(boardSearch.getKeyword()));
        } else if (isTitleSearch(boardSearch)) {
            builder.and(board.title.containsIgnoreCase(boardSearch.getKeyword()));
        } else if (isNicknameSearch(boardSearch)) {
            builder.and(board.account.nickname.containsIgnoreCase(boardSearch.getKeyword()));
        }

        if (!boardSearch.getBoardType().equals("ALL")) {
            builder.and(board.boardType.eq(BoardType.valueOf(boardSearch.getBoardType())));
        }

        QueryResults<Board> boardQueryResults = jpaQueryFactory
                .selectFrom(board)
                .join(board.account, account).fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(builder)
                .orderBy(board.id.desc())
                .fetchResults();

        List<Board> boardList = boardQueryResults.getResults();
        return new PageImpl<>(boardList, pageable, boardQueryResults.getTotal());
    }

    // 게시글 제목, 작성자 둘우 다 검색하는 경우
    private boolean isTotalSearch(BoardSearch boardSearch) {
        return boardSearch.getSearchCondition() != null &&
                SearchCondition.valueOf(boardSearch.getSearchCondition()) == SearchCondition.TOTAL;
    }
    
    // 게시글 제목으로 검색하는 경우
    private boolean isTitleSearch(BoardSearch boardSearch) {
        return boardSearch.getSearchCondition() != null &&
                SearchCondition.valueOf(boardSearch.getSearchCondition()) == SearchCondition.TITLE;
    }
    
    // 게시글 작성자로 검색하는 경우
    private boolean isNicknameSearch(BoardSearch boardSearch) {
        return boardSearch.getSearchCondition() != null &&
                SearchCondition.valueOf(boardSearch.getSearchCondition()) == SearchCondition.NICKNAME;
    }

}

```
