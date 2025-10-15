package com.example.upgradeagent;

import com.example.upgradeagent.controller.UpgradingAssistant;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootUpgradeAgent implements ApplicationRunner {

    public static void main(final String[] args) {
        SpringApplication app = new SpringApplication(SpringBootUpgradeAgent.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        UpgradingAssistant upgradingAssistant = UpgradingAssistant.getInstance();
        upgradingAssistant.run();
    }
}
