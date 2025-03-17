package com.example.iruda.users;

import com.example.iruda.users.dto.UserRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(nullable =false)
    private String phone;

    @Column(nullable =false)
    private String department;

    @Column
    private String provider;

    @Column
    private String providerId;

    public User(UserRequest userRequest, String encryptedPassword) {
        this.userId = userRequest.userId();
        this.userPw = encryptedPassword;
        this.name = userRequest.name();
        this.phone = userRequest.phone();
        this.department = userRequest.department();
        this.provider = userRequest.provider();
        this.providerId = userRequest.providerId();
    }
}
