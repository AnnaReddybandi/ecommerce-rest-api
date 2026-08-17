package com.example.ecommerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for serving uploaded product images.
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Value("${file.upload.dir}")
    private String uploadDirectory;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String absolutePath = uploadDirectory;

        if (!absolutePath.endsWith("/")) {
            absolutePath += "/";
        }

        registry.addResourceHandler("/uploads/products/**")
                .addResourceLocations(
                        "file:" + absolutePath
                );
    }
}