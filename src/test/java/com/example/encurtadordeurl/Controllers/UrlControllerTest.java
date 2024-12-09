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