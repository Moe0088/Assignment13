package com.coderscampus.assignment13.service;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {


    @Autowired
    private AccountRepository accountRepo;


    public void save(Account account) {
        accountRepo.save(account);
    }

    public void delete(Long accountId) {
        accountRepo.deleteById(accountId);
    }

    public Account findAccountById(Long accountId) {
        Optional<Account> accountOpt = accountRepo.findById(accountId);

        return accountOpt.orElse(new Account());
    }

    public void create(Account account) {
        accountRepo.save(account);
    }
}
