package org.sparta.memo_spring.controller;


import com.sparta.memo_spring.entity.Memo;
import org.sparta.memo_spring.dto.MemoRequestDto;
import org.sparta.memo_spring.dto.MemoResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MemoController {

    private final Map<Long, Memo> memoList = new HashMap<>(); // 데이터 저장소 대신 사용
                                                              // long에 유일한 식별자 id 주어 간편하게 memo를 구분하려고 넣기
                                                              // Memo엔 실제 데이터(메모 객체)를 넣어주기

    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) { // 데이터는 body 부분에 json형태로 넘어올것임 - RequestBody
                                                                                // 반환타입 : MemoResponseDto
        // RequestDto -> Entity (저장하기 위해) - Entity 클래스로 바꿔주는 과정 (24~26열)
        Memo memo = new Memo(requestDto); // () 안에 없으면 데이터가 하나도 들어가지 않죠? -> 클라이언트가 보내준 데이터 requestDto를 넣기
                                          // 빨간 줄이 뜰텐데 기본 생성자가 없기 때문에 뜨는 것임 -> Memo 클래스에서 생성자 만들어주기(Memo - 16열) -> alt + enter - new constructor


        // Memo Max ID Check
        // Memo 필드의 id 값으로 메모를 구분할 것임 - 중복이 되면 안됨
        // 현재 데이터베이스에 가장 마지막 값을 구해서 +1을 하면 Max id를 만들어낼 수 있다
        // 아직 DB를 저장하고 잇는 방법을 모르니 java 컬렉션(map)으로 구현을 해보겠다 - 19열애서 map 객체 생성
        Long maxId = memoList.size() > 0 ? Collections.max(memoList.keySet()) + 1 : 1; // Collections.max 메소드 사용
                                                                                       // memoList.keySet()을 호출하면 19열 Long 에 들어있는 값을 다 가져오고 그 중에 가장 큰 값을 가져온다.
                                                                                       // 가장 큰 값에 1을 더해주고 아니라면 1을 반환해준다는 의미 - 1은 memoList 자료구조에 데이터가 하나도 없다는 뜻
                                                                                       // 하나도 없기 때문에 중복이 될 일이 없으니 1번부터 시작해라~ 라는 것이다
         memo.setId(maxId); // memo 클래스의 id에 업데이트된 maxid를 set해주기

        // DB 저장
        memoList.put(memo.getId(), memo); // key값에 memo.getId() - Memo 클래스 보면 생성자 안에 id는 없음 key값으로 식별할 수 있는 id값을 넣어준 것임
                                          // value 값에 27열 memo 객체 넣기

        // Entity(memo) -> ResponseDto 로 바꾸기
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);

        return memoResponseDto; // 여기까지 하면 메모 생성하기 api가 하나 만들어진 것임
    }

    // read 구현
    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {  // List<MemoResponseDto>인 이유 : 메모가 하나일 수도 있고 여러 개일 수도 있기 때문에 list형태로 반환하게끔 함
        // Map To List
        List<MemoResponseDto> responseList = memoList.values().stream() // db용으로 사용하는 memoList에 .values하면 18열 Memo의 값을 모두 가지고 온다
                                                                        // .stream()로 인해 values()로 나온 여러 개의 데이터들을 for문처럼 돌려줌
                .map(MemoResponseDto::new).toList();    // 위로 인해 18열 Memo가 하나씩 튀어나올 거 아님? .map하면 Memo를 가지고 '변환'을 할 수 있음
                                                        // 어떤 것을 변환할거냐면 MemoResponseDto를 객체로 변환해야돼서 'MemoResponseDto::new'하면 MemoResponseDto 클래스의 생성자가 호출이 된다
                                                        // stream으로 인해 하나씩 튀어나오는게 Memo였죠? 그러니까 'new' 생성자를 찾을 수 있는 것임. .toList()를 통해 리스트로 만들어주기
        return responseList; // 여기까지 조회하는 api 완료
    }

   // update 구현
    @PutMapping("/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) { // 업데이트한 id만 넘겨줄거라 반환타입은 long type으로
                                                                                            // 63열 {id} 데이터를 받아오기 위해 '@PathVariable Long id'
                                                                                            // {id} 데이터와 값을 수정해야하니 데이터에 대한 내용(컨텐츠)가 같이 넘어온다 - boby 부분에서 json형식으로 넘어오니 ' @RequestBody', 그리고 만들어둔 MemoRequestDto를 통해 받아오기
                                                                                            // 즉, 수정할 내용 @RequestBody로 받아온다는 뜻
        // 해당 메모가 DB에 존재하는지 확인
        if(memoList.containsKey(id)) { // .containsKey() : 18열 map의 자료구조에서 key부분(Long)에 해당하는 값이 있는지 확인하는 거
            // 해당 메모 가져오기
            Memo memo = memoList.get(id); // 받아온 id를 넣어주면 현재 우리의 데이터 베이스(18열) memoList 안에 해당하는 메모가 있는지 없는지 확인 후 반환 타입 bollean이라 있으면 true 없으면 false로 반환
                                          // id를 넣으면 long에 넣어줬던 id값이랑 매치해서 그 id값에 들어있는 value부분의 Memo객체가 반환이 될 것임(18열 참조)
            // memo 수정
            memo.update(requestDto); // 수정할 내용을 파라미터로 전달해줘야 됨 - requestDto
                                     // .update는 안 만들었으면 빨간색이 뜰텐데 Memo 클래스 가서 update 생성자 만들거나 여기서 alt +ins : 생성자 만들기
            return memo.getId(); // 혹은 return id; 해도 됨
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    // delete 구현
    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        // 해당 메모가 DB에 존재하는지 확인
        if(memoList.containsKey(id)) {
            // 해당 메모 삭제하기
            memoList.remove(id); // 이거는 remove 따로 만들 필요 없이 map 자료구조의 remove 넣어주면 된다 - 해당 key(id)를 파라미터 값으로 넣어주기
            return id; // id 반환
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }
}
