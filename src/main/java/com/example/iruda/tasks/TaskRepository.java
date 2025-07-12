package com.example.iruda.tasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByProjectId(Long projectId);

    @Query("SELECT t FROM Task t " + "JOIN t.project p " + "JOIN p.projectMembers pm " + "WHERE pm.user.id = :userId " + "AND t.endDate = CURRENT_DATE " + "AND t.alarmSet = 'ON'")
    List<Task> findTodayAlarmsByUserId(@Param("userId") Long userId);
}
