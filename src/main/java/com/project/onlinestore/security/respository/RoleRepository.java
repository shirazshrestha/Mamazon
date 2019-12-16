package com.project.onlinestore.security.respository;

import com.project.onlinestore.security.domain.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends CrudRepository<Role,Long> {
    Optional<Role> findById(Long id);
}
