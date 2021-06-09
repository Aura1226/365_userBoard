package org.zerock.board.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "writer") //(exclude = "제외할 이름")해주지 않으면 no Session에러가 난다.
public class Board extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;

    private String content;

    //연관관계(참조, FK)를 걸어줄거
    @ManyToOne(fetch = FetchType.LAZY) //(fetch = Fetch.Lazy or Eager) 추가. Lazy가 퍼포먼스적으로 월등히 좋다.
    private Member writer;

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String content){
        this.content = content;
    }
}
