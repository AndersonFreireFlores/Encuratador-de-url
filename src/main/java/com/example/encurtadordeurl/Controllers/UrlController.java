package com.example.encurtadordeurl.Controllers;

import com.example.encurtadordeurl.Entities.UrlDTO;
import com.example.encurtadordeurl.Services.UrlService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/shorten/longUrl/{shortCode}")
    public String getOriginalUrl(@PathVariable String shortCode) {
        UrlDTO dto = urlService.getUrlByShortCode(shortCode);
        return dto.getUrl();
    }

    @GetMapping("/shorten/shortUrl/{longUrl}")
    public String getShortCode(@PathVariable String longUrl) {
        UrlDTO dto = urlService.getUrlByUrl(longUrl);
        return dto.getShortCode();
    }

    @GetMapping("/shorten/accessCount/{shortCode}")
    public int getAccessCount(@PathVariable String shortCode) {
        UrlDTO dto = urlService.getUrlByShortCode(shortCode);
        return dto.getAccessCount();
    }

    @PostMapping("/shorten")
    public UrlDTO createUrl(@RequestBody String url) {
        if (urlService.urlExists(url)) {
            Optional<UrlDTO> dto = Optional.ofNullable(urlService.getUrlByUrl(url));
            return dto.get();
        }
        return urlService.shortenUrl(url);
    }

    @PutMapping("/shorten/{shortCode}")
    public UrlDTO updateUrl(@PathVariable String shortCode, @RequestBody @Validated UrlDTO url) {
        return urlService.updateUrl(shortCode, url);
    }

    @DeleteMapping("/shorten/{shortCode}")
    public void deleteUrl(@PathVariable String shortCode) {
        urlService.deleteUrl(shortCode);
    }




}
