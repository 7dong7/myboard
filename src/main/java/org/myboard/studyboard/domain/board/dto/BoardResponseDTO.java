package org.myboard.studyboard.domain.board.dto;

import lombok.Data;

@Data
public class BoardResponseDTO {

    private Long id;
    private String title;
    private String content;
}
