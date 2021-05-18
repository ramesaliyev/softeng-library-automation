package com.ytusofteng.model.entities;

import java.util.Date;

public class Book extends Entity {
    private String author;

    public Book(int id, int issueCount, String name, Date createdAt, String author) {
        super(id, issueCount, name, createdAt);
        this.author = author;
    }
}
