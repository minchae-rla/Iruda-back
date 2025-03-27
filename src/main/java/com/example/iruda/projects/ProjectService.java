package com.example.iruda.projects;

import com.example.iruda.jwt.JwtProvider;
import com.example.iruda.projects.dto.ProjectDetailRequest;
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
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectDetailRepository projectDetailRepository;
    private final UserRepository userRepository;

    //프로젝트 생성
    public void addProject(ProjectRequest projectRequest, Long userId) {
        //회원이 존재하는 확인
        User user = userRepository.findById(userId).orElse(null);
        
        //프로젝트 테이블에 프로젝트명만 저장
        Project project = new Project(projectRequest);
        projectRepository.save(project);

        //프로젝트 멤버테이블에 프로젝트PK, userId, 포지션 저장
        ProjectMember projectMember = new ProjectMember(project, user, ProjectPosition.TL); // 역할(TL) 설정
        projectMemberRepository.save(projectMember);
    }


    //프로젝트 조회
    public List<ProjectResponse> getProjects(Long userId) {
        // ProjectMember에서 프로젝트를 가져와서 ProjectResponse로 변환
        return projectMemberRepository.findByUserId(userId).stream()
                .map(projectMember -> new ProjectResponse(projectMember.getProject().getId(), projectMember.getProject().getName()))
                .toList();
    }


    // 프로젝트 상세 일정 추가
    public void addTask(ProjectDetailRequest projectDetailRequest, Long projectId) {
        ProjectDetail projectDetail = new ProjectDetail(projectDetailRequest, projectId);

        projectDetailRepository.save(projectDetail);
    }


    //프로젝트 삭제
    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
        projectMemberRepository.deleteById(projectId);
        projectDetailRepository.deleteById(projectId);
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
        List<ProjectMember> projectMembers = projectMemberRepository.findByUserId(userId);

        return projectMembers.stream()
                .anyMatch(projectMember -> projectMember.getProject().getId().equals(projectId));
    }
}
