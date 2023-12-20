package com.taskmanager.taskappmongo.repository;

import com.taskmanager.taskappmongo.entity.TaskEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends MongoRepository<TaskEntity, String> {
    List<TaskEntity> findAllByOrderByCreationDateDesc();
    Optional<TaskEntity> findTaskEntityByUserIdAndId(String userId, String taskId);
}
