package ru.netology.money_transfer_service.repository;

import org.springframework.stereotype.Repository;
import ru.netology.money_transfer_service.entity.MoneyData;
import ru.netology.money_transfer_service.entity.OperationData;
import ru.netology.money_transfer_service.entity.UserAccount;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class UsersAccountRepository {
    public ConcurrentMap<String, UserAccount> usersAccountRepository;

    public UsersAccountRepository() {
        this.usersAccountRepository = new ConcurrentHashMap<>();

        //        some DB emulation
        UserAccount userAccount1 = new UserAccount("1111111111111111",
                "11/21", "111", 100000, "RUR");
        UserAccount userAccount2 = new UserAccount("2222222222222222",
                "11/22", "222", 100000, "RUR");
        usersAccountRepository.put(userAccount1.getCardNumber(), userAccount1);
        usersAccountRepository.put(userAccount2.getCardNumber(), userAccount2);
    }

    public UserAccount getUserAccount(String cardNumber) {
        return usersAccountRepository.get(cardNumber);
    }

    public void setUserAccount(UserAccount userAccount) {
        usersAccountRepository.put(userAccount.getCardNumber(), userAccount);
    }
}
