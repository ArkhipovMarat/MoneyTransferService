package ru.netology.money_transfer_service.service;

import org.springframework.stereotype.Service;
import ru.netology.money_transfer_service.repository.UsersRepository;

import java.math.BigDecimal;

@Service
public class TransferMoneyService {
    UsersRepository usersRepository;

    public TransferMoneyService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public String transferMoneyFromTo(String login, String password, String accountFrom, String accountTo, BigDecimal ammount) {
        return usersRepository.transferMoneyFromTo(login,password,accountFrom,accountTo,ammount);
    }
}
