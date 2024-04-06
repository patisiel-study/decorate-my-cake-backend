package com.example.decoratemycakebackend.domain.friend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FriendRequestDto {
    @Schema(description = "수신자", example = "test12345@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "발신자 이메일은 필수 입력 값입니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String receiverEmail;

    @Schema(description = "메시지", example = "나 ooo인데 친추좀 해주라", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String message;


}
