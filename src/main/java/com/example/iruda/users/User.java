package com.example.iruda.users;

import com.example.iruda.projects.ProjectMember;
import com.example.iruda.users.dto.UserRequest;
import com.example.iruda.users.dto.SetUserRequest;
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
    private String userId;  // 로그인 ID (수정 불가)

    @Column(nullable = true)
    private String userPw;  // 비밀번호

    @Column(nullable = false)
    private String name;    // 이름

    @Column(nullable = false)
    private String phone;   // 전화번호

    @Column(nullable = false)
    private String birth;   // 생년월일 (수정 불가)

    @Column(nullable = false)
    private String department; // 소속

    @Column(nullable = false)
    private boolean privacyAgree; // 개인정보 동의 (수정 불가)

    @Column
    private String provider;  // OAuth 제공자 (수정 불가)

    @Column
    private String providerId; // OAuth provider ID (수정 불가)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER; // 권한 (수정 불가)

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectMember> projectMembers;

    // 회원가입용 생성자
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

    // 회원정보 수정용 (SetUserRequest 사용)
    public void updateProfile(SetUserRequest userRequest, String encodedPw) {
        if (encodedPw != null) {
            this.userPw = encodedPw;
        }
        if (userRequest.name() != null) {
            this.name = userRequest.name();
        }
        if (userRequest.phone() != null) {
            this.phone = userRequest.phone();
        }
        if (userRequest.department() != null) {
            this.department = userRequest.department();
        }
    }
}
