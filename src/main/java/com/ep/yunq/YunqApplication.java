package com.ep.yunq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
//@ServletComponentScan
@EnableOpenApi
public class YunqApplication {

    public static void main(String[] args) {
        SpringApplication.run(YunqApplication.class, args);
    }

}
