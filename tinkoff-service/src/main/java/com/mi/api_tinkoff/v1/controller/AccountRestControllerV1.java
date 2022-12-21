package com.mi.api_tinkoff.v1.controller;


import com.mi.api_tinkoff.v1.service.account.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
@AllArgsConstructor
public class AccountRestControllerV1 {

    private AccountService accountService;

    @GetMapping(path = "/all")
    public ResponseEntity<byte[]> getAccounts() {
        byte[] accounts = accountService.findAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<byte[]> getAccount(@RequestParam("id") String accountID) {
        byte[] account = accountService.findAccount(accountID);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @GetMapping(path = "/portfolio/position_list")
    public ResponseEntity<byte[]> getPositions(@RequestParam("id") String accountID) {
        byte[] positions = accountService.findPositions(accountID);
        return new ResponseEntity<>(positions, HttpStatus.OK);
    }

}
