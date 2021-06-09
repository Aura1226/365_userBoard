package org.zerock.board.repository.search;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.QBoard;
import org.zerock.board.entity.QMember;
import org.zerock.board.entity.QReply;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {


    public SearchBoardRepositoryImpl(){
        super(Board.class);
    }


    @Override
    public Board search1() {

        log.warn("search1........");

        QBoard board = QBoard.board;
        QMember member = QMember.member;
        QReply reply =QReply.reply;

        //select * from Board b
        JPQLQuery<Board> jpqlQuery = from(board); //상속받았으니 from
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        //jpqlQuery.leftJoin(board.writer); //이러면 on을 안해줘도 된다.
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        BooleanExpression ex1 = board.bno.eq(100L);

        JPQLQuery<Tuple> tuple =  jpqlQuery.select(board, member, reply.count()).where(ex1).groupBy(board);
                                   //이 query는 MariaDB에선 정상작동하지만 MySQL, OracleDB에선 안된다.
        //여러개를 select하려면 Tuple이 필요
        log.warn("----------------------------");
        log.info(jpqlQuery);
        log.warn("----------------------------");

        List<Tuple> result = tuple.fetch(); //여기도 Board를 Tuple로 변경
        log.warn("==========================");
        log.warn(result);
        Long count = jpqlQuery.fetchCount();


        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {

        log.warn("=====================================");
        log.warn("=====================================");

        QBoard board = QBoard.board;
        QMember member = QMember.member;
        QReply reply =QReply.reply;

        //select * from Board b
        JPQLQuery<Board> jpqlQuery = from(board); //상속받았으니 from
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        //jpqlQuery.leftJoin(board.writer); //이러면 on을 안해줘도 된다.
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression booleanExpression = board.bno.gt(0L); //gt = 보다 큰 Greater than
        //and bno > 0
        booleanBuilder.and(booleanExpression);

        //where (board.title like xxxx OR member.name like xxx OR reply.text like xxx)
        if (type != null){

            BooleanBuilder conditionBuilder = new BooleanBuilder();
            String[] typeArr = type.split("");

            for (String t :typeArr){
                switch (t){
                    case "t" :
                        conditionBuilder.or(board.title.contains(keyword));
                        break;
                    case "w" :
                        conditionBuilder.or(member.name.contains(keyword));
                        break;
                    case "c" :
                        conditionBuilder.or(reply.text.contains(keyword));
                        break;

                }//end switch
            }//end for

            booleanBuilder.and(conditionBuilder);

        }//end type if

        //==================================================
        Sort sort = pageable.getSort();

        //무엇에 대한 order by인가
        sort.stream().forEach(order -> {
            //bno
            String prop = order.getProperty();
            log.warn("sort prop: " + prop);

            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            PathBuilder<Board> orderByExpression = new PathBuilder<Board>(
                    Board.class, "board");

            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));

        });


        tuple.where(booleanBuilder);

        //group by
        tuple.groupBy(board);

        tuple.offset(pageable.getOffset()); //0부터 1까지? 계산해준다
        tuple.limit(pageable.getPageSize());

        log.warn(tuple);

        List<Tuple> result = tuple.fetch();

        long count = tuple.fetchCount();

        List<Object[]> resultList
                = result.stream().map(tuple1 -> tuple1.toArray()).collect(Collectors.toList());

        return new PageImpl<Object[]>(resultList, pageable, count);
    }
}
