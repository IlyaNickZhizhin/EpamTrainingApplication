package org.epam.mainservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.List;

@Entity(name = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private Authority authority;

    @Override
    public String getAuthority() {
        return authority.toString();
    }

    public static Role of(Authority authority){
        return new Role(Authority.idOf(authority), authority);
    }

    public static Role of(String authority){
        return new Role(Authority.idOf(authority), Authority.valueOf(authority));
    }

    public enum Authority {
        ROLE_ADMIN, ROLE_TRAINER, ROLE_TRAINEE;

        public static int idOf(String name) {
            return getAuthorities().indexOf(Authority.valueOf(name))+1;
        }

        public static int idOf(Authority name) {
            return getAuthorities().indexOf(name)+1;
        }

        public static List<Authority> getAuthorities() {
            return Arrays.asList(Authority.values());
        }
    }
}
