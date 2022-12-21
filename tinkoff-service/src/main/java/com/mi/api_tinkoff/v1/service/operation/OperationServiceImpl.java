package com.mi.api_tinkoff.v1.service.operation;


import com.mi.api_tinkoff.config.tinkoff_api_config.TinkoffContextProvider;
import com.mi.api_tinkoff.v1.service.operation.converter.OperationConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.Account;
import ru.tinkoff.piapi.contract.v1.Operation;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
@AllArgsConstructor
public class OperationServiceImpl implements OperationService {

    private TinkoffContextProvider contextProvider;
    private OperationConverter operationConverter;


    @Override
    public byte[] findOperations(String accountID) {
        Instant from = Instant.now().minus(30, ChronoUnit.DAYS);
        Instant to = Instant.now();
        List<Operation> operations = contextProvider.getOperationsService().getAllOperationsSync(accountID, from, to);
        List<String> operationsInfo;
        String accountName = contextProvider.getUsersService().getAccountsSync().stream().filter(account -> account.getId().equalsIgnoreCase(accountID)).map(Account::getName).findFirst().get();
        if (!operations.isEmpty()) {
            operationsInfo = operationConverter.convert(operations);
            String response = operationConverter.writeToFile(accountName, operationsInfo);
            return response.getBytes();
        }

        return "Список операций отсутствует".getBytes();
    }

}
