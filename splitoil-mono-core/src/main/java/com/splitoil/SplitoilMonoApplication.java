package com.splitoil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.mediatype.hal.HalConfiguration;

@SpringBootApplication
public class SplitoilMonoApplication {

    public static void main(String[] args) {
        final SomeClass someClass = new SomeClass();
        someClass.setValue("siema");
        SpringApplication.run(SplitoilMonoApplication.class, args);
    }

    @Bean
    public HalConfiguration globalPolicy() {
        return new HalConfiguration()
            .withRenderSingleLinks(HalConfiguration.RenderSingleLinks.AS_ARRAY);
    }

}
