package com.mi.api_tinkoff.v1.service.operation.converter;

import ru.tinkoff.piapi.contract.v1.Operation;

import java.util.List;

public interface OperationConverter {
    List<String> convert(List<Operation> operationList);

    String writeToFile(String accountName, List<String> operationInfo);
}
