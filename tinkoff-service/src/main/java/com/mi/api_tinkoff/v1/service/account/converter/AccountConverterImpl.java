package com.mi.api_tinkoff.v1.service.account.converter;

import com.mi.api_tinkoff.config.tinkoff_api_config.TinkoffContextProvider;
import com.mi.api_tinkoff.v1.service.instrument.InstrumentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.Account;
import ru.tinkoff.piapi.core.models.Money;
import ru.tinkoff.piapi.core.models.Position;
import ru.tinkoff.piapi.core.models.WithdrawLimits;

import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountConverterImpl implements AccountConverter {

    private TinkoffContextProvider contextProvider;
    private InstrumentService instrumentService;

    @Override
    public account.Account.TinkoffAccount convertAccount(Account a) {
        WithdrawLimits withdrawLimit = contextProvider.getOperationsService().getWithdrawLimitsSync(a.getId());
        account.Account.TinkoffAccount protoAccount = account.Account.TinkoffAccount.newBuilder()
                .setId(a.getId())
                .setType(a.getType().name())
                .setName(a.getName())
                .setAccessLevel(a.getAccessLevel().name())
                .setStatus(a.getStatus().name())
                .setWithdrawLimitList(account.Account.TinkoffAccount.WithdrawLimitList.newBuilder()
                        .addAllWithdrawLimit(convertMoneyList(withdrawLimit.getMoney()))
                        .addAllBlocked(convertMoneyList(withdrawLimit.getBlocked()))
                        .addAllBlockedGuarantee(convertMoneyList(withdrawLimit.getBlockedGuarantee()))
                        .build())
                .build();

        return protoAccount;
    }

    @Override
    public List<account.Account.Position> convertPositions(List<Position> positionList) {
        List<account.Account.Position> positions = positionList.stream().map(position -> {
            var instrument = instrumentService.findInstrumentByFigi(position.getFigi());
            var lotPrice = (position.getCurrentPrice().getValue().multiply(position.getQuantity())).divide(position.getQuantityLots(), 2, RoundingMode.HALF_UP);
            var protoPosition = account.Account.Position.newBuilder()
                    .setTicker(instrument.getTicker())
                    .setFigi(position.getFigi())
                    .setInstrumentType(position.getInstrumentType())
                    .setQuantity(position.getQuantity().setScale(2, RoundingMode.HALF_UP).toString())
                    .setAveragePositionPrice(account.Account.Money.newBuilder()
                            .setPrice(position.getAveragePositionPrice().getValue().toString())
                            .setCurrency(position.getAveragePositionPrice().getCurrency())
                            .build())
                    .setExpectedYield(position.getExpectedYield().setScale(2, RoundingMode.HALF_UP).toString())
                    .setCurrentNkd(account.Account.Money.newBuilder()
                            .setPrice(position.getCurrentNkd().getValue().toString())
                            .setCurrency(position.getCurrentNkd().getCurrency())
                            .build())
                    .setAveragePositionPricePt(position.getAveragePositionPricePt().toString())
                    .setCurrentPrice(account.Account.Money.newBuilder()
                            .setPrice(position.getCurrentPrice().getValue().setScale(2, RoundingMode.HALF_UP).toString())
                            .setCurrency(position.getCurrentPrice().getCurrency())
                            .build())
                    .setCurrentLotPrice(account.Account.Money.newBuilder()
                            .setPrice(lotPrice.toString())
                            .setCurrency(position.getCurrentPrice().getCurrency()).build())
                    .setAveragePositionPriceFifo(account.Account.Money.newBuilder()
                            .setPrice(position.getAveragePositionPriceFifo().getValue().toString())
                            .setCurrency(position.getAveragePositionPriceFifo().getCurrency())
                            .build())
                    .setQuantityLots(position.getQuantityLots().setScale(0, RoundingMode.HALF_UP).toString())
                    .setSecurityTradingStatus(instrument.getTradingStatus().name())
                    .setBuyAvailableFlag(instrument.getBuyAvailableFlag())
                    .setSellAvailableFlag(instrument.getSellAvailableFlag())
                    .setApiTradeAvailableFlag(instrument.getApiTradeAvailableFlag())
                    .build();
            return protoPosition;
        }).collect(Collectors.toList());

        return positions;
    }

    private List<account.Account.Money> convertMoneyList(List<Money> moneyList) {
        List<account.Account.Money> protoMoney = moneyList.stream()
                .map(money -> account.Account.Money.newBuilder()
                        .setPrice(money.getValue().setScale(2, RoundingMode.HALF_UP).toString())
                        .setCurrency(money.getCurrency())
                        .build())
                .collect(Collectors.toList());

        return protoMoney;
    }

}
