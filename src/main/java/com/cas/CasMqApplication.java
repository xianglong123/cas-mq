package com.cas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.cas")
public class CasMqApplication {

    public static void main(String[] args) {
        SpringApplication.run(CasMqApplication.class, args);
    }

}
