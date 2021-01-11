package ru.netology.money_transfer_service.controller;

import org.springframework.web.bind.annotation.*;
import ru.netology.money_transfer_service.objects.User;
import ru.netology.money_transfer_service.service.TransferMoneyService;
import ru.netology.money_transfer_service.service.ViewUserInfoService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/")
public class Controller {
    ViewUserInfoService viewUserInfoService;
    TransferMoneyService transferMoneyService;

    public Controller(ViewUserInfoService viewUserInfoService, TransferMoneyService transferMoneyService) {
        this.viewUserInfoService = viewUserInfoService;
        this.transferMoneyService = transferMoneyService;
    }

    @GetMapping("user/accounts")
    public String getUserAccounts(@RequestParam String login, @RequestParam String password) {
        return viewUserInfoService.getUserAccounts(login, password);
    }

    @GetMapping("user/transfer")
    public String transferMoneyFromTo(@RequestParam String login, @RequestParam String password,
                                      @RequestParam String accountFrom, @RequestParam String accountTo,
                                      @RequestParam BigDecimal ammount) {
        return transferMoneyService.transferMoneyFromTo(login,password,accountFrom,accountTo,ammount);
    }
}
