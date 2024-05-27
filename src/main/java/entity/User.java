package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity (name = "users")
@Table(indexes = {
        @Index(unique = true, name = "email_idx", columnList = "email")
})
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "name")
    private String name;
    @Column(nullable = false, name = "surname")
    private String surname;
    @Column(nullable = false, name = "email", unique = true)
    private String email;
    @Column(name = "profile picture")
    private String profilePicture;
}
