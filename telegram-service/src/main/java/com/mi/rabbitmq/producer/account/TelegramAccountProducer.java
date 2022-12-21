package com.mi.rabbitmq.producer.account;


import account.Account;

import java.util.List;

public interface TelegramAccountProducer {

    List<Account.TinkoffAccount> getAccounts();

    Account.TinkoffAccount getAccount(String accountID);

    List<Account.Position> getPositions(String accountID);

}
