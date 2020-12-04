package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "create_date")
    LocalDateTime createDate;
    
    @Column(name = "update_date")
    LocalDateTime updateDate;

    @Column(name = "title")
    String title;

    @Column(name = "text")
    String text;

    @Column(name = "description")
    String description;

    @Column(name = "photo_url")
    String photoUrl;

    @Column(name = "source")
    String source;

}
