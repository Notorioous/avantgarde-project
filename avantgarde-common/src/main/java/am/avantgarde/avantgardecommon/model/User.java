package am.avantgarde.avantgardecommon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String firstname;
    @Column
    private String lastname;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String picUrl;
    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column
    @Enumerated(EnumType.STRING)
    private UserType userType = UserType.USER;
    @Column
    private String token;
    @Column
    private boolean isActive  = false;
}
