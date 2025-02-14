package org.myboard.studyboard.domain.board.repository;

import org.myboard.studyboard.domain.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
}
