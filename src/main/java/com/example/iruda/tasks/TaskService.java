package com.example.iruda.tasks;

import com.example.iruda.projects.Project;
import com.example.iruda.projects.ProjectRepository;
import com.example.iruda.tasks.dto.TaskRequest;
import com.example.iruda.tasks.dto.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    
    //일정 조회
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
}
