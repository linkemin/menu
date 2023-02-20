package com.lkm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

/**
 * Hello world!
 *
 */
@EnableScheduling
@SpringBootApplication
public class App 
{
    public static void main( String[] args ){
        SpringApplication.run(App.class, args);
    }
}
