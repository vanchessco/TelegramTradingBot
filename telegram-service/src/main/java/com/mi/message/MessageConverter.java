package com.mi.message;

import account.Account;
import com.mi.library.compiled_proto.schedule.ExchangeSchedule;
import com.mi.rabbitmq.producer.instrument.TelegramInstrumentProducer;
import instrument.Instrument;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class MessageConverter {

    private TelegramInstrumentProducer instrumentProducer;

    public String convertAccount(Account.TinkoffAccount account) {
        StringBuilder accountInfo = new StringBuilder();
        accountInfo.append("Счет: ").append(account.getName()).append("\n")
                .append("Остаток: ").append(account.getWithdrawLimitList().getWithdrawLimitList().get(0).getPrice())
                .append(" ")
                .append(account.getWithdrawLimitList().getWithdrawLimitList().get(0).getCurrency()).append("\n");
        return accountInfo.toString();
    }

    public String convertEffectivePortfolio() {
        Map<String, Map<BigDecimal, Integer>> effectivePortfolio = instrumentProducer.getEffectivePortfolio();
        StringBuilder portfolioInfo = new StringBuilder();
        for (Map.Entry<String, Map<BigDecimal, Integer>> map : effectivePortfolio.entrySet()) {
            String key = map.getKey();
            Map<BigDecimal, Integer> value = map.getValue();
            String valueKey = value.keySet().toString();
            String valueValue = value.values().toString();
            portfolioInfo
                    .append(key)
                    .append(": ")
                    .append("\n")
                    .append("Cумма инвестиций: ")
                    .append(valueKey, 1, valueKey.length() - 1)
                    .append("\n")
                    .append("Кол-во акций в шт: ")
                    .append(valueValue, 1, valueValue.length() - 1)
                    .append("\n");

        }
        return portfolioInfo.toString();
    }


    public String convertPosition(Account.TinkoffAccount account, Account.Position position) {
        StringBuilder positionInfo = new StringBuilder();
        var quantity = position.getQuantity().substring(0, position.getQuantity().indexOf("."));
        if (!position.getInstrumentType().equalsIgnoreCase("currency")) {
            positionInfo
                    .append("Cчёт: ").append(account.getName()).append("\n")
                    .append("Тип: ").append(position.getInstrumentType()).append("\n")
                    .append("Кол-во: ").append(quantity).append("\n")
                    .append("Лоты: ").append(position.getQuantityLots()).append("\n")
                    .append("Цена за инструмент: ").append(position.getCurrentPrice().getPrice()).append(" ").append(position.getCurrentPrice().getCurrency()).append("\n")
                    .append("Цена за лот: ").append(position.getCurrentLotPrice().getPrice()).append(" ").append(position.getCurrentLotPrice().getCurrency()).append("\n")
                    .append("Доходность: ").append(position.getExpectedYield()).append(" ").append("%").append("\n");
        } else {
            positionInfo
                    .append("Cчет: ").append(account.getName()).append("\n")
                    .append("Тип: ").append(position.getInstrumentType()).append("\n")
                    .append("Кол-во: ").append(position.getQuantity()).append(" ")
                    .append("rub");
        }

        return positionInfo.toString();
    }


    public String convertShare(Account.TinkoffAccount account, Instrument.TinkoffInstrument instrument) {
        StringBuilder shareInfo = new StringBuilder();
        shareInfo.append("Наименование: ").append(instrument.getName()).append("\n")
                .append("Страна: ").append(instrument.getCountryOfRiskName()).append("\n")
                .append("Валюта рассчётов: ").append(instrument.getCurrency()).append("\n")
                .append("Штук в лоте: ").append(instrument.getLot()).append("\n")
                .append("Цена за инструмент: ").append(instrument.getCurrentInstrumentPrice().getPrice())
                .append(" ")
                .append(instrument.getCurrentInstrumentPrice().getCurrency()).append("\n")
                .append("Цена за лот: ").append(instrument.getCurrentLotPrice().getPrice())
                .append(" ")
                .append(instrument.getCurrentLotPrice().getCurrency()).append("\n")
                .append("Счёт: ").append(account.getName()).append("\n")
                .append("Доступный остаток: ").append(account.getWithdrawLimitList().getWithdrawLimitList().get(0).getPrice())
                .append(" ")
                .append(account.getWithdrawLimitList().getWithdrawLimitList().get(0).getCurrency()).append("\n");

        return shareInfo.toString();
    }

    public String convertShareShort(String ticker) {
        StringBuilder tickerInfo = new StringBuilder();
        tickerInfo.append("Тикер: ").append("/").append(ticker).append("\n");
        return tickerInfo.toString();
    }

    public String convertSchedule() {
        List<ExchangeSchedule.Schedule> schedule = instrumentProducer.getSchedule();
        StringBuilder scheduleInfo = new StringBuilder();
        for (int i = 0; i < schedule.size(); i++) {
            ExchangeSchedule.Schedule tr = schedule.get(i);
            scheduleInfo
                    .append("Дата: ").append(tr.getDate()).append("\n")
                    .append("Открытие биржи: ").append(tr.getStartTime()).append("\n")
                    .append("Закрытие биржи: ").append(tr.getEndTime()).append("\n")
                    .append(" ");

        }

        return scheduleInfo.toString();
    }
}
