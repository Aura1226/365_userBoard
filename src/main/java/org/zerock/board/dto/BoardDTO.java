package org.zerock.board.dto;


import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {

    private Long bno;

    private String title, content, writerEmail, writerName;

    private LocalDateTime regDate, modDate;

    private int replyCount;
}
