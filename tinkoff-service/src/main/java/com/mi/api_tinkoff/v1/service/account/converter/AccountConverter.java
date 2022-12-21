package com.mi.api_tinkoff.v1.service.account.converter;


import account.Account;
import ru.tinkoff.piapi.core.models.Position;

import java.util.List;

public interface AccountConverter {

    Account.TinkoffAccount convertAccount(ru.tinkoff.piapi.contract.v1.Account a);

    List<Account.Position> convertPositions(List<Position> positionList);


}
