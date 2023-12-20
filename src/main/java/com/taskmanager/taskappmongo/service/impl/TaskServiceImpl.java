package com.taskmanager.taskappmongo.service.impl;

import com.taskmanager.taskappmongo.entity.TaskEntity;
import com.taskmanager.taskappmongo.exception.TaskNotFoundException;
import com.taskmanager.taskappmongo.repository.TaskRepository;
import com.taskmanager.taskappmongo.service.TaskService;
import com.taskmanager.taskappmongo.telegram.service.ReminderService;
import com.taskmanager.taskappmongo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;


@Service
public class TaskServiceImpl implements TaskService {
    private static final Logger LOGGER = Logger.getLogger(TaskServiceImpl.class.getName());

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ReminderService reminderService;

    public TaskServiceImpl(TaskRepository taskRepository,
                           UserService userService,
                           @Lazy ReminderService reminderService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.reminderService = reminderService;
    }

    @Override
    public TaskEntity saveTask(TaskEntity taskEntity) {
        TaskEntity savedTask = taskRepository.save(taskEntity);
        userService.updateOrAddTaskToUser(savedTask);
        reminderService.setReminder(savedTask.getId());
        LOGGER.info("New task with id: " + taskEntity.getId() + " was successfully saved");
        return savedTask;
    }

    @Override
    public TaskEntity updateTaskReminderTime(String userId, String taskId, int time, String timeUnit) {
        TaskEntity foundTask = getTaskByUserId(userId, taskId);
        LocalDateTime newDate = (timeUnit.equals("minute"))
                ? LocalDateTime.now().plusMinutes(time)
                : LocalDateTime.now().plusHours(time);

        foundTask.setReminderDate(newDate);
//        TaskEntity first = user.getTasks().stream()
//                .filter(task -> task.getId().equals(foundTask.getId()))
//                .findFirst().orElseThrow(
//                        () -> new TaskNotFoundException(
//                                "Task with id " + foundTask
//                                        + " was not found in user with id "
//                                        + userId + ", list"));
        TaskEntity savedTask = taskRepository.save(foundTask);
        userService.updateOrAddTaskToUser(savedTask);
        return savedTask;
    }

    @Override
    public TaskEntity getTaskByUserId(String userId, String taskId) {
        return taskRepository.findTaskEntityByUserIdAndId(userId, taskId)
                .orElseThrow(
                        () -> new TaskNotFoundException(
                                "Task with user id " + userId + " was not found."));
    }

    @Override
    public TaskEntity getTaskById(String taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(
                        () -> new TaskNotFoundException(
                                "Task with id " + taskId + " was not found."));
    }

    @Override
    public List<TaskEntity> getAllTasks() {
        List<TaskEntity> allTasks = taskRepository.findAll();
        LOGGER.info("Retrieving all " + allTasks.size() + " tasks.");
        return allTasks;
    }

    @Override
    public ResponseEntity<String> deleteTaskById(String taskId) {
        LOGGER.info("Deleting task with id " + taskId);
        taskRepository.deleteById(taskId);
        return ResponseEntity.ok("Successfully deleted");
    }

    @Override
    public List<TaskEntity> filterByInstrument(String instrument) {
        if (instrument.equals("date")) {
            return taskRepository.findAllByOrderByCreationDateDesc();
        }
        return null;
    }


}
