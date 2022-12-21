package com.mi.rabbit_mq_consumer.rabbit_mq.consumer.message.converter;

public interface MessageConverter {

    static String updateMessage(String message) {
        String[] data = message.split(",");
        String response;
        if (checkMessage(message, "request_order")) {
            response = data[0].endsWith("buy") ? "buy" : "sell";
            return response;
        } else if (checkMessage(message, "request_one_account")) {
            response = "request_one_account";
            return response;
        } else if (checkMessage(message, "request_positions")) {
            response = "request_positions";
            return response;
        } else if (checkMessage(message, "request_operations")) {
            response = "request_operations";
            return response;
        } else if (checkMessage(message, "request_instrument")) {
            response = "request_instrument";
            return response;
        }

        return message;
    }

    static boolean checkMessage(String message, String request) {
        return message.startsWith(request);
    }
}
