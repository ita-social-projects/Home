package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.Builder;

@Builder
public class NewsDto extends BaseDto {

    private Long id;

    private String title;

    private String photoUrl;

    private String description;

    private String source;

    private String text;

    public NewsDto() {
    }

    public NewsDto(Long id, String title, String photoUrl, String description, String source, String text) {
        this.id = id;
        this.title = title;
        this.photoUrl = photoUrl;
        this.description = description;
        this.source = source;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
