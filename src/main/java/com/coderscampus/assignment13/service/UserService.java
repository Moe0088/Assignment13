package com.coderscampus.assignment13.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.coderscampus.assignment13.domain.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.repository.AccountRepository;
import com.coderscampus.assignment13.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private AccountRepository accountRepo;

    public List<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public List<User> findByNameAndUsername(String name, String username) {
        return userRepo.findByNameAndUsername(name, username);
    }

    public List<User> findByCreatedDateBetween(LocalDate date1, LocalDate date2) {
        return userRepo.findByCreatedDateBetween(date1, date2);
    }

    public User findExactlyOneUserByUsername(String username) {
        List<User> users = userRepo.findExactlyOneUserByUsername(username);
        if (users.size() > 0)
            return users.get(0);
        else
            return new User();
    }

    public Set<User> findAll() {
        return userRepo.findAllUsersWithAccountsAndAddresses();
    }

    public User findById(Long userId) {
        Optional<User> userOpt = userRepo.findById(userId);
        return userOpt.orElse(new User());
    }


    public User saveUser(User user) {
        if (user.getUserId() == null) {
            Account checking = new Account();
            checking.setAccountName("Checking Account");
            checking.getUsers().add(user);
            Account savings = new Account();
            savings.setAccountName("Savings Account");
            savings.getUsers().add(user);

            user.getAccounts().add(checking);
            user.getAccounts().add(savings);
            accountRepo.save(checking);
            accountRepo.save(savings);
        }
        if (user.getAddress() != null) {
            Address address = user.getAddress();
            address.setUser(user);
        }
        return userRepo.save(user);
    }

    public void delete(Long userId) {
        userRepo.deleteById(userId);
    }

    @Transactional
    public User updateOneUser(Long userId, User user) {
        User existingUser = findById(userId);


        existingUser.setUsername(user.getUsername());
        existingUser.setName(user.getName());


        if (existingUser.getPassword() != null && existingUser.getPassword().isEmpty()) {
            existingUser.setPassword(user.getPassword());
        }

        if (existingUser.getAddress() == null) {
            existingUser.setAddress(new Address());
        }
        existingUser.getAddress().setUser(existingUser);
        if (user.getAddress() != null) {

            existingUser.getAddress().setAddressLine1(user.getAddress().getAddressLine1());
            existingUser.getAddress().setAddressLine2(user.getAddress().getAddressLine2());
            existingUser.getAddress().setCity(user.getAddress().getCity());
            existingUser.getAddress().setRegion(user.getAddress().getRegion());
            existingUser.getAddress().setCountry(user.getAddress().getCountry());
            existingUser.getAddress().setZipCode(user.getAddress().getZipCode());
        }
        return userRepo.save(existingUser);
    }


}


