package com.example.techcentral.service.imageService;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "firebase")
public class Properties {

    private String bucketName;

    private String imageUrl;
}
