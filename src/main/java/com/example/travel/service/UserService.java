package com.example.travel.service;

import com.example.travel.domain.Account;
import com.example.travel.domain.User;
import com.example.travel.repository.AccountRepository;
import com.example.travel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public User getUser(Principal principal){

        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("not found user"));
    }

    public long getUserId(Principal principal){
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("not found user")).getUserId();
    }

    public List<User> findAllUser(){
        return userRepository.findAll();
    }

    public List<Account> findAllAccount(){
        return accountRepository.findAll();
    }
}
