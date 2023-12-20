package com.taskmanager.taskappmongo.telegram.service.impl;

import com.taskmanager.taskappmongo.entity.TaskEntity;
import com.taskmanager.taskappmongo.service.TaskService;
import com.taskmanager.taskappmongo.telegram.TaskTelegramBot;
import com.taskmanager.taskappmongo.entity.UserProfile;
import com.taskmanager.taskappmongo.telegram.service.ReminderService;
import com.taskmanager.taskappmongo.service.UserService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final TaskTelegramBot taskTelegramBot;
    private final TaskService taskService;
    private final UserService userService;

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private final InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();


    @Override
    public void setReminder(String taskId) {
        TaskEntity task = taskService.getTaskById(taskId);
        long delayInSeconds = calculateDelayInSeconds(task.getReminderDate());

        executorService.schedule(() -> {
            sendReminderNotification(task.getUserId(), taskId);
        }, delayInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void snoozeReminder(String taskId, int time, String timeUnit) {
        TaskEntity task = taskService.getTaskById(taskId);
        TaskEntity updatedTask = taskService.updateTaskReminderTime(task.getUserId(), taskId, time, timeUnit);
        long delayInSeconds = calculateDelayInSeconds(updatedTask.getReminderDate());

        executorService.schedule(() -> {
            sendReminderNotification(updatedTask.getUserId(), taskId);
        }, delayInSeconds, TimeUnit.SECONDS);
    }

    private void sendReminderNotification(String userId, String taskId) {
        TaskEntity task = taskService.getTaskById(taskId);
        UserProfile user = userService.getUserById(userId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getChatId());
        sendMessage.setText("Reminder: " + task.getTitle());

        List<List<InlineKeyboardButton>> InlineKeyboard = new ArrayList<>();

        InlineKeyboardButton snoozeButton = new InlineKeyboardButton();
        snoozeButton.setText("Snooze for 1 minute");
        snoozeButton.setCallbackData("snooze-" + taskId);

        InlineKeyboardButton okButton = new InlineKeyboardButton();
        okButton.setText("OK");
        okButton.setCallbackData("OK");

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(snoozeButton);
        rowInline.add(okButton);

        InlineKeyboard.add(rowInline);
        markupInline.setKeyboard(InlineKeyboard);

        sendMessage.setReplyMarkup(markupInline);

        try {
            taskTelegramBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private long calculateDelayInSeconds(LocalDateTime reminderDateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        long currentEpochSeconds = currentDateTime.toEpochSecond(ZoneOffset.UTC);
        long reminderEpochSeconds = reminderDateTime.toEpochSecond(ZoneOffset.UTC);
        return Math.max(0, reminderEpochSeconds - currentEpochSeconds);
    }

    @PreDestroy
    public void shutdownExecutorService() {
        executorService.shutdown();
    }
}
