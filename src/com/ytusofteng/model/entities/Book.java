package com.ytusofteng.model.entities;

import com.ytusofteng.model.enums.BookType;

import java.util.Date;

public class Book extends Entity {
    private BookType type;
    private String author;

    public Book(int id, int issueCount, String name, Date createdAt, String author, BookType type) {
        super(id, issueCount, name, createdAt);
        this.author = author;
        this.type = type;
    }
}
