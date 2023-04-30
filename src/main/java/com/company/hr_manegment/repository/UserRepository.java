package com.company.hr_manegment.repository;

import com.company.hr_manegment.entity.Role;
import com.company.hr_manegment.entity.Task;
import com.company.hr_manegment.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Page<User> findAllByRoles(Set<Role> roles, Pageable pageable);

    Optional<User> findByIdAndRoles(UUID id, Set<Role> roles);

}
