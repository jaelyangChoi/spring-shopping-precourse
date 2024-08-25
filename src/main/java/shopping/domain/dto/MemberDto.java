package shopping.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shopping.domain.Member;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;

    public Member toEntity() {
        return new Member(this.email, this.password);
    }
}
