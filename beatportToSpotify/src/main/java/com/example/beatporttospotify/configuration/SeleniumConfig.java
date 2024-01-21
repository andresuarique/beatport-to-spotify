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
        //Path path = Paths.get("drivers/chromedriver.exe");
        Path path = Paths.get("/usr/bin/chromedriver");
        String absolutePath = path.toAbsolutePath().toString();
        System.out.println(absolutePath);
        System.setProperty("webdriver.chrome.driver", absolutePath);
        System.setProperty("webdriver.chrome.verboseLogging", "true");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");



        return new ChromeDriver(options);
    }
}
