package com.taskmanager.taskappmongo.telegram.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public interface MessageService {
    SendMessage sendRegisterMessage(long chatId);

    EditMessageText createEditMessageText(String message, Update update);
}
