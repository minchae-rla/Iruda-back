package com.example.iruda.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(String userId);  // userId로 User를 찾는 메서드

    User findByNameAndBirthAndPhone(String name, String birth, String phone);

    User findByUserIdAndNameAndBirthAndPhone(String userId, String name, String birth, String phone);
}
