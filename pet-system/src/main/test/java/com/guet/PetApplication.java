package com.guet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.Filter;


@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class PetApplication  {
    public static void main(String[] args) {
            SpringApplication.run(PetApplication.class, args);
            log.info("PetApplication is running...");
    }
}
