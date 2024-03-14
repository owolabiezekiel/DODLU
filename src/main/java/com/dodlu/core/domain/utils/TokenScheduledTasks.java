package com.dodlu.core.domain.utils;


import com.dodlu.core.domain.user.service.abstracts.TokenGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenScheduledTasks {
    @Autowired
    private TokenGeneratorService tokenGeneratorService;

    @Scheduled(cron = "0 * * * * *")
    public void execute(){
        tokenGeneratorService.cleanUpToken();
    }
}
