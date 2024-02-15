package com.taskmanager.taskappmongo.service;

import com.taskmanager.taskappmongo.entity.TaskEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {

    TaskEntity saveTask(TaskEntity taskEntity);

    TaskEntity updateTaskReminderTime(String userId, String taskId, int time, String timeUnit);

    TaskEntity getTaskByUserId(String userId, String taskId);

    TaskEntity getTaskById(String taskId);

    List<TaskEntity> getAllTasks();

    ResponseEntity<String> deleteTaskById(String taskId);

    List<TaskEntity> filterByInstrument(String instrument);
}
