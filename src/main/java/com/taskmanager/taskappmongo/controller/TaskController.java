package com.taskmanager.taskappmongo.controller;

import com.taskmanager.taskappmongo.entity.TaskEntity;
import com.taskmanager.taskappmongo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public List<TaskEntity> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    public TaskEntity addTask(@RequestBody TaskEntity taskEntity) {
        return taskService.saveTask(taskEntity);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTaskById(@PathVariable(value = "taskId") String taskId) {
        return taskService.deleteTaskById(taskId);
    }

    @GetMapping("/filter/{instrument}")
    public List<TaskEntity> filterAllTasks(@PathVariable(value = "instrument") String instrument) {
        return taskService.filterByInstrument(instrument);
    }
}
