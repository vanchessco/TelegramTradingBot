package com.mi.api_tinkoff.v1.controller;


import com.mi.api_tinkoff.v1.service.operation.OperationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/operation")
@AllArgsConstructor
public class OperationRestController {

    private OperationService operationService;


    @GetMapping(path = "/report/operations")
    public ResponseEntity<byte[]> getOperations(@RequestParam("id") String accountID) {
        byte[] response = operationService.findOperations(accountID);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
