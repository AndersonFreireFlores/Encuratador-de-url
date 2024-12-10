package com.example.encurtadordeurl.Controllers;

import com.example.encurtadordeurl.Entities.UrlDTO;
import com.example.encurtadordeurl.Repositories.UrlRepository;
import com.example.encurtadordeurl.Services.UrlService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UrlControllerTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>(PostgreSQLContainer.IMAGE);

    @Autowired
    private UrlController urlController;

    @Autowired
    private UrlService urlService;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        postgresContainer.start();
    }

    @AfterEach
    void tearDown() {
        postgresContainer.stop();
    }

    @Test
    void getOriginalUrl() {
        UrlDTO dto = urlService.shortenUrl("www.google.com");
        String originalUrl = urlController.getOriginalUrl(dto.getShortCode());
        assertEquals("www.google.com", originalUrl);
    }

    @Test
    void getShortCode() {
        UrlDTO dto = urlService.shortenUrl("www.google.com");
        String shortCode = urlController.getShortCode(dto.getUrl());
        assertEquals(dto.getShortCode(), shortCode);
    }

    @Test
    void getAccessCount() {
    }

    @Test
    void createUrl() {
    }

    @Test
    void updateUrl() {
    }

    @Test
    void deleteUrl() {
    }
}