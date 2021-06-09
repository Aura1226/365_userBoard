package org.zerock.board.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Member extends BaseEntity{

    @Id //@Entity하면 @ID가 있어야한다.
    private String email;

    private String password;

    private String name;
}
