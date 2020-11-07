package com.polytech.alertcovidserviceuser.models;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query(value = "SELECT * FROM users Where email = ?1 ",nativeQuery = true)
    User getUserByEmail(String email);
}
