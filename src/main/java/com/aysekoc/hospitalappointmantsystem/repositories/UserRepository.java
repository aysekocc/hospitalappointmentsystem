package com.aysekoc.hospitalappointmantsystem.repositories;

import com.aysekoc.hospitalappointmantsystem.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdentity(String identity);
    Optional<User> findByUsername(String username);
    List<User> findByName(String name);
    User findByUsernameAndPassword(String username, String password);



}
