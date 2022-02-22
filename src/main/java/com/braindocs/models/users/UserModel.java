package com.braindocs.models.users;

import com.braindocs.models.organisations.OrganisationModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
public class UserModel {

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "fullname")
    private String fullname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="organisation")
    private OrganisationModel organisation;

    @Column(name = "shortname")
    private String shortname;

    @Column(name = "male")
    private String male; // list of: 'female', 'male'

    @Column(name = "birthday")
    private String birthday;

    @OneToMany
    @JoinColumn(name="user_id")
    private List<UserContactModel> contacts;

    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<UserRoleModel> roles;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "marked")
    private Boolean marked;
    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createTime;
    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updateTime;

}
