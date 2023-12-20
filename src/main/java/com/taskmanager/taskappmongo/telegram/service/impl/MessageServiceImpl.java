package com.taskmanager.taskappmongo.telegram.service.impl;

import com.taskmanager.taskappmongo.telegram.service.MessageService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

@Service
public class MessageServiceImpl implements MessageService {

    private final InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

    @Override
    public SendMessage sendRegisterMessage(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Click the 'Register' button to register");

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        InlineKeyboardButton registerButton = new InlineKeyboardButton();
        registerButton.setText("Register");
        registerButton.setCallbackData("register");

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(registerButton);

        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);

        message.setReplyMarkup(markupInline);

        return message;
    }

    @Override
    public EditMessageText createEditMessageText(String message, Update update) {
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        EditMessageText newMessage = new EditMessageText();
        newMessage.setChatId(chatId);
        newMessage.setMessageId(toIntExact(messageId));
        newMessage.setText(message);

        return newMessage;
    }
}
