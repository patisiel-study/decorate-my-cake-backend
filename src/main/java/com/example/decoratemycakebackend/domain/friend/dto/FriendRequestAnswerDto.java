package com.example.decoratemycakebackend.domain.friend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendRequestAnswerDto {
    @Schema(description = "발신자 이메일", example = "test1234@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "발신자 이메일은 필수 입력 값입니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String senderEmail;

    @Schema(description = "수신자", example = "test12345@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "발신자 이메일은 필수 입력 값입니다.")
    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    private String receiverEmail;

    @Schema(description = "친구요청 수락 여부", example = "true/false", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean isAccepted;
}
