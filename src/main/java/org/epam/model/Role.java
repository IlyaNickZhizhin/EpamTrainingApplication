package org.epam.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity(name = "roles")
@Data
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "authority")
    private Authority authority;

    @Override
    public String getAuthority() {
        return authority.toString();
    }

    public enum Authority {
        ROLE_ADMIN, ROLE_TRAINER, ROLE_TRAINEE
    }
}
