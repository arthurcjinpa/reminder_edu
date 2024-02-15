package com.taskmanager.taskappmongo.service.impl;

import com.taskmanager.taskappmongo.entity.TaskEntity;
import com.taskmanager.taskappmongo.entity.UserProfile;
import com.taskmanager.taskappmongo.service.UserService;
import com.taskmanager.taskappmongo.telegram.exception.UserNotFoundException;
import com.taskmanager.taskappmongo.telegram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());

    private final UserRepository userRepository;

    @Override
    public void registerUser(long chatId) {
        UserProfile newUser = UserProfile.builder()
                .chatId(chatId)
                .tasks(Collections.emptyList()) //TODO: is there a better way to make default empty list?
                .build();
        userRepository.save(newUser);
        LOGGER.info("User with id " + newUser.getId() + " successfully saved.");
    }

    @Override
    public List<UserProfile> getAllUsers() {
        List<UserProfile> allUsers = userRepository.findAll();
        LOGGER.info("Retrieving all " + allUsers.size() + " users.");
        return allUsers;
    }

    @Override
    public void updateOrAddTaskToUser(TaskEntity task) {
        UserProfile user = getUserById(task.getUserId());

        if (user.getTasks().contains(task)) {
            int index = user.getTasks().indexOf(task);
            user.getTasks().set(index, task);
            userRepository.save(user);
            LOGGER.info("Task with title " + task.getTitle()
                    + " was successfully updated to the user with id " + user.getId());
        } else {
            user.getTasks().add(task);
            task.setUserId(user.getId());
            userRepository.save(user);
            LOGGER.info("Task with title " + task.getTitle()
                    + " was successfully added to the user with id " + user.getId());
        }
    }

    @Override
    public UserProfile getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new UserNotFoundException(
                                "User with id " + userId + " was not found."));
    }

    @Override
    public String getUserIdByChatId(long chatId) {
        return userRepository.findIdByChatId(chatId)
                .orElseThrow(
                        () -> new UserNotFoundException(
                                "User with chat id " + chatId + " was not found."));
    }
}
