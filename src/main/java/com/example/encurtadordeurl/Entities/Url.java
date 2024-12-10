package com.example.encurtadordeurl.Entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.Date;

@Entity
@Table(name = "tb_urls", uniqueConstraints = {
        @UniqueConstraint(columnNames = "url"),
        @UniqueConstraint(columnNames = "shortCode")
})
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String url;
    @NonNull
    private String shortCode;
    @NonNull
    private Date createdAt;
    @NonNull
    private int accessCount;

    public Url() {
    }

    public Url(@NonNull String url, @NonNull String shortCode, @NonNull Date createdAt, int accessCount) {
        this.url = url;
        this.shortCode = shortCode;
        this.createdAt = createdAt;
        this.accessCount = accessCount;
    }

    public Url(long id, String url, String shortCode, Date createdAt, int accessCount) {
        this.id = id;
        this.url = url;
        this.shortCode = shortCode;
        this.createdAt = createdAt;
        this.accessCount = accessCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
