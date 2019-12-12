package com.splitoil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SplitoilMonoApplication {

    public static void main(String[] args) {
        final SomeClass someClass = new SomeClass();
        someClass.setValue("siema");
        SpringApplication.run(SplitoilMonoApplication.class, args);
    }

}
