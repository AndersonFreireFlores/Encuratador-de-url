package com.example.encurtadordeurl.Entities;

import java.util.Date;

public class UrlDTO {

    private String url;
    private String shortCode;
    private Date createdAt;
    private int accessCount;

    public UrlDTO() {
    }

    public UrlDTO(String url, String shortCode, Date createdAt, int accessCount) {
        this.url = url;
        this.shortCode = shortCode;
        this.createdAt = createdAt;
        this.accessCount = accessCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(int accessCount) {
        this.accessCount = accessCount;
    }
}
