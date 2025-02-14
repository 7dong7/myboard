package org.myboard.studyboard.domain.user.dto;

import lombok.Data;

@Data
public class UserRequestDTO {

    private String username;
    private String password;
    private String nickname;
}
