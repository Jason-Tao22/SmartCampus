package com.atguigu.campus.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Value("${upload.file.location}")
    private String fileName;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/upload/**")){
            registry.addResourceHandler("/upload/**").addResourceLocations("file:".concat(fileName));
        }
    }
}
