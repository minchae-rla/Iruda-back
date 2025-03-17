package com.example.iruda.projects;

import com.example.iruda.jwt.JwtProvider;
import com.example.iruda.projects.dto.ProjectRequest;
import com.example.iruda.projects.dto.ProjectResponse;
import com.example.iruda.users.User;
import com.example.iruda.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    //프로젝트 조회
    public List<ProjectResponse> getProjects(Long userId) {
        return projectRepository.findByUserId(userId).stream().map(ProjectResponse::fromEntity).toList();
    }

    //프로젝트 추가
    public void addProject(ProjectRequest projectRequest, Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        Project project = new Project(projectRequest, user);
        projectRepository.save(project);
    }

    //프로젝트 삭제
    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }

    //프로젝트 수정
    public void updateProject(Long projectId, ProjectRequest projectRequest) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + projectId));
        project.update(projectRequest);
        projectRepository.save(project);
    }

    // 본인 프로젝트 확인
    public boolean projectUserCheck(Long userId, Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);

        //project에 user가 포함이면 true반환
        return project != null && project.getUser().getId().equals(userId);
    }
}
