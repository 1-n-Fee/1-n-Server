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
public ResponseEntity<?> signup(@RequestBody @Valid RequestSignupForm form, BindingResult result) {
  /**
         * 인가 코드 사용해서 oAuth 로그인 해줘야?
         */
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





1. 매번 메서드마다 값을 검증 해야한다. 
2. 응답값에 HashMap -> 응답 클래스를 만들어주는게 좋다.
3. 여러개의 에러 처리는 힘들다
4. 세 번이상의 반복적인 작업은 피해야 한다. -> 다른 방법이 있지 않을까?







### 빌더

1. 가독성에 좋다.
2. 필요한 값만 받을 수 있다.

클래스 위 말고, 직접 생성자 만든 후 그 위에 다는거 추천







### Post요청시 return body에 데이터를 주는 경우가 적다.

물론 이를 요청하는 client 개발자도 존재. 예를 들어 회원가입을 한 경우, 회원ID를 리턴해달라던지..

안좋은 케이스는 "서버에서 반드시 이렇게 할겁니다!" 라고 fix 해버리는거! 서버에서는 유연하게 대응할 수 있도록 코드를 짜야한다. 실제로 한 번에 잘 처리되는 케이스는 없다. 잘 관리하는 형태가 중요하다.







