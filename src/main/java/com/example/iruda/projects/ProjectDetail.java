package com.example.iruda.projects;

import com.example.iruda.projects.dto.ProjectDetailRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@Table(name="projectDetail")
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_Id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;

    @Column(nullable = false)
    private String alarmSet;
    
    public ProjectDetail(ProjectDetailRequest projectDetailRequest, Project project) {
        this.project = project;
        this.title = projectDetailRequest.title();
        this.content = projectDetailRequest.content();
        this.startDate = projectDetailRequest.startDate();
        this.endDate = projectDetailRequest.endDate();
        this.alarmSet = projectDetailRequest.alarmSet();
        this.createDate = new Date();
        this.updateDate = new Date();
    }


    public void update(ProjectDetailRequest projectDetailRequest, Project project) {
        this.project = project;
        this.title = projectDetailRequest.title();
        this.content = projectDetailRequest.content();
        this.startDate = projectDetailRequest.startDate();
        this.endDate = projectDetailRequest.endDate();
        this.alarmSet = projectDetailRequest.alarmSet();
        this.updateDate = new Date();
    }
}
