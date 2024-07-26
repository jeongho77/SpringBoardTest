package JH.Board.repository;

import JH.Board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    // 조회수를 올려야할때.
    // update board_table set board_hits = board_hits+1 where id=?

    @Modifying //@Modifying 어노테이션은 데이터베이스 수정 쿼리(INSERT, UPDATE, DELETE)를 실행할 때 필요합니다.
    @Query(value = "update BoardEntity b set b.boardHits=b.boardHits+1 where b.id=:id")
    void updateHits(@Param("id") Long id);
}
