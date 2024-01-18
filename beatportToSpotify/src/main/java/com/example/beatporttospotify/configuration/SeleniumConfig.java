package com.example.beatporttospotify.configuration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class SeleniumConfig {
    @Bean
    public WebDriver webDriver() {
        Path path = Paths.get("drivers/chromedriver");
        String absolutePath = path.toAbsolutePath().toString();
        System.out.println(absolutePath);
        System.setProperty("webdriver.chrome.driver", absolutePath);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");


        return new ChromeDriver(options);
    }
}
