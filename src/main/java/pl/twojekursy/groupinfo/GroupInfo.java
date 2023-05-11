package pl.twojekursy.groupinfo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.twojekursy.user.User;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Audited
@Data
@NoArgsConstructor
@ToString(exclude = {"users"})
@EqualsAndHashCode(exclude = {"users"})
@Builder(toBuilder = true)
@AllArgsConstructor
public class GroupInfo {
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
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "groupsInfo")
    private Set<User> users;
}
