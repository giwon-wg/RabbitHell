package com.example.rabbithell.domain.job.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rabbithell.domain.job.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
}
