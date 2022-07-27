## Post 방식의 application/x-www-form-urlencoded 을 컨트롤러에서 받는 방법

```java
@PostMapping("/posts")

1. @RequestParam
public String post(@RequestParam String title, @RequestParam String content) {}

2. @RequestParam + Map
public String post(@RequestParam Map<String, String> params){
  String title = params.get("title")
}

3. @ModelAttribute(생략가능)
public Stirng post(@ModelAttribute PostCreateDto params){}


cf. Json 받기
public String post(@RequestBody PostCreateDto params){}
```



Key, value 방식의 한계- 글 제목, 글 내용, 사용자(id, name, level)을 전송할 때.. (데이터를 풀어서 보내야됨)

```
title=xx&content=xx&userId=xx&userName=xx&userLevel=xx
```

반면 Json으로 보내게 된다면

```json
{
  "title": "xxx",
  "content": "xxx",
  "user": {
	    "id" : "xxx",
  	  "name":"xxx",
    	"level":"xxx"
  }
}
```











## Test

```java
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Slf4j
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("/posts 요청시 Hello World를 출력한다.")
    void test() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"))
                .andDo(print()); // http 요청 로그 남기기

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "글 제목입니다.")
                        .param("content", "글 내용입니다.")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"))
                .andDo(print()); // http 요청 로그 남기기

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("title", "글 제목입니다.")
                        .param("content", "글 내용입니다.")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("json의 title 검증")) // 구글링해서 찾아보기
                .andDo(print()); // http 요청 로그 남기기
    }
}
```







## 데이터를 검증하는 이유

1. client 개발자가 실수했을 수 있다.
2. Client bug로 값이 누락될 수 있다.
3. 외부에 나쁜 사람이 값을 임의로 조작해서 보낼 수 있다.
4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
5. 서버 개발자의 편안함을 위해서 -> 처음부터 검증하고 로직 시작







## BindingResult

```java
@PostMapping("/signup")
public Map<String, String> signup(@RequestBody @Valid RequestSignupForm form, BindingResult result) {
  if(result.hasErrors()){
    List<FieldError> fieldErrors = result.getFieldErrors();
    FieldError firstFieldError = fieldErrors.get(0);
    String fieldName = firstFieldError.getField(); // title
    String errorMessage = firstFieldError.getDefaultMessage();// 에러 메시지

    Map<String, String> error = new HashMap<>();
    error.put(fieldName, errorMessage);
    return error;
  }
  ...
}
```

1. 응답값에 HashMap -> 응답 클래스를 만들어서, 분리하는게 좋다.
2. 세 번이상의 반복적인 작업은 피해야 한다. -> 다른 방법이 있지 않을까?







### 빌더

1. 가독성에 좋다.
2. 필요한 값만 받을 수 있다.

클래스 위 말고, 직접 생성자 만든 후 그 위에 다는거 추천







### Post요청시 return body에 데이터를 주는 경우가 적다.

물론 이를 요청하는 client 개발자도 존재. 예를 들어 회원가입을 한 경우, 회원ID를 리턴해달라던지..

안좋은 케이스는 "서버에서 반드시 이렇게 할겁니다!" 라고 fix 해버리는거! 서버에서는 유연하게 대응할 수 있도록 코드를 짜야한다. 실제로 한 번에 잘 처리되는 케이스는 없다. 잘 관리하는 형태가 중요하다.

Petch도 대부분 주진 않지만, 클라이언트의 요구에 따라 코딩하면 된다.





### Entity 클래스 내부에 서비스 정책은 넣지 않도록 한다.

절대로 서비스 정책이 entity에 영향을 주지 않도록 해야 한다.





### 응답을 위한 Response 객체는 어디서 변환해야 하는가?

의견이 좀 갈리는듯. 서비스 계층에서 변환 후 리턴이 맞는가? 아니면 controller에서 변환을 하는게 맞는가?

호돌맨의 경우, 서비스가 작은 땐 service에서 변환 후 리턴함.



### Request DTO, Response DTO는 분리해주자

Request DTO에는 validation을, Response DTO에는 서비스 정책을 녹여주자





### findAll 후 class DTO로 변환

```java
public List<PostResponse> getList(){
    return postRepository.findAll().stream()
            .map(post -> PostResponse.builder()
                 .id(post.getId())
                 .title(post.getTitle())
                 .content(post.getContent())
                 .build())
            .collect(Collectors.toList());
}


만약 해당 DTO가 반복 사용된다면, 클래스 내부에 entity를 받는 생성자를 만들어서 사용할 수 있음.  
public class PostResponse{
  ...
    // 생성자 오버로딩
    public PostResponse(Post post){
    	this.title = post.getTitle();
    	this.content = post.getContent();
  	}
}
```







### saveAll할 때는 List로

```java
repository.save(List.of(user1, user2))
```







### FindAll의 문제점

1. 데이터가 너무 많은 경우, 비용이 너무 많이 든다.
2. 데이터가 10000000개라면, DB를 모두 조회했다간 DB가 뻗을 수 있다.
3. DB에서 서버로 전달하는 시간, 트래픽 비용 등이 많이 발생할 수 있다.

4. 그래서 실제로도 전부 조회하는 경우는 많지 않다.



**해결법 : 페이징 처리**

```java
// /posts?page={페이지번호}&sort=id,desc&size=5
// 기본값(@PageableDefault, 파라미터로 수정 가능): sort는 오름차순, 한 페이지에 10개 출력
// 주로 size는 서버에서 세팅하는 경우가 많다.
@GetMapping("/posts")
public List<PostResponse> getList(Pageable pageable) {
  return postService.getList(pageable);
}

/*
관련 설정
spring.data.web.pageable.one-indexed-parameters: true # 페이지 번호 1부터 시작
spring.data.web.pageable.default-page-size: 5 # 한 페이지에 몇 개?
*/

public class postService{
  // jpa의 pageable 사용하기(spring data jpa domain?)
  
  public List<PostResponse> getList(int page){
    // 한 페이지당 넘어올 데이터 개수 = 5
    // 페이지는 0부터 계산이 됨
    // Sort.by(정렬방식, 정렬기준)
    // Sort.by(정렬기준): 기본적으로 오름차순으로 정렬
    Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "id"));
    return postRepository.findAll(pageable).stream()
            //.map(post -> PostResponse(post.getId(), post.getTitle(), post.getContent())
            .map(PostResponse::new)
            .collect(Collectors.toList());
  }
  
  public List<PostResponse> getList(Pageable pageable){
    // 한 페이지당 넘어올 데이터 개수 = 5
    // spring.data.web.pageable.one-indexed-parameters: true
    //                   -> Controller에서 @PageableDefault로 받을 때 index 자동 수정
    // !web!에서 넘어올 때 page-1로 받아줌. 직접 숫자 써서 사용하면 작동 안함
    return postRepository.findAll(pageable).stream()
            //.map(post -> PostResponse(post.getId(), post.getTitle(), post.getContent())
            .map(PostResponse::new)
            .collect(Collectors.toList());
  }
}

List<Post> requestPosts = IntStream.range(0, 30)
  .mapToObj(i -> {
    return new Post("title"+i, "content"+i)
      .collect(Collector.toList());
  });
  
repository.saveAll(requestPosts);

List<PostResponse> posts = postService.getList(pageNum);
```



**QueryDsl을 이용한 페이징**

```java
@Data
@Builder
public class PostSearch {
  
  private static final int MAX_SIZE = 2000;
  
  @Builder.Default // 빌더를 사용할 때 값이 안들어오면 default 값 사용
  private Integer page = 1;
  
  @Builder.Default
  private Integer size = 10;
  
  public long getOffset(){
    return (long) (Math.max(1, page) - 1) * Math.min(size, MAX_SIZE);
  }
}
```

```java
@GetMapping("/post")
// /post?page=1&size=10
public List<PostResponse> paging(@ModelAttribute PostSearch postSearch){
  return postService.getList(postSearch)
}
```

```java
public class PostService {
  ...
  public List<PostResponse> getList(PostSearch postSearch){
  		return postRepository.getList(postSearch).stream()
        			.map(PostResponse::new)
        			.collect(Collectors.toList());
  }
}
```

```java
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<User> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(QPost.post)
                .limit(postSearch.getSize())
          			.orderBy(QPosst.post.id.desc())
                .offset(postSearch.getOffset())
                .fetch();
    }
}
```

```java
public interface UserRepositoryCustom {
    public List<User> getList(int page);
}
```

```java
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom { }
```







### Entity 수정할 때, Builder 이용하는 방법

https://www.inflearn.com/course/%ED%98%B8%EB%8F%8C%EB%A7%A8-%EC%9A%94%EC%A0%88%EB%B3%B5%ED%86%B5-%EA%B0%9C%EB%B0%9C%EC%87%BC/unit/111160?tab=community&q=584082

15분?
