package com.example.iruda.tasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByProjectId(Long projectId);

    @Query("SELECT t FROM Task t " + "JOIN t.project p " + "JOIN p.projectMembers pm " + "WHERE pm.user.id = :userId")
    List<Task> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Task t " + "JOIN t.project p " + "JOIN p.projectMembers pm " + "WHERE pm.user.id = :userId " + "AND FUNCTION('DATE', t.endDate) = :date")
    List<Task> findTaskByUserIdAndEndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    @Query("SELECT t FROM Task t " + "JOIN t.project p " + "JOIN p.projectMembers pm " + "WHERE pm.user.id = :userId " + "AND FUNCTION('DATE', t.endDate) < :date")
    List<Task> findCompleteTaskByUserIdAndEndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    @Query("SELECT t FROM Task t " + "JOIN t.project p " + "JOIN p.projectMembers pm " + "WHERE pm.user.id = :userId " + "AND t.endDate >= :dateStart AND t.endDate < :dateEnd " + "AND t.alarmSet = 'on'")
    List<Task> findTodayAlarmsByUserId(@Param("userId") Long userId, @Param("dateStart") LocalDateTime dateStart, @Param("dateEnd") LocalDateTime dateEnd);

    @Query("SELECT t FROM Task t " +
            "JOIN t.project p " +
            "JOIN p.projectMembers pm " +
            "WHERE pm.user.id = :userId " +
            "AND t.id IN :taskIds")
    List<Task> findByIdInAndUserId(@Param("taskIds") List<Long> taskIds, @Param("userId") Long userId);
}
