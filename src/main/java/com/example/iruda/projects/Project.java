package com.example.iruda.projects;

import com.example.iruda.projects.dto.ProjectRequest;
import com.example.iruda.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name="projects")
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //PrimaryKey로 설정
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY) //지연로딩 방식, 실제 사용전까지 DB에서 조회하지 않음
    @JoinColumn(name = "user_id", nullable = false) //외래키(FK) 설정
    private User user;

    public Project(ProjectRequest projectRequest, User user) {
        this.name = projectRequest.name();
        this.user = user;
    }

    public void update(ProjectRequest projectRequest) {
        this.name = projectRequest.name();
    }
}
