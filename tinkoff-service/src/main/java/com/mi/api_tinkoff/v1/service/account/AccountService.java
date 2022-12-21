package com.mi.api_tinkoff.v1.service.account;


public interface AccountService {

    byte[] findAccounts();

    byte[] findAccount(String accountID);

    byte[] findPositions(String accountID);

}
