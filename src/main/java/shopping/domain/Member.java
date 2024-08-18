package shopping.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Member {

    @Id
    private String memberId;
    private String password;
    private String name;
    private String email;

}