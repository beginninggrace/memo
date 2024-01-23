package com.sparta.memo_spring.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sparta.memo_spring.dto.MemoRequestDto;

@Getter
@Setter
@NoArgsConstructor
public class Memo {
    private Long id;
    private String username;
    private String contents;

    public Memo(MemoRequestDto requestDto) { // requestDto 타입에 맞는 (MemoRequestDto) 생성자 만들어줌 - (MemoController 25열 참조)
                                             // 이렇게 받아오는 이유는 클라이언트에서 받아온 데이터가 requestDto에 들어있으니 필드에다가 데이터를 넣어주려고 하는 거
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
    }

    public void update(MemoRequestDto requestDto) { // 수정할 데이터는 username과 contents가 넘어올거니 이렇게 만들기
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
    }
}