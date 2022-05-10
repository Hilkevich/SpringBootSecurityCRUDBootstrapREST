package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /* настройка маппинга для страниц, которым не нужен отдельный контроллер, т.к
    они никак не обрабатываются,а просто возвращаются сервером.*/

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/admin").setViewName("adminPage");
    }
}
