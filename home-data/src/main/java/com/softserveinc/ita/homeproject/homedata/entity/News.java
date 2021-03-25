package com.softserveinc.ita.homeproject.homedata.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@SequenceGenerator(name = "sequence", sequenceName = "news_sequence")
public class News extends BaseEntity {

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "description")
    private String description;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "source")
    private String source;

    @Column(name = "enabled")
    private Boolean enabled;
}
