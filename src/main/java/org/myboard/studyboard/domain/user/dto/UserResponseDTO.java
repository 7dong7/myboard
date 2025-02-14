package org.myboard.studyboard.domain.user.dto;

import lombok.Data;

@Data
public class UserResponseDTO {

    private String username;
    private String nickname;
    private String role;
}
