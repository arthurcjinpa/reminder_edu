package com.taskmanager.taskappmongo.telegram.configuration;

import com.taskmanager.taskappmongo.telegram.TaskTelegramBot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TaskTelegramBotConfiguration {

    @Bean
    public TelegramBotsApi telegramBotsApi(TaskTelegramBot taskTelegramBot) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(taskTelegramBot);
        return telegramBotsApi;
    }

}
