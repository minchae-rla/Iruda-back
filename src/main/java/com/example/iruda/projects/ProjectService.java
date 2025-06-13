package com.example.iruda.projects;

import com.example.iruda.jwt.JwtProvider;
import com.example.iruda.projects.dto.ProjectDetailRequest;
import com.example.iruda.projects.dto.ProjectDetailResponse;
import com.example.iruda.projects.dto.ProjectRequest;
import com.example.iruda.projects.dto.ProjectResponse;
import com.example.iruda.users.User;
import com.example.iruda.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectDetailRepository projectDetailRepository;
    private final UserRepository userRepository;

    // 프로젝트 생성
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


    // 프로젝트 조회
    public List<ProjectResponse> getProjects(Long userId) {
        List<ProjectMember> projectMembers = projectMemberRepository.findByUserId(userId);

        return projectMembers.stream()
                .map(ProjectMember::getProject)  // getProject()로 Project 객체 가져오기
                .distinct()
                .map(project -> {
                    if (project != null) {
                        return new ProjectResponse(project.getId(), project.getName());
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)  // null 제거
                .toList();
    }


    // 일정 조회
    public ProjectDetailResponse getTask(Long taskId) {
        return projectDetailRepository.findById(taskId)
                .map(ProjectDetailResponse::fromEntity)
                .orElse(null);
    }


    // 일정 추가
    public void addTask(ProjectDetailRequest request) {
        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        ProjectDetail detail = new ProjectDetail(request, project);
        projectDetailRepository.save(detail);
    }


    // 프로젝트 삭제
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("프로젝트를 찾을 수 없습니다."));

        List<ProjectMember> projectMembers = projectMemberRepository.findAllByProjectId(projectId);
        projectMemberRepository.deleteAll(projectMembers);

        List<ProjectDetail> projectDetails = projectDetailRepository.findAllByProjectId(projectId);
        projectDetailRepository.deleteAll(projectDetails);

        projectRepository.delete(project);
    }

    // 일정 삭제
    public void deleteTask(Long taskId) {
        projectDetailRepository.deleteById(taskId);
    }

    // 프로젝트 제목 수정
    public void updateProject(Long projectId, ProjectRequest projectRequest) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        project.update(projectRequest);

        projectRepository.save(project);
    }

    // 일정 수정
    public void updateTask(Long taskId, ProjectDetailRequest request) {
        ProjectDetail detail = projectDetailRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task Not found"));

        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        detail.update(request, project);
        projectDetailRepository.save(detail);
    }

    // 본인 프로젝트 확인
    public boolean projectUserCheck(Long userId, Long projectId) {
        ProjectMember projectMember = projectMemberRepository.findByUserIdAndProjectId(userId, projectId);

        return projectMember != null;
    }

    // 프로젝트 TL 확인
    public boolean leaderCheck(Long userId, Long projectId) {
        ProjectMember projectMember = projectMemberRepository.findByUserIdAndProjectId(userId, projectId);

        return projectMember.getProjectPosition() == ProjectPosition.TL;
    }

    // 일정 pk로 프로젝트 pk 찾기 수정
    public Long taskCheck(Long taskId) {
        return projectDetailRepository.findById(taskId)
                .map(projectDetail -> projectDetail.getProject().getId())  // getProject()로 Project 객체를 가져온 뒤 ID를 반환
                .orElse(null);
    }
    
    
    //프로젝트에 유저 초대
    public void inviteUser(Long projectId, ProjectRequest projectRequest) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("프로젝트를 찾을 수 없습니다."));

        Long userId = projectRequest.userId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        ProjectMember projectMember = new ProjectMember(project, user, ProjectPosition.TM);  // 역할(TM) 설정
        projectMemberRepository.save(projectMember);
    }
    
    
}
