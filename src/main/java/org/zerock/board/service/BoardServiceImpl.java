package org.zerock.board.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.repository.ReplyRepository;

import javax.transaction.Transactional;
import java.util.function.Function;

@Service
@AllArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;

    private final ReplyRepository replyRepository;

    @Override
    public Long register(BoardDTO dto) {

        Board board = dtoToEntity(dto);

        boardRepository.save(board);

        return board.getBno();
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable(Sort.by("bno").descending());

        //Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);
        // => 0422 검색 조건 추가 후 수정
        Page<Object[]> result = boardRepository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending())
        );

        //T = 입력값(Object[]) , R = 리턴타입(BoardDTO)
        Function<Object[], BoardDTO> fn = (arr -> entityToDTO(
                (Board) arr[0],
                (Member) arr[1],
                (Long) arr[2])
        );

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public BoardDTO get(Long bno) {

        Object result = boardRepository.getBoardByBno(bno);

        Object[] arr = (Object[]) result;

        return entityToDTO((Board) arr[0], (Member) arr[1], (Long) arr[2]);
    }

    @Transactional
    @Override
    public void removeWithReplies(Long bno) {

        replyRepository.deleteByBno(bno);

        boardRepository.deleteById(bno);
    }

    @Override
    public void modify(BoardDTO dto) {

        Board board = boardRepository.getOne(dto.getBno());

        board.changeTitle(dto.getTitle());
        board.changeContent(dto.getContent());

        boardRepository.save(board);
    }
}
