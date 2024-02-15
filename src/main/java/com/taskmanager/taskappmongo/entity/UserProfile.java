package com.taskmanager.taskappmongo.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Builder
@Document
public class UserProfile {
    @Id
    private String id;

    private List<TaskEntity> tasks;

    @Indexed
    @Field("chat_id")
    private final long chatId;
}
