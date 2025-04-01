package com.example.iruda.projects;

import com.example.iruda.projects.dto.ProjectRequest;
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

    //관계설정(실제컬럼X, 추후 조회 등을 사용할때 편리)
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectMember> projectMembers;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectDetail> projectDetails;

    public Project(ProjectRequest projectRequest) {
        this.name = projectRequest.name();
    }

    public void update(ProjectRequest projectRequest) {
        this.name = projectRequest.name();
    }
}
