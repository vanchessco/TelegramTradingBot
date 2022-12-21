package com.mi.message;


import account.Account;
import com.mi.rabbitmq.producer.account.TelegramAccountProducer;
import com.mi.rabbitmq.producer.instrument.TelegramInstrumentProducer;
import com.mi.rabbitmq.producer.operation.TelegramOperationProducer;
import com.mi.rabbitmq.producer.order.TelegramOrderProducer;
import instrument.Instrument;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Data
@AllArgsConstructor
public class MessageSender {

    private TelegramAccountProducer accountProducer;
    private TelegramInstrumentProducer instrumentProducer;
    private TelegramOrderProducer orderProducer;
    private TelegramOperationProducer operationProducer;
    private MessageConverter messageConverter;


    public SendDocument sendOperations(String accountID, Message message) {
        String response = operationProducer.getOperations(accountID);
        String accountName = accountProducer.getAccount(accountID).getName();
        SendDocument sendDocument = null;
        if (response.equalsIgnoreCase("done")) {
            InputFile inputFile = new InputFile(Path.of(accountName + ".txt").toFile());
            sendDocument = SendDocument.builder()
                    .chatId(message.getChatId())
                    .document(inputFile)
                    .build();
        }


        return sendDocument;
    }

    public SendMessage sendStartInfo(Message message) {
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/accounts", "Счета"));
        commands.add(new BotCommand("/schedule", "Расписание торговой площадки"));
        commands.add(new BotCommand("/effective_portfolio", "Эффективный портфель"));
        commands.add(new BotCommand("/trading_positions", "Позиции портфеля доступные для торговли"));
        commands.add(new BotCommand("/trading_shares", "Акции для торговли"));

        StringBuilder startInfo = new StringBuilder();
        startInfo.append("Список команд бота:").append("\n");
        commands.stream().forEach(command -> {
            startInfo.append(command.getCommand()).append(" - ").append(command.getDescription()).append("\n");
        });
        SendMessage sendMessage = SendMessage.builder()
                .chatId(message.getChatId())
                .text(startInfo.toString())
                .build();

        return sendMessage;
    }

    public List<SendMessage> sendAccounts(Message message) {
        List<SendMessage> messages = new ArrayList<>();
        List<Account.TinkoffAccount> accounts = accountProducer.getAccounts();
        accounts.forEach(account -> {
            String accountInfo = messageConverter.convertAccount(account);

            var positionButton = new InlineKeyboardButton();
            positionButton.setText("Позиции");
            positionButton.setCallbackData(account.getId());
            var resetButton = new InlineKeyboardButton();
            resetButton.setText("Обновить");
            resetButton.setCallbackData(account.getId() + "," + "reset_account");
            var operationButton = new InlineKeyboardButton();
            operationButton.setText("Отчет");
            operationButton.setCallbackData(account.getId() + "," + "operations");
            InlineKeyboardMarkup markup = sendKeyboard(positionButton, resetButton, operationButton);

            SendMessage sendMessage = SendMessage.builder()
                    .chatId(message.getChatId())
                    .text(accountInfo)
                    .replyMarkup(markup)
                    .build();
            messages.add(sendMessage);
        });
        return messages;
    }

    public SendMessage sendAccount(Account.TinkoffAccount account, Message message) {
        var positionButton = new InlineKeyboardButton();
        positionButton.setText("Позиции");
        positionButton.setCallbackData(account.getId() + ",");
        var resetButton = new InlineKeyboardButton();
        resetButton.setText("Обновить");
        resetButton.setCallbackData(account.getId() + "," + "reset_account");
        var operationButton = new InlineKeyboardButton();
        operationButton.setText("Отчет");
        operationButton.setCallbackData(account.getId() + "," + "operations");
        InlineKeyboardMarkup markup = sendKeyboard(positionButton, resetButton, operationButton);

        String accountInfo = messageConverter.convertAccount(account);
        SendMessage sendMessage = SendMessage.builder()
                .chatId(message.getChatId())
                .text(accountInfo)
                .replyMarkup(markup)
                .build();

        return sendMessage;
    }


    public SendMessage sendEffectivePortfolio(Message message) {
        var buttonReset = new InlineKeyboardButton();
        buttonReset.setText("Обновить");
        buttonReset.setCallbackData("reset" + "," + "effective_portfolio");
        var markup = sendKeyboard(buttonReset);
        SendMessage sendMessage = SendMessage.builder()
                .chatId(message.getChatId())
                .text(messageConverter.convertEffectivePortfolio())
                .replyMarkup(markup)
                .build();

        return sendMessage;
    }

    public List<SendMessage> sendPositions(Message message) {
        List<SendMessage> messages = new ArrayList<>();
        List<Account.TinkoffAccount> accounts = accountProducer.getAccounts();
        SendMessage sendMessage = SendMessage.builder()
                .chatId(message.getChatId())
                .text("Позиции доступные для торговли:")
                .build();
        messages.add(sendMessage);
        accounts.forEach(account -> {
            String accountID = account.getId();
            List<Account.Position> positions = accountProducer.getPositions(accountID).stream()
                    .filter(Account.Position::getApiTradeAvailableFlag)
                    .toList();
            positions.forEach(position -> {
                String tradingPosition = messageConverter.convertPosition(account, position);

                var buttonMarketBuy = new InlineKeyboardButton();
                buttonMarketBuy.setText("Купить по рыночной");
                buttonMarketBuy.setCallbackData(accountID + "," + position.getFigi() + "," + "buy");

                var buttonMarketSell = new InlineKeyboardButton();
                buttonMarketSell.setText("Продать по рыночной");
                buttonMarketSell.setCallbackData(accountID + "," + position.getFigi() + "," + "sell");

                var markup = sendKeyboard(buttonMarketBuy, buttonMarketSell);

                SendMessage positionMessage = SendMessage.builder()
                        .chatId(message.getChatId())
                        .text(tradingPosition)
                        .replyMarkup(markup)
                        .build();
                messages.add(positionMessage);
            });
        });

        return messages;
    }

    public List<SendMessage> sendPositionsInfo(String accountID, Message message) {
        List<SendMessage> positions = new ArrayList<>();
        Account.TinkoffAccount account = accountProducer.getAccount(accountID);
        List<String> positionsInfo = accountProducer.getPositions(accountID)
                .stream()
                .map(position -> messageConverter.convertPosition(account, position))
                .toList();
        positionsInfo.forEach(a -> {
            SendMessage sendMessage = SendMessage.builder().chatId(message.getChatId()).text(a).build();
            positions.add(sendMessage);
        });
        return positions;
    }

    public SendMessage sendTickers(Message message) {
        List<String> tickers = instrumentProducer.getFirstLevelTickers();
        StringBuilder instrumentInfo = new StringBuilder();
        tickers.forEach(ticker -> {
            instrumentInfo.append(messageConverter.convertShareShort(ticker));
        });
        SendMessage sendMessage = SendMessage.builder()
                .chatId(message.getChatId())
                .text(instrumentInfo.toString())
                .build();

        return sendMessage;
    }

    public List<SendMessage> sendShareInfo(Message message) {
        List<SendMessage> messages = new ArrayList<>();
        List<Account.TinkoffAccount> accounts = accountProducer.getAccounts();
        String ticker = message.getText().substring(1);
        Instrument.TinkoffInstrument instrument = instrumentProducer.getInstrument(ticker);
        SendMessage sendMessage = SendMessage.builder()
                .chatId(message.getChatId())
                .text("Выберите счёт на который хотите купить инструмент." + "\n"
                        + "Наименование счёта находится в строке \"Брокерский счёт\".")
                .build();
        messages.add(sendMessage);

        accounts.forEach(brokerAccount -> {
            String instrumentInfo = messageConverter.convertShare(brokerAccount, instrument);
            var buttonBuy = new InlineKeyboardButton();
            buttonBuy.setText("Купить по рыночной");
            buttonBuy.setCallbackData(brokerAccount.getId() + "," + instrument.getFigi() + "," + "buy");
            var markup = sendKeyboard(buttonBuy);

            SendMessage shareInfo = SendMessage.builder()
                    .chatId(message.getChatId())
                    .text(instrumentInfo)
                    .replyMarkup(markup)
                    .build();
            messages.add(shareInfo);
        });

        return messages;
    }

    public SendMessage sendSchedule(Message message) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(message.getChatId())
                .text(messageConverter.convertSchedule())
                .build();
        return sendMessage;
    }

    public InlineKeyboardMarkup sendKeyboard(InlineKeyboardButton... button) {
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>(Arrays.asList(button));

        rowsInLine.add(rowInLine);
        markupInLine.setKeyboard(rowsInLine);

        return markupInLine;
    }

    public SendMessage sendOrder(String action, String data, Message message) {
        LocalDateTime date = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        LocalTime time = LocalTime.of(date.getHour(), date.getMinute());
        SendMessage sendMessage = null;
        if (!date.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            if (time.isAfter(LocalTime.of(10, 0)) && time.isBefore(LocalTime.of(18, 40))) {
                String response = orderProducer.getOrderResponse(action, data);
                switch (action) {
                    case "buy" ->
                            sendMessage = SendMessage.builder().chatId(message.getChatId()).text("Куплено:" + response).build();
                    case "sell" ->
                            sendMessage = SendMessage.builder().chatId(message.getChatId()).text("Продано:" + response).build();
                }

            } else {
                sendMessage = SendMessage.builder().chatId(message.getChatId()).text("Биржа закрыта").build();
            }
        } else {
            sendMessage = SendMessage.builder().chatId(message.getChatId()).text("Биржа закрыта").build();
        }

        return sendMessage;
    }


}
