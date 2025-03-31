package com.example.iruda.projects;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findByUserId(Long userId);

    ProjectMember findByUserIdAndProjectId(Long userId, Long projectId);
}