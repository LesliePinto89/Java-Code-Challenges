package com.yuzu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yuzu.entities.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
