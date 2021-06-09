package org.zerock.board.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass //얘를 가지고 table을 만들어 주지 않는다? Entity의 슈퍼 클래스가 되겠다.
@EntityListeners(value = {AuditingEntityListener.class}) //@EnalbeJpaAuditing를 자동으로 감지하겠다?
@Getter
public class BaseEntity {

    @CreatedDate
    @Column(name = "regdate", updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column(name = "moddate")
    public LocalDateTime modDate;
}
