package com.taskmanager.taskappmongo.service;

import com.taskmanager.taskappmongo.entity.TaskEntity;
import com.taskmanager.taskappmongo.entity.UserProfile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    void registerUser(long chatId);

    UserProfile getUserById(String id);

    String getUserIdByChatId(long chatId);

    List<UserProfile> getAllUsers();

    void updateOrAddTaskToUser(TaskEntity task);
}
