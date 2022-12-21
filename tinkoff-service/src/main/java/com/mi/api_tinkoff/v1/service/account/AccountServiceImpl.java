package com.mi.api_tinkoff.v1.service.account;

import com.mi.api_tinkoff.config.tinkoff_api_config.TinkoffContextProvider;
import com.mi.api_tinkoff.v1.service.account.converter.AccountConverter;
import com.mi.api_tinkoff.v1.service.converter.ConverterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.Account;
import ru.tinkoff.piapi.core.models.Position;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private TinkoffContextProvider contextProvider;
    private AccountConverter accountConverter;


    @Override
    public byte[] findAccounts() {
        List<Account> accounts = contextProvider.getUsersService().getAccountsSync();
        List<account.Account.TinkoffAccount> protoAccounts = accounts.stream().map(account -> accountConverter.convertAccount(account)).collect(Collectors.toList());
        byte[] converted = ConverterService.serialize(protoAccounts);
        return converted;
    }

    @Override
    public byte[] findAccount(String accountID) {
        byte[] converted = null;
        if (isIdValid(accountID)) {
            List<Account> accountList = contextProvider.getUsersService().getAccountsSync();
            Account account = accountList.stream().filter(acc -> acc.getId().equalsIgnoreCase(accountID)).findFirst().get();
            converted = ConverterService.serialize(accountConverter.convertAccount(account));
        }


        return converted;
    }

    @Override
    public byte[] findPositions(String accountID) {
        byte[] converted = null;
        if (isIdValid(accountID)) {
            List<Position> positionList = contextProvider.getOperationsService().getPortfolioSync(accountID).getPositions();
            List<account.Account.Position> protoPositionList = accountConverter.convertPositions(positionList);
            converted = ConverterService.serialize(protoPositionList);
        }


        return converted;
    }

    private boolean isIdValid(String accountID) {
        Account account = null;
        if (accountID != null && !accountID.isEmpty()) {
            account = contextProvider.getUsersService().getAccountsSync()
                    .stream()
                    .filter(a -> a.getId().equalsIgnoreCase(accountID))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Неверный идентификатор счета: " + accountID));
        }

        return account != null;
    }


}
