package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.entity.Assignment;

@Repository
public interface AssignmentRepo extends JpaRepository<Assignment, Long> {

}
