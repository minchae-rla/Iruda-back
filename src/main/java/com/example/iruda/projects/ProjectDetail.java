package com.example.iruda.projects;

import com.example.iruda.projects.dto.ProjectDetailRequest;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PrimaryKey로 설정
    private Long id;

    @JoinColumn(name = "project_id", nullable = false)
    private Long projectId;  // 프로젝트 ID

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @Column(nullable = false)
    private Date createDate;

    @Column(nullable = false)
    private Date updateDate;

    @Column(nullable = false)
    private String alarmSet;

    public ProjectDetail(ProjectDetailRequest projectDetailRequest, Long projectId) {
        this.projectId = projectDetailRequest.projectId();
        this.title = projectDetailRequest.title();
        this.content = projectDetailRequest.content();
        this.startDate = projectDetailRequest.startDate();
        this.endDate = projectDetailRequest.endDate();
        this.alarmSet = projectDetailRequest.alarmSet();
        this.createDate = new Date();
        this.updateDate = new Date();
    }

    public void update(ProjectDetailRequest projectDetailRequest) {
        this.projectId = projectDetailRequest.projectId();
        this.title = projectDetailRequest.title();
        this.content = projectDetailRequest.content();
        this.startDate = projectDetailRequest.startDate();
        this.endDate = projectDetailRequest.endDate();
        this.alarmSet = projectDetailRequest.alarmSet();
        this.updateDate = new Date();
    }
}

