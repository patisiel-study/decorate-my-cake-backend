package com.example.decoratemycakebackend.domain.member.dto;

import com.example.decoratemycakebackend.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDto {

    @Schema(description = "이메일", example = "test1234@gmail.com", required = true)
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String email;

    @Schema(description = "비밀번호는 최소 8자 이상, 영문자, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다.", example = "!test1234", required = true)
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "비밀번호는 최소 8자 이상, 영문자, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다.")
    private String password;

    @Schema(description = "닉네임은 2자 이상 10자 이하로 입력해주세요.", example = "민교수", required = true)
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로 입력해주세요.")
    private String nickname;

    @Schema(description = "생년월일은 yyyy-MM-dd 형태로 입력해주세요.", example = "1997-03-20", required = true)
    @Past(message = "생년월일은 yyyy-MM-dd 형태로 이전 날짜만 입력 가능합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private String profileImg;

    public Member toEntity(String encodedPassword, List<String> roles) {
        return Member.builder()
                .email(email)
                .password(encodedPassword)
                .nickname(nickname)
                .birthday(birthday)
                .profileImg(profileImg)
                .roles(roles)
                .build();
    }
}
