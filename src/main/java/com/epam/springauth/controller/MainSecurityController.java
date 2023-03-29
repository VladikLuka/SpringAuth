package com.epam.springauth.controller;

import com.epam.springauth.model.UserEntity;
import com.epam.springauth.repository.UserRepository;
import com.epam.springauth.service.LoginAttemptService;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@AllArgsConstructor
public class MainSecurityController {

    private UserRepository userRepository;
    private LoginAttemptService loginAttemptService;

    @GetMapping("/info")
    public String getInfo(){
        return "info";
    }

    @GetMapping("/about")
    public String getAboutInfo(){
        return "about";
    }

    @GetMapping("/admin")
    public String getAdmin(){
        return "admin";
    }

    @GetMapping("/loginb")
    public String login(){
        return "login";
    }

    @GetMapping("/logoutSuccess")
    public String logoutSuccess(){
        return "logout";
    }

    @GetMapping("/blocked")
    public String blocked (Model model) {
        List<UserEntity> UserEntities = userRepository.findAll();
        Map<String, LocalDateTime> blockedUsers = UserEntities.stream()
                .map(UserEntity::getUserName)
        .filter (userName -> loginAttemptService.isBlocked(userName))
                .collect(Collectors.toMap (user -> user, user -> loginAttemptService.getCachedValue(user).getBlockedTimeStamp()));

        if (!blockedUsers.isEmpty()) {
            model. addAttribute ("blockedUsers", blockedUsers) ;
        }
        return "blocked";
    }
}
