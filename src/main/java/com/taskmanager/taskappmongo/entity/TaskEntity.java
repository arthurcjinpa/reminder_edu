package com.taskmanager.taskappmongo.entity;

import com.taskmanager.taskappmongo.enums.PriorityLevel;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Document
public class TaskEntity {

    @Id
    private String id;

    @Indexed
    private String title;

    @TextIndexed
    private String description;

    @Indexed
    private PriorityLevel priorityLevel;

    private String userId;

    private LocalDateTime reminderDate;

    @Indexed
    @CreatedDate
    @Field("due_date")
    private LocalDateTime creationDate;
}
