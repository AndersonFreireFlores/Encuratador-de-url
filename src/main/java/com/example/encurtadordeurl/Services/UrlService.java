package com.example.encurtadordeurl.Services;

import com.example.encurtadordeurl.Entities.Url;
import com.example.encurtadordeurl.Entities.UrlDTO;
import com.example.encurtadordeurl.Entities.UrlMapper;
import com.example.encurtadordeurl.Repositories.UrlRepository;
import org.springframework.stereotype.Service;

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

    public String shortenUrl(String originalUrl) {
        Optional<Url> existingUrl = urlRepository.findByUrl(originalUrl);
        if (existingUrl.isPresent()) {
            return existingUrl.get().getShortCode();
        }

        Url url = new Url();
        url.setUrl(originalUrl);
        url.setCreatedAt(new Date());
        url.setAccessCount(0);

        url = urlRepository.save(url);
        String shortCode = encode(url.getId());
        url.setShortCode(shortCode);
        urlRepository.save(url);

        return shortCode;
    }

    private String encode(long id) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder shortCode = new StringBuilder();
        while (id > 0) {
            shortCode.append(characters.charAt((int) (id % 62)));
            id /= 62;
        }
        return shortCode.reverse().toString();
    }


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

    public String saveUrl(UrlDTO urlDTO) {
        Url url = urlMapper.convert(urlDTO);
        url = urlRepository.save(url);
        return url.getShortCode();
    }

    public void deleteUrl(String shortCode) {
        Url url = urlRepository.findByShortCode(shortCode).orElseThrow(
                () -> new IllegalArgumentException("URL not found"));
        urlRepository.delete(url);
    }

    public void updateUrl(String shortCode, UrlDTO urlDTO) {
        Url url = urlRepository.findByShortCode(shortCode).orElseThrow(
                () -> new IllegalArgumentException("URL not found"));
        Url updatedUrl = urlMapper.convert(urlDTO);
        updatedUrl.setId(url.getId());
        urlRepository.save(updatedUrl);
    }

}
