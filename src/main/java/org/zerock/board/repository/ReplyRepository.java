package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.board.entity.Reply;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Modifying
    @Query("delete from Reply r where r.board.bno = :bno")
    void deleteByBno(Long bno);

    //게시물로 댓글 목록 가져오기... 교재랑 다르게 @Query로 할 것

    @Query("select r from Reply r where r.board.bno = :bno order by r.rno asc ")
    List<Reply> getBoardReply(Long bno);
}
