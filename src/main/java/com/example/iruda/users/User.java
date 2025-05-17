package com.example.iruda.users;

import com.example.iruda.projects.ProjectMember;
import com.example.iruda.users.dto.UserRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
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

    }

    public void setPw(String newPassword) {
        this.userPw = newPassword;
    }
}
