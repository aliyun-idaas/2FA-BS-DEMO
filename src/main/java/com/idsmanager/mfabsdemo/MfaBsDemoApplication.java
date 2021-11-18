package com.idsmanager.mfabsdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @since 1.0.0
 */
@ServletComponentScan
@SpringBootApplication
public class MfaBsDemoApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MfaBsDemoApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(MfaBsDemoApplication.class, args);
    }

}
