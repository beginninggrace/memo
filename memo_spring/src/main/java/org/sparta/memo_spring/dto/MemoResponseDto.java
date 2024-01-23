package org.sparta.memo_spring.dto;

import lombok.Getter;

    @Getter
    public class MemoResponseDto {
        private Long id;
        private String username;
        private String contents;

        public MemoResponseDto(com.sparta.memo_spring.entity.Memo memo) {
            this.id = memo.getId();
            this.username = memo.getUsername();
            this.contents = memo.getContents();
        }
    }

