package com.taskmanager.taskappmongo.telegram;

import com.taskmanager.taskappmongo.telegram.service.MessageService;
import com.taskmanager.taskappmongo.telegram.service.ReminderService;
import com.taskmanager.taskappmongo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Map;

import static com.taskmanager.taskappmongo.telegram.utility.Converter.convertStringToMap;

@Component
public class TaskTelegramBot extends TelegramLongPollingBot {

    private final UserService userService;
    private final MessageService messageService;
    private final ReminderService reminderService;

    private long chatId;

    @Autowired
    public TaskTelegramBot(@Value("${bot.token}") String botToken,
                           UserService userService,
                           MessageService messageService,
                           @Lazy ReminderService reminderService) {
        super(botToken);
        this.userService = userService;
        this.messageService = messageService;
        this.reminderService = reminderService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // Text message
        if (isMessageWithText(update)) {
            String messageText = update.getMessage().getText();
            chatId = update.getMessage().getChatId();
            if ("/start".equals(messageText)) {
                processStartMessage();
            }
            // Buttons
        } else if (update.hasCallbackQuery()) {
            String callData = update.getCallbackQuery().getData();
            Map<String, String> callDataMap = convertStringToMap(callData);
            if (callDataMap.containsKey("register")) {
                registerAnswerButton(update);
            } else if (callData.contains("snooze")) {
                reminderSnoozeAnswerButton(update);
            } else if (callData.contains("OK")) {
                reminderOkAnswerButton(update);
            }
        }
    }

    private void reminderOkAnswerButton(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        EditMessageReplyMarkup editMessage = new EditMessageReplyMarkup();
        editMessage.setChatId(callbackQuery.getMessage().getChatId());
        editMessage.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessage.setReplyMarkup(new InlineKeyboardMarkup(new ArrayList<>()));
        try {
            execute(editMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void reminderSnoozeAnswerButton(Update update) {
        String taskId = "";
        String callData = update.getCallbackQuery().getData();
        Map<String, String> callDataMap = convertStringToMap(callData);

        String snoozeUnit = "minute";
        int snoozeTime;

        if (callDataMap.containsKey("snooze15")) {
            taskId = callDataMap.get("snooze15");
            snoozeTime = 15;
        } else if (callDataMap.containsKey("snooze30")) {
            taskId = callDataMap.get("snooze15");
            snoozeTime = 30;
        } else {
            snoozeUnit = "hour";
            taskId = callDataMap.get("snooze1");
            snoozeTime = 1;
        }

        reminderService.snoozeReminder(taskId, snoozeTime, snoozeUnit);
        String answer = "You will be notified later";

        EditMessageText newMessage =
                messageService.createEditMessageText(answer, update);
        try {
            execute(newMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void registerAnswerButton(Update update) {
        String answer = "Thank you for registering";
        userService.registerUser(chatId);
        EditMessageText newMessage =
                messageService.createEditMessageText(answer, update);
        try {
            execute(newMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void processStartMessage() {
        SendMessage registrationMessage = messageService.sendRegisterMessage(chatId);
        try {
            execute(registrationMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private boolean isMessageWithText(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }

    @Override
    public String getBotUsername() {
        return "TuriqueTaskBot";
    }

    public void sendTaskNotification(long chatId, String message) {
        try {
            execute(new SendMessage(String.valueOf(chatId), message));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}