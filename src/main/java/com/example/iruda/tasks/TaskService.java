package com.example.iruda.tasks;

import com.example.iruda.projects.Project;
import com.example.iruda.projects.ProjectRepository;
import com.example.iruda.tasks.dto.TaskRequest;
import com.example.iruda.tasks.dto.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    //일정 추가
    public void addTask(TaskRequest taskRequest) {
        Project project = projectRepository.findById(taskRequest.projectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        Task task = new Task(taskRequest, project);
        taskRepository.save(task);
    }
    
    //일정 전체 조회
    public List<TaskResponse> getAllTasks(Long projectId) {
        return taskRepository.findAllByProjectId(projectId).stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    //일정 상세
    public TaskResponse getTask(Long taskId) {
        return taskRepository.findById(taskId)
                .map(TaskResponse::fromEntity)
                .orElse(null);
    }
    
    //일정 삭제
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
    
    //일정 수정
    public void updateTask(Long taskId, TaskRequest taskRequest) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task Not found"));

        Project project = projectRepository.findById(taskRequest.projectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        task.update(taskRequest, project);
        taskRepository.save(task);
    }


    public TaskResponse getAlarm(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("해당 작업이 존재하지 않습니다."));

        LocalDate today = LocalDate.now();

        LocalDate taskEndDate = task.getEndDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        if (today.equals(taskEndDate)) {
            // 날짜가 같으면 알림 전송 로직
            System.out.println("오늘은 일정의 마지막 날입니다. 알림 전송!");
        }

        return new TaskResponse(task);
    }
}
