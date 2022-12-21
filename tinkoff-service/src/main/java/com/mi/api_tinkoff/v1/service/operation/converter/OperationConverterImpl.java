package com.mi.api_tinkoff.v1.service.operation.converter;


import com.mi.api_tinkoff.v1.service.converter.ConverterService;
import com.mi.api_tinkoff.v1.service.instrument.InstrumentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.Operation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class OperationConverterImpl implements OperationConverter {

    private InstrumentService instrumentService;


    @Override
    public List<String> convert(List<Operation> operationList) {
        StringBuilder sb = new StringBuilder();
        List<String> converted = operationList.stream()
                .filter(operation -> operation.getPayment().getUnits() >= 0 && operation.getPayment().getNano() >= 0)
                .sorted(Comparator.comparing(o -> ConverterService.timeStampToLocalDateTime(o.getDate())))
                .map(o -> {
                    sb.delete(0, sb.length());
                    var payment = operation.Operation.Money.newBuilder()
                            .setPrice(ConverterService.moneyValueToBigDecimal(o.getPayment()).toString())
                            .setCurrency(o.getPayment().getCurrency())
                            .build();

                    if (!o.getFigi().isEmpty()) {
                        var instrument = instrumentService.findInstrumentByFigi(o.getFigi());
                        var price = operation.Operation.Money.newBuilder()
                                .setPrice(ConverterService.moneyValueToBigDecimal(o.getPrice()).toString())
                                .setCurrency(o.getPrice().getCurrency())
                                .build();

                        var date = ConverterService.timeStampToLocalDateTime(o.getDate());
                        sb
                                .append("\n")
                                .append("Наименование: ").append(instrument.getName()).append("\n")
                                .append("Цена операции за 1 инструмент: ").append(price.getPrice()).append(" ").append(price.getCurrency()).append("\n")
                                .append("Тип инструмента: ").append(o.getInstrumentType()).append("\n")
                                .append("Дата и время операции: ").append(date).append("\n")
                                .append("Тип операции: ").append(o.getOperationType()).append("\n")
                                .append("Количество единиц инструмента: ").append(o.getQuantity()).append("\n")
                                .append("Сумма операции: ").append(payment.getPrice()).append(" ").append(payment.getCurrency()).append("\n");
                    } else {
                        sb
                                .append("\n")
                                .append("Тип операции: ").append(o.getType()).append("\n")
                                .append("Сумма операции: ").append(payment.getPrice()).append(" ").append(payment.getCurrency()).append("\n");

                    }
                    return sb.toString();
                })
                .toList();

        return converted;
    }


    @Override
    public String writeToFile(String accountName, List<String> operationInfo) {
        if (!operationInfo.isEmpty()) {
            Path path = Paths.get(accountName + ".txt");
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {
                for (int i = 0; i < operationInfo.size(); i++) {
                    try {
                        writer.write(operationInfo.get(i));
                        writer.write("-------------------------");
                        writer.newLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return "Done";
        } else {
            return "Fail";
        }

    }

}
