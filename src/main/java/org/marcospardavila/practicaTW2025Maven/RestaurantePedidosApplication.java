package org.marcospardavila.practicaTW2025Maven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class RestaurantePedidosApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RestaurantePedidosApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(RestaurantePedidosApplication.class, args);
    }
}
