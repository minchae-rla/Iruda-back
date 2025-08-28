package com.example.iruda.users;

import com.example.iruda.users.dto.GetMinimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserId(String userId);  // userId로 User 찾기

    User findByNameAndBirthAndPhone(String name, String birth, String phone);

    @Query("SELECT u.id FROM User u WHERE u.userId = :userId AND u.name = :name AND u.birth = :birth AND u.phone = :phone")
    Long findPw(
            @Param("userId") String userId,
            @Param("name") String name,
            @Param("birth") String birth,
            @Param("phone") String phone
    );

    Optional<User> findByProviderAndProviderId(String provider, String providerId);

    @Query("SELECT new com.example.iruda.users.dto.GetMinimal(u.userId, u.name) FROM User u WHERE u.id = :userId")
    GetMinimal findMinimalById(@Param("userId") Long userId);

}
