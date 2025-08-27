package com.example.iruda.users;

import com.example.iruda.projects.ProjectMember;
import com.example.iruda.users.dto.UserRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = true)
    private String userPw;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String birth;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private boolean privacyAgree;

    @Column
    private String provider;

    @Column
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectMember> projectMembers;

    public User(UserRequest userRequest, String encryptedPassword) {
        this.userId = userRequest.userId();
        this.userPw = encryptedPassword;
        this.name = userRequest.name();
        this.phone = userRequest.phone();
        this.birth = userRequest.birth();
        this.department = userRequest.department();
        this.privacyAgree = userRequest.privacyAgree();
        this.provider = userRequest.provider();
        this.providerId = userRequest.providerId();
        this.role = userRequest.role();
    }

    public void update(UserRequest userRequest, String encodedPw) {
        this.userId = userRequest.userId();
        if (encodedPw != null) {
            this.userPw = encodedPw;
        }
        this.name = userRequest.name();
        this.phone = userRequest.phone();
        this.birth = userRequest.birth();
        this.department = userRequest.department();
        this.privacyAgree = userRequest.privacyAgree();
        this.provider = userRequest.provider();
        this.providerId = userRequest.providerId();
        this.role = userRequest.role();
    }
}
