package org.telegrambot.demobot;import org.springframework.boot.SpringApplication;import org.springframework.boot.autoconfigure.SpringBootApplication;@SpringBootApplicationpublic class DemoBotApplication {    public static void main(String[] args) {        String version =  System.getenv("VERSION") != null ? System.getenv("VERSION") : "unknown";        System.out.println("----------VERSION: " + version);        SpringApplication.run(DemoBotApplication.class, args);    }}