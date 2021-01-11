package ru.netology.money_transfer_service.service;

import org.springframework.stereotype.Service;
import ru.netology.money_transfer_service.repository.UsersRepository;

@Service
public class ViewUserInfoService {
    UsersRepository usersRepository;

    public ViewUserInfoService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public String getUserAccounts(String login, String password) {
        return usersRepository.getUserAccounts(login,password);
    }
}
