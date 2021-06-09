package org.zerock.board.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Data
public class PageRequestDTO { //모든 페이징을 할 때 다 이걸 쓸 수 있게...범용성있게 만들어보자


    private int page = 1;
    private int size = 10;

    private String type;
    private String keyword;

   public PageRequestDTO(){ //초기화를 위해...Default와 같은
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable(Sort sort){

        return PageRequest.of(page - 1, size, sort);
    }
}
