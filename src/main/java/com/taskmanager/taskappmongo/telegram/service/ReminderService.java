package com.taskmanager.taskappmongo.telegram.service;

import org.springframework.stereotype.Service;

@Service
public interface ReminderService {

    void setReminder(String taskId);

    void snoozeReminder(String taskId, int time, String timeUnit);
}
