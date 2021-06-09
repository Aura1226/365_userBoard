package org.zerock.board.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;

import javax.transaction.Transactional;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService service;

    @Test
    public void testRegister(){

        BoardDTO dto = BoardDTO.builder()
                .title("테스트")
                .content("테스트")
                .writerEmail("user10@aaa.com")
                .build();

        Long bno = service.register(dto);

        log.info("BNO: " + bno);

    }

    @Test
    public void testList(){

        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        //log.info(service.getList(pageRequestDTO));

        PageResultDTO<BoardDTO, Object[]> result = service.getList(pageRequestDTO);

        result.getDtoList().forEach(boardDTO -> log.info(boardDTO));
    }


    @Test
    public void testGet(){

        log.info(service.get(100L));
    }

    @Test
    public void testRemove(){

        Long bno = 42L;

        service.removeWithReplies(bno);
    }

    @Transactional //getOne 해줬기때문에 @Transactional 해줘야 에러가 없어지지만 Rolled back이 되어 수정이 안된다.
    @Commit //그래서 이거까지 해줘야 한다.
    @Test
    public void testModify(){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(100L)
                .title("100번 업데이트 됨")
                .content("100번 수정")
                .build();

        service.modify(boardDTO);
    }
}
