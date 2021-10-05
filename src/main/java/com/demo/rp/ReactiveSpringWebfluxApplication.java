package com.demo.rp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReactiveSpringWebfluxApplication {

    public static void main(String[] args) {
        //BlockHound.install();
        SpringApplication.run(ReactiveSpringWebfluxApplication.class, args);
    }
}
