package com.example.encurtadordeurl.Controllers;

import com.example.encurtadordeurl.Entities.UrlDTO;
import com.example.encurtadordeurl.Services.UrlService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UrlControllerTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>(PostgreSQLContainer.IMAGE);

    @Autowired
    private UrlController urlController;

    @Autowired
    private UrlService urlService;

    @Autowired
    private TestRestTemplate restTemplate;

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
        UrlDTO dto = urlService.shortenUrl("www.google.com");
        int accessCount = urlController.getAccessCount(dto.getShortCode());
        assertEquals(1, accessCount);
    }

    @Test
    void createUrl() {
        String url = "www.google.com";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(url, headers);

        ResponseEntity<UrlDTO> response = restTemplate.exchange(
                "/shorten", HttpMethod.POST, request, UrlDTO.class);

        UrlDTO dto = urlService.getUrlByUrl(url);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(url, response.getBody().getUrl());
        assertEquals("www.google.com", dto.getUrl());
    }


    @Test
    void updateUrl() {
        UrlDTO dto = urlService.shortenUrl("www.google.com");
        String shortCode = dto.getShortCode();

        UrlDTO updatedDto = new UrlDTO();
        updatedDto.setUrl("www.updated.com");
        updatedDto.setShortCode(shortCode);
        updatedDto.setCreatedAt(dto.getCreatedAt());
        updatedDto.setAccessCount(0);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UrlDTO> request = new HttpEntity<>(updatedDto, headers);

        ResponseEntity<UrlDTO> response = restTemplate.exchange(
                "/shorten/" + shortCode, HttpMethod.PUT, request, UrlDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        UrlDTO updatedUrl = urlService.getUrlByShortCode(shortCode);
        assertNotNull(updatedUrl);
        assertEquals("www.updated.com", updatedUrl.getUrl());
    }

}