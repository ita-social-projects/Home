package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
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

}
