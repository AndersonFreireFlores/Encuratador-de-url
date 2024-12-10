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

public UrlDTO shortenUrl(String originalUrl) {
    Optional<Url> existingUrl = urlRepository.findByUrl(originalUrl);
    if (existingUrl.isPresent()) {
        return urlMapper.convert(existingUrl.get());
    }

    Url url = new Url();
    url.setUrl(originalUrl);
    url.setCreatedAt(new Date());
    url.setAccessCount(0);

   url = urlRepository.save(url); // Save first to get the ID
String shortCode = encode(url.getUrl());
url.setShortCode(shortCode);
urlRepository.save(url); // Save again with the short code

return urlMapper.convert(url);
}

private String encode(String url) {
    String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder shortCode = new StringBuilder();
    int hashCode = url.hashCode();
    while (hashCode > 0) {
        shortCode.append(characters.charAt(hashCode % 62));
        hashCode /= 62;
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

    public boolean urlExists(String url) {
        return urlRepository.findByUrl(url).isPresent();
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
