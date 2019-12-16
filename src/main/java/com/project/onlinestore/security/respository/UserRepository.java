package com.project.onlinestore.security.respository;

import com.project.onlinestore.security.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    @Query("select u from User u where u.username = ?1")
    User findByUsername(String username);

    @Query("select u from User u where u.active=:active and u.role.id=:roleId")
    List<User> findByActiveAndRoleId(Integer active, Long roleId);
}
