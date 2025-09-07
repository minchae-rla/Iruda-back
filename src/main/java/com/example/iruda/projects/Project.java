package com.example.iruda.projects;

import com.example.iruda.projects.dto.ProjectRequest;
import com.example.iruda.tasks.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name="projects")
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProjectMember> projectMembers;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Task> task;

    public Project(ProjectRequest projectRequest) {
        this.name = projectRequest.name();
    }

    public void update(ProjectRequest projectRequest) {
        this.name = projectRequest.name();
    }
}
