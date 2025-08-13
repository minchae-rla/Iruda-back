package com.example.iruda.tasks;

import com.example.iruda.projects.Project;
import com.example.iruda.projects.ProjectRepository;
import com.example.iruda.tasks.dto.TaskRequest;
import com.example.iruda.tasks.dto.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
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
    
    //프로젝트의 일정 전체 조회
    public List<TaskResponse> getAllTasks(Long projectId) {
        return taskRepository.findAllByProjectId(projectId).stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }

    //일정 전체 조회
    public List<TaskResponse> allTasks(Long userId) {

        return taskRepository.findAllByUserId(userId)
                .stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }

    //오늘의 일정
    public List<TaskResponse> getTodayTasks(Long userId) {
        LocalDate date = LocalDate.now();

        return taskRepository.findTaskByUserIdAndEndDate(userId, date)
                .stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    //내일의 일정
    public List<TaskResponse> getTomorrowTasks(Long userId) {
        LocalDate date = LocalDate.now().plusDays(1);

        return taskRepository.findTaskByUserIdAndEndDate(userId, date)
                .stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }

    //완료된 일정
    public List<TaskResponse> getCompleteTasks(Long userId) {
        LocalDate date = LocalDate.now();

        return taskRepository.findCompleteTaskByUserIdAndEndDate(userId, date)
                .stream()
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

    //일정 알람
    public List<TaskResponse> getAlarm(Long userId) {
        List<Task> tasks = taskRepository.findTodayAlarmsByUserId(userId);
        return tasks.stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }



}
