package com.epam.springauth;

import com.epam.springauth.model.UserEntity;
import com.epam.springauth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@Slf4j
public class SpringAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAuthApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(UserRepository repository){
        return (args) -> {
            List<UserEntity> users = repository.findAll();
            log.info("UserEntity records: ");
            log.info("-------------");
            log.info(users.toString());
            log.info("");
        };
    }
}
