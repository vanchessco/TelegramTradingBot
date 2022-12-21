package com.mi.config;


import account.Account;
import com.mi.message.MessageSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


@Service
@AllArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private MessageSender messageSender;

    @Override
    public String getBotUsername() {
        return "TradingBot";
    }

    @Override
    public String getBotToken() {
        return "token";
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            checkCallBackQuery(update);
        } else if (update.hasMessage()) {
            Message message = update.getMessage();
            String messageText = update.getMessage().getText();
            if (message.isCommand()) {
                if (Character.isUpperCase(messageText.codePointAt(1))) {
                    List<SendMessage> shareMessageList = messageSender.sendShareInfo(message);
                    shareMessageList.forEach(shareMessage -> {
                        try {
                            execute(shareMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    });
                }
                try {
                    switch (messageText) {
                        case "/start" -> {
                            SendMessage sendMessage = messageSender.sendStartInfo(message);
                            execute(sendMessage);
                        }
                        case "/accounts" -> {
                            List<SendMessage> sendMessage = messageSender.sendAccounts(message);
                            sendMessage.forEach(accountInfo -> {
                                try {
                                    execute(accountInfo);
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                        case "/effective_portfolio" -> {
                            SendMessage sendMessage = messageSender.sendEffectivePortfolio(message);
                            execute(sendMessage);
                        }
                        case "/trading_positions" -> {
                            List<SendMessage> sendMessage = messageSender.sendPositions(message);
                            sendMessage.forEach(positionInfo -> {
                                try {
                                    execute(positionInfo);
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                        case "/trading_shares" -> {
                            SendMessage sendMessage = messageSender.sendTickers(message);
                            execute(sendMessage);
                        }
                        case "/schedule" -> {
                            SendMessage sendMessage = messageSender.sendSchedule(message);
                            execute(sendMessage);
                        }
                    }
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    public void checkCallBackQuery(Update update) {
        try {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            Message message = callbackQuery.getMessage();
            String[] dataSplit = data.split(",");
            if (dataSplit.length == 1) {
                List<SendMessage> sendMessage = messageSender.sendPositionsInfo(data, message);
                sendMessage.forEach(positionMessage -> {
                    try {
                        execute(positionMessage);
                    } catch (TelegramApiException e) {
                    }
                });
            } else if (data.contains("reset")) {
                if (dataSplit[1].equalsIgnoreCase("effective_portfolio")) {
                    messageSender.getInstrumentProducer().resetEffectivePortfolio();
                    var buttonReset = new InlineKeyboardButton();
                    buttonReset.setText("Обновить");
                    buttonReset.setCallbackData("reset_effective_portfolio");
                    var markup = messageSender.sendKeyboard(buttonReset);
                    execute(SendMessage.builder()
                            .chatId(message.getChatId())
                            .text(messageSender.getMessageConverter().convertEffectivePortfolio())
                            .replyMarkup(markup)
                            .build());

                } else if (data.endsWith("reset_account")) {
                    String accountID = dataSplit[0];
                    Account.TinkoffAccount account = messageSender.getAccountProducer().getAccount(accountID);
                    SendMessage sendMessage = messageSender.sendAccount(account, message);
                    execute(sendMessage);
                }

            } else if (data.endsWith("operations")) {
                String accountID = dataSplit[0];
                SendDocument sendDocument = messageSender.sendOperations(accountID, message);
                execute(sendDocument);

            } else if (data.endsWith("buy")) {
                SendMessage sendMessage = messageSender.sendOrder("buy", data, message);
                execute(sendMessage);
            } else if (data.endsWith("sell")) {
                SendMessage sendMessage = messageSender.sendOrder("sell", data, message);
                execute(sendMessage);
            } else {
                SendMessage sendMessage = SendMessage.builder().chatId(message.getChatId()).text(data).build();
                execute(sendMessage);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}
