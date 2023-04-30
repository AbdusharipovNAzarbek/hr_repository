package com.company.hr_manegment.repository;

import com.company.hr_manegment.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    Page<Task>findAllByUsersId(UUID users_id, Pageable pageable);
    Optional<Task> findByIdAndUsersId(Integer id, UUID users_id);
}
