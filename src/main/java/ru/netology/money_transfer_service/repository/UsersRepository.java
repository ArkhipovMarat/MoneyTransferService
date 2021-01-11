package ru.netology.money_transfer_service.repository;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class UsersRepository {
    //    testMethod
    public String getUserAccounts(String login, String password) {
        return userValidation(login,password)?"User Accounts":"validation false";
    }

    //    testValidation
    private boolean userValidation (String login, String password) {
        return true;
    }

    //    testMethod
    public String transferMoneyFromTo(String login, String password, String accountFrom, String accountTo, BigDecimal ammount) {
        return userValidation(login,password)?"Transfer success":"validation false";
    }
}
