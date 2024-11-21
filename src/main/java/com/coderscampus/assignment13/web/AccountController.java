package com.coderscampus.assignment13.web;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.AccountService;
import com.coderscampus.assignment13.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;

    @GetMapping("/users/{userId}/accounts/{accountid}")
    public String viewOneUserAccount(ModelMap model, @PathVariable Long userId, @PathVariable Long accountid) {
        Account account = accountService.findAccountById(accountid);
        User user = userService.findById(userId);
        model.put("account", account);
        model.put("users", Arrays.asList(user));
        model.put("user", user);
        return "account";
    }

    @PostMapping("/users/{userId}/accounts")
    public String postOneAccount(@PathVariable Long userId, ModelMap model) {

        User user = userService.findById(userId);


        Account newAccount = new Account();
        int accountNumber = user.getAccounts().size() + 1; // Generate a new account number based on user accounts count
        newAccount.setAccountName("Account#" + accountNumber);
        newAccount.getUsers().add(user);
        user.getAccounts().add(newAccount);


        accountService.save(newAccount);


        return "redirect:/users/" + userId + "/accounts/" + newAccount.getAccountId();
    }

    @PostMapping("/users/{userId}/accounts/{accountid}")
    public String updateOneAccount(@PathVariable Long userId, @PathVariable Long accountid, Account updatedAccount) {
        Account existingAccount = accountService.findAccountById(accountid);

        if (existingAccount != null) {
            existingAccount.setAccountName(updatedAccount.getAccountName());
            accountService.save(existingAccount);
        }

        return "redirect:/users/" + userId + "/accounts/" + accountid;
    }

}
