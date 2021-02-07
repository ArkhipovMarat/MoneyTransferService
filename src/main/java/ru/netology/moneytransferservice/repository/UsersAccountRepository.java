package ru.netology.moneytransferservice.repository;

import org.springframework.stereotype.Repository;
import ru.netology.moneytransferservice.entity.UserAccount;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class UsersAccountRepository {
    public ConcurrentMap<String, UserAccount> usersAccountRepository;

    // INPUT DATA for DB
    private static final String DATA_CARD_FROM_NUMBER_1 = "1111111111111111";
    private static final String DATA_CARD_FROM_NUMBER_2 = "2222222222222222";
    private static final String DATA_CARD_FROM_NUMBER_3 = "3333333333333333";
    private static final String DATA_CARD_FROM_NUMBER_4 = "4444444444444444";
    private static final String DATA_CARD_FROM_CVV = "111";
    private static final String DATA_CARD_FROM_VALID_TILL = "11/21";
    private static final String CURRENCY = "RUR";
    private static final int AMOUNT_VALUE = 100000;


    public UsersAccountRepository() {
        //  some DB emulation
        this.usersAccountRepository = new ConcurrentHashMap<>();

        UserAccount userAccount1 = new UserAccount(DATA_CARD_FROM_NUMBER_1, DATA_CARD_FROM_VALID_TILL,
                DATA_CARD_FROM_CVV, AMOUNT_VALUE, CURRENCY);
        UserAccount userAccount2 = new UserAccount(DATA_CARD_FROM_NUMBER_2, DATA_CARD_FROM_VALID_TILL,
                DATA_CARD_FROM_CVV, AMOUNT_VALUE, CURRENCY);
        UserAccount userAccount3 = new UserAccount(DATA_CARD_FROM_NUMBER_3, DATA_CARD_FROM_VALID_TILL,
                DATA_CARD_FROM_CVV, AMOUNT_VALUE, CURRENCY);
        UserAccount userAccount4 = new UserAccount(DATA_CARD_FROM_NUMBER_4, DATA_CARD_FROM_VALID_TILL,
                DATA_CARD_FROM_CVV, AMOUNT_VALUE, CURRENCY);

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
