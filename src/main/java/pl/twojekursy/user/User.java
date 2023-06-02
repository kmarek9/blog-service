package pl.twojekursy.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.twojekursy.address.Address;
import pl.twojekursy.groupinfo.GroupInfo;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
@Data
@NoArgsConstructor
@ToString(exclude = {"groupsInfo", "address"})
@EqualsAndHashCode(exclude = {"groupsInfo", "address"})
@Builder(toBuilder = true)
@AllArgsConstructor
@Table(name = "user_info"/*,
        uniqueConstraints=
            @UniqueConstraint(columnNames={"login"})*/)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @NotNull
    private Integer version;

    @CreatedDate
    @NotNull
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    @NotNull
    private LocalDateTime lastModifiedDateTime;

    @NotBlank
    @NotNull
    @Size(max = 100)
    private String login;

    @NotBlank
    @NotNull
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<GroupInfo> groupsInfo;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Address address;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isAdmin() {
        return role==UserRole.ADMIN;
    }
}
