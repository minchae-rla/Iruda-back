package com.example.iruda.tasks;

import com.example.iruda.projects.Project;
import com.example.iruda.tasks.dto.TaskRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name="tasks")
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
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

    public task(TaskRequest taskRequest, Project project) {
        this.project = project;
        this.title = taskRequest.title();
        this.content = taskRequest.content();
        this.startDate = taskRequest.startDate();
        this.endDate = taskRequest.endDate();
        this.alarmSet = taskRequest.alarmSet();
        this.createDate = new Date();
        this.updateDate = new Date();


    }

    public void update(TaskRequest taskRequest, Project project) {
        this.project = project;
        this.title = taskRequest.title();
        this.content = taskRequest.content();
        this.startDate = taskRequest.startDate();
        this.endDate = taskRequest.endDate();
        this.alarmSet = taskRequest.alarmSet();
        this.updateDate = new Date();
    }
}
