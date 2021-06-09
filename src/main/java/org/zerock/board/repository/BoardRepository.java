package org.zerock.board.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.board.entity.Board;
import org.zerock.board.repository.search.SearchBoardRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {

//    @Query("select b,count(r) from Board b left join Reply r on r.board = b" +
//            " group by b")//select 뒤에 알리아스가 와야하고 from 뒤에는 들어가는건 Entity 타입.
//    Page<Object[]> getBoardWithReplyCount(Pageable pageable); //여기의 타입(Board)와 쿼리의 b부분이 같아야한다.(= Board)
                                            //2가지이상 들어간다면 Object[]로 해준다?

    //3가지만 join 해서 많은 양의 컬럼을 가져온다.
    @Query(value = "select b,w,count(r) from Board b " +
            "inner join b.writer w " +
            "left join Reply r on r.board = b " +
            "group by b"
    ,
    countQuery = "select count(b) from Board b")
            Page<Object[]> getBoardWithReplyCount(Pageable pageable);


    @Query(value = "select b,w,count(r) from Board b " +
            "inner join b.writer w " +
            "left join Reply r on r.board = b " +
            "where b.bno= :bno " +
            "group by b")
    Object getBoardByBno(Long bno);
    }
