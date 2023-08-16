package com.example.elderlinker.User;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {

    @Size(min = 3, max = 25, message = "아이디는 3자 이상 25자 이하로 입력해주세요.")
    @NotEmpty(message = "사용자 아이디는 필수항목입니다.")
    private String userid; // 사용자의 아이디를 입력하는 필드.

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password; // 사용자의 비밀번호를 입력하는 필드.

    @NotEmpty(message = "이름은 필수항목입니다.")
    private String username; // 사용자의 이름을 입력하는 필드.

    @Pattern(regexp = "010\\d{8}", message = "전화번호는 010으로 시작하고 총 13자리 숫자여야 합니다.")
    private String phonenumber; // 사용자의 전화번호를 입력하는 필드.
}
