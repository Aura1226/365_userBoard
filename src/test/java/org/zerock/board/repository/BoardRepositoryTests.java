package org.zerock.board.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.entity.QBoard;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    //주입
    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void insertBoards(){

        IntStream.rangeClosed(1, 100).forEach(i -> {
            //1부터 100까지 랜덤한 수 를 넣어줄거야
            int idx = (int)(Math.random() * 100) + 1;

            //FK? PK? 주입
            Member member = Member.builder().email("user" + idx + "@aaa.com").build();

            Board board = Board.builder()
                    .title("Title..." + i)
                    .content("Content..." + i)
                    .writer(member)
                    .build();
            boardRepository.save(board);
        });

    }

    //에러를 잘 봐둬라 => no Session 에러
    @Test
    public void testRead() {

        Optional<Board> result = boardRepository.findById(100L);

        log.info(result);// 여기까지만 출력하면 자동으로 조인되어 있다고 결과가 나오고

        if(result.isPresent()){
            Board board = result.get();
            log.info("-------------------------");
            log.info(board);// 여기까지하면 Member까지 다 가져온다고 나와서 엄청 좋아보이지만...
            // 조인 된 만큼 불러올 때 마다 추가로 불러오기때문에 너무 무거워진다.
        }

    }

    @Test
    public void testBoardWithReplyCount(){

        //long bno = 100L;

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);

//        Object[] arr = boardRepository.getBoardWithReplyCount(bno);
//
//        log.info(Arrays.toString(arr));
//
//        Object[] inner = (Object[])arr[0];
//
//        log.info(Arrays.toString(inner));
        result.get().forEach(arr -> log.info(Arrays.toString(arr)));
    }

    @Test
    public void testReadWithCount(){

        Long bno = 100L;

        Object result = boardRepository.getBoardByBno(bno);

        log.info(result);

        //위에까지하고 돌리면 배열로 나오니까 Object[]로 만들어준다.
        Object[] arr = (Object[]) result;

        log.info(Arrays.toString(arr));
    }

    @Test
    public void testSearch1(){

        boardRepository.search1();

    }

    @Test
    public void testSearchPage(){

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        String type = "tcw";
        String keyword = "10";

        Page<Object[]> result = boardRepository.searchPage(type, keyword, pageable);

        result.get().forEach(arr -> log.info(Arrays.toString(arr)));
    }

}
