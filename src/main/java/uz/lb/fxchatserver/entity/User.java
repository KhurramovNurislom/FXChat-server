package uz.lb.fxchatserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import uz.lb.fxchatserver.enums.AccountRoleEnums;
import uz.lb.fxchatserver.enums.GeneralStatus;

import java.io.Serializable;
import java.util.Date;


@Data
@Builder
@Entity(name = "Account")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(unique = true, nullable = false, name = "login")
    String login;

    @Column(nullable = false, name = "password")
    String password;


//    @ManyToMany
//    @Column(name = "role_list")
//    List<Role> roleList;

    @JsonIgnore
    @Column(name = "roles")
    @Enumerated(EnumType.STRING)
    AccountRoleEnums role;

    @Column(name = "status")
    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Builder.Default
    GeneralStatus status = GeneralStatus.ACTIVE;

    @JsonIgnore
    @Column(name = "visible")
    @Builder.Default
    Boolean visible = Boolean.TRUE;

    @JsonIgnore
    @CreationTimestamp
    Date createdAt;

    @JsonIgnore
    @UpdateTimestamp
    Date updatedAt;

}
