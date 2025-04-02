package com.example.iruda.projects;

import com.example.iruda.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="projectMembers")
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "project_Id", nullable = false)
    private Long projectId;

    @JoinColumn(name = "user_Id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectPosition projectPosition;  // 역할

    public ProjectMember(Project project, User user, ProjectPosition projectPosition) {
        this.projectId = projectId;
        this.userId = userId;
        this.projectPosition = projectPosition;
    }
}
