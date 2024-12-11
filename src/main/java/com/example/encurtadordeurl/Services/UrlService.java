package com.example.encurtadordeurl.Services;

import com.example.encurtadordeurl.Entities.Url;
import com.example.encurtadordeurl.Entities.UrlDTO;
import com.example.encurtadordeurl.Entities.UrlMapper;
import com.example.encurtadordeurl.Repositories.UrlRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Optional;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final UrlMapper urlMapper;

    public UrlService(UrlRepository urlRepository, UrlMapper urlMapper) {
        this.urlRepository = urlRepository;
        this.urlMapper = urlMapper;
    }

    @Transactional
    public UrlDTO shortenUrl(String originalUrl) {
        Optional<Url> existingUrl = urlRepository.findByUrl(originalUrl);
        if (existingUrl.isPresent()) {
            return urlMapper.convert(existingUrl.get());
        }

        Url url = new Url();
        url.setUrl(originalUrl);
        url.setCreatedAt(new Date());
        url.setAccessCount(0);


        String shortCode = encode(originalUrl);
        url.setShortCode(shortCode);
        urlRepository.save(url);

        return urlMapper.convert(url);
    }

    private String encode(String url) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder shortCode = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            shortCode.append(characters.charAt(random.nextInt(characters.length())));
        }
        return shortCode.toString();
    }

    @Transactional
    public UrlDTO getUrlByShortCode(String shortCode) {
        Url url = urlRepository.findByShortCode(shortCode).orElseThrow(
                () -> new IllegalArgumentException("URL not found"));

        url.setAccessCount(url.getAccessCount() + 1);
        urlRepository.save(url);

        return urlMapper.convert(url);
    }

    public UrlDTO getUrlByUrl(String url) {
        Url url1 = urlRepository.findByUrl(url).orElseThrow(
                () -> new IllegalArgumentException("URL not found"));
        return urlMapper.convert(url1);
    }


    public boolean urlExists(String url) {
        return urlRepository.findByUrl(url).isPresent();
    }

    @Transactional
    public void deleteUrl(String shortCode) {
        Url url = urlRepository.findByShortCode(shortCode).orElseThrow(
                () -> new IllegalArgumentException("URL not found"));
        urlRepository.delete(url);
    }

    @Transactional
    public UrlDTO updateUrl(String shortCode, UrlDTO urlDTO) {
        Url url = urlRepository.findByShortCode(shortCode).orElseThrow(
                () -> new IllegalArgumentException("URL not found"));
        Url updatedUrl = urlMapper.convert(urlDTO);
        updatedUrl.setId(url.getId());
        urlRepository.save(updatedUrl);
        return urlMapper.convert(updatedUrl);
    }

}
