package ru.netology.moneytransferservice.repository;

import org.springframework.stereotype.Repository;
import ru.netology.moneytransferservice.entity.UserAccount;
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
                "11/21", "222", 100000, "RUR");
        UserAccount userAccount3 = new UserAccount("3333333333333333",
                "11/21", "333", 100000, "RUR");
        UserAccount userAccount4 = new UserAccount("4444444444444444",
                "11/21", "444", 100000, "RUR");

        usersAccountRepository.put(userAccount1.getCardNumber(), userAccount1);
        usersAccountRepository.put(userAccount2.getCardNumber(), userAccount2);
        usersAccountRepository.put(userAccount3.getCardNumber(), userAccount3);
        usersAccountRepository.put(userAccount4.getCardNumber(), userAccount4);
    }

    public UserAccount getUserAccount(String cardNumber) {
        return usersAccountRepository.get(cardNumber);
    }

    public void setUserAccount(UserAccount userAccount) {
        usersAccountRepository.put(userAccount.getCardNumber(), userAccount);
    }
}
