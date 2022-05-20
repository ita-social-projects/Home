package com.softserveinc.ita.homeproject.readerapp.models;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class ReaderUser {

    @Id
    private String id;

    private int age;

    private String name;

    private String surname;

    public ReaderUser() {
    }

    public ReaderUser(String id, int age, String name, String surname) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.surname = surname;
    }
}
